package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.local.LetterEntity
import com.example.ui.components.EnvelopeCard
import com.example.ui.components.RandomLetterPickerModal
import com.example.ui.components.ShareCardDialog
import com.example.ui.components.WaxSealBadge
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepEmeraldDark
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MidnightNavySurface
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel

@Composable
fun HomeScreen(
    viewModel: ChithiGhorViewModel,
    onNavigateToLetterDetail: (LetterEntity) -> Unit,
    onNavigateToMailbox: (String) -> Unit,
    onNavigateToWrite: (String) -> Unit,
    onNavigateToPublicRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    val publicLetters by viewModel.publicLetters.collectAsStateWithLifecycle()
    val inboxLetters by viewModel.inboxLetters.collectAsStateWithLifecycle()
    val sentLetters by viewModel.sentLetters.collectAsStateWithLifecycle()
    val savedLetters by viewModel.savedLetters.collectAsStateWithLifecycle()
    val flyingLetters by viewModel.flyingLetters.collectAsStateWithLifecycle()

    var showRandomPicker by remember { mutableStateOf(false) }
    var shareLetterTarget by remember { mutableStateOf<LetterEntity?>(null) }

    // Featured / Today's Flying Letter
    val todayFlyingLetter = flyingLetters.firstOrNull() ?: publicLetters.firstOrNull()

    if (showRandomPicker) {
        RandomLetterPickerModal(
            availableLetters = publicLetters,
            onDismiss = { showRandomPicker = false },
            onLetterOpened = { letter ->
                showRandomPicker = false
                onNavigateToLetterDetail(letter)
            }
        )
    }

    if (shareLetterTarget != null) {
        ShareCardDialog(
            letter = shareLetterTarget!!,
            onDismiss = { shareLetterTarget = null },
            onShareAction = {
                // share handled in dialog
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Serene Header with Brand Name and Greeting
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(MidnightNavy, MidnightNavyDark)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "চিঠি ঘর",
                                color = SoftOffWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            WaxSealBadge(sealType = "crimson_wax", size = 22.dp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“আজ কী লিখবে?”",
                            color = MutedGoldBright,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    // Random letter discovery button (🎲 হঠাৎ একটি চিঠি)
                    Button(
                        onClick = { showRandomPicker = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MutedGold.copy(alpha = 0.2f),
                            contentColor = MutedGoldBright
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MutedGold.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("random_letter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "হঠাৎ চিঠি",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "হঠাৎ চিঠি",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 2. Main Hero Card: একটা চিঠি লিখুন with artwork
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
                    .shadow(8.dp, RoundedCornerShape(22.dp))
            ) {
                // Background artistic hero illustration
                Image(
                    painter = painterResource(id = R.drawable.chithi_ghor_hero_1789576847448),
                    contentDescription = "চিঠি ঘরের পরিবেশ",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                // Atmospheric gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MidnightNavyDark.copy(alpha = 0.35f),
                                    MidnightNavyDark.copy(alpha = 0.85f),
                                    MidnightNavyDark
                                )
                            )
                        )
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "✉️ একটা চিঠি লিখুন",
                        color = SoftOffWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "“মনের কথাগুলো একটু কাগজে রাখি?”",
                        color = MutedGoldBright,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToWrite("general") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MutedGoldBright,
                                contentColor = MidnightNavyDark
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("hero_write_letter_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "চিঠি লিখুন",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "চিঠি লিখুন",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { onNavigateToWrite("flying") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepEmerald,
                                contentColor = SoftOffWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF57D1A2).copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("hero_flying_letter_button")
                        ) {
                            Text(
                                text = "🕊️ উড়ো চিঠি",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 3. Section: 📮 আমার ডাকবাক্স (My Mailbox summary)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📮 আমার ডাকবাক্স",
                        color = SoftOffWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "সব দেখুন →",
                        color = MutedGoldBright,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onNavigateToMailbox("inbox") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Received / Inbox Box
                    MailboxStatCard(
                        icon = Icons.Default.Inbox,
                        title = "পাওয়া চিঠি",
                        count = inboxLetters.size,
                        onClick = { onNavigateToMailbox("inbox") },
                        modifier = Modifier.weight(1f)
                    )

                    // Sent Box
                    MailboxStatCard(
                        icon = Icons.Default.Send,
                        title = "পাঠানো চিঠি",
                        count = sentLetters.size,
                        onClick = { onNavigateToMailbox("sent") },
                        modifier = Modifier.weight(1f)
                    )

                    // Saved Box
                    MailboxStatCard(
                        icon = Icons.Default.Drafts,
                        title = "সংরক্ষিত",
                        count = savedLetters.size,
                        onClick = { onNavigateToMailbox("saved") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Section: 🕊️ আজকের উড়ো চিঠি (Letter of the Day)
        if (todayFlyingLetter != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🕊️ আজকের উড়ো চিঠি",
                            color = SoftOffWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MutedGold.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "নির্বাচিত", color = MutedGoldBright, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    EnvelopeCard(
                        letter = todayFlyingLetter,
                        onClick = { onNavigateToLetterDetail(todayFlyingLetter) },
                        onToggleSave = { viewModel.toggleSave(todayFlyingLetter) },
                        onToggleLike = { viewModel.toggleLike(todayFlyingLetter) },
                        onShare = { shareLetterTarget = todayFlyingLetter },
                        onReply = {
                            viewModel.startWriting(mode = "general", replyTo = todayFlyingLetter)
                            onNavigateToWrite("general")
                        }
                    )
                }
            }
        }

        // 5. Section: 📖 চিঠিঘরের পাতা (Curated Public Letters)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📖 চিঠিঘরের পাতা",
                        color = SoftOffWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "চিঠিঘরে প্রবেশ →",
                        color = MutedGoldBright,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onNavigateToPublicRoom() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(publicLetters.take(5)) { letter ->
                        Box(modifier = Modifier.width(280.dp)) {
                            EnvelopeCard(
                                letter = letter,
                                onClick = { onNavigateToLetterDetail(letter) },
                                onToggleSave = { viewModel.toggleSave(letter) },
                                onToggleLike = { viewModel.toggleLike(letter) },
                                onShare = { shareLetterTarget = letter },
                                onReply = {
                                    viewModel.startWriting(mode = "general", replyTo = letter)
                                    onNavigateToWrite("general")
                                }
                            )
                        }
                    }
                }
            }
        }

        // 6. Section: 🔖 মনে রাখা চিঠি (Recently saved letters)
        if (savedLetters.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Text(
                        text = "🔖 মনে রাখা চিঠি",
                        color = SoftOffWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    savedLetters.take(3).forEach { letter ->
                        Box(modifier = Modifier.padding(vertical = 4.dp)) {
                            EnvelopeCard(
                                letter = letter,
                                onClick = { onNavigateToLetterDetail(letter) },
                                onToggleSave = { viewModel.toggleSave(letter) },
                                onToggleLike = { viewModel.toggleLike(letter) },
                                onShare = { shareLetterTarget = letter }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MailboxStatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MidnightNavySurface)
            .border(1.dp, MutedGold.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MutedGoldBright,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                color = SoftOffWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = title,
                color = SoftOffWhite.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}
