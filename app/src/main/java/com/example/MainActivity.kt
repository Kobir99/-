package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LetterDetailScreen
import com.example.ui.screens.MailboxScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PublicLetterRoomScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WriteLetterScreen
import com.example.ui.theme.ChithiGhorTheme
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChithiGhorTheme(darkTheme = true) {
                ChithiGhorApp()
            }
        }
    }
}

@Composable
fun ChithiGhorApp(
    viewModel: ChithiGhorViewModel = viewModel()
) {
    var showSplash by remember { mutableStateOf(true) }
    val hasSeenOnboarding by viewModel.hasSeenOnboarding.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedLetter by viewModel.selectedLetter.collectAsStateWithLifecycle()

    val navItems = listOf(
        BottomNavItem("home", "বাড়ি", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("write", "লিখুন", Icons.Filled.Create, Icons.Outlined.Create),
        BottomNavItem("mailbox", "ডাকবাক্স", Icons.Filled.Mail, Icons.Outlined.MailOutline),
        BottomNavItem("letters", "চিঠিঘর", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        BottomNavItem("profile", "প্রোফাইল", Icons.Filled.Person, Icons.Outlined.PersonOutline)
    )

    when {
        showSplash -> {
            SplashScreen(onSplashFinished = { showSplash = false })
        }
        !hasSeenOnboarding -> {
            OnboardingScreen(onFinished = { viewModel.completeOnboarding() })
        }
        currentScreen == "detail" && selectedLetter != null -> {
            LetterDetailScreen(
                letter = selectedLetter!!,
                viewModel = viewModel,
                onBackClick = { viewModel.closeLetterDetail() },
                onReplyClick = { letter ->
                    viewModel.startWriting(mode = "general", replyTo = letter)
                }
            )
        }
        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.systemBars,
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .shadow(12.dp)
                            .background(MidnightNavy)
                            .border(0.8.dp, MutedGold.copy(alpha = 0.25f)),
                        containerColor = MidnightNavy,
                        tonalElevation = 0.dp
                    ) {
                        navItems.forEach { item ->
                            val isSelected = currentScreen == item.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MidnightNavyDark,
                                    selectedTextColor = MutedGoldBright,
                                    indicatorColor = MutedGoldBright,
                                    unselectedIconColor = SoftOffWhite.copy(alpha = 0.6f),
                                    unselectedTextColor = SoftOffWhite.copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.testTag("nav_${item.route}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            "home" -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToLetterDetail = { letter -> viewModel.openLetterDetail(letter) },
                                onNavigateToMailbox = { tab ->
                                    viewModel.setMailboxTab(tab)
                                    viewModel.navigateTo("mailbox")
                                },
                                onNavigateToWrite = { mode -> viewModel.startWriting(mode = mode) },
                                onNavigateToPublicRoom = { viewModel.navigateTo("letters") }
                            )
                            "write" -> WriteLetterScreen(
                                viewModel = viewModel,
                                onLetterSent = { viewModel.navigateTo("mailbox") }
                            )
                            "mailbox" -> MailboxScreen(
                                viewModel = viewModel,
                                onNavigateToLetterDetail = { letter -> viewModel.openLetterDetail(letter) }
                            )
                            "letters" -> PublicLetterRoomScreen(
                                viewModel = viewModel,
                                onNavigateToLetterDetail = { letter -> viewModel.openLetterDetail(letter) }
                            )
                            "profile" -> ProfileScreen(
                                viewModel = viewModel,
                                onNavigateToLetterDetail = { letter -> viewModel.openLetterDetail(letter) }
                            )
                            else -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToLetterDetail = { letter -> viewModel.openLetterDetail(letter) },
                                onNavigateToMailbox = { tab ->
                                    viewModel.setMailboxTab(tab)
                                    viewModel.navigateTo("mailbox")
                                },
                                onNavigateToWrite = { mode -> viewModel.startWriting(mode = mode) },
                                onNavigateToPublicRoom = { viewModel.navigateTo("letters") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "চিঠি ঘর: $name", modifier = modifier)
}
