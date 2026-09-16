package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.LetterEntity
import com.example.ui.components.EnvelopeCard
import com.example.ui.components.ShareCardDialog
import com.example.ui.components.WaxSealBadge
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MidnightNavySurface
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel
import kotlinx.coroutines.launch

@Composable
fun PublicLetterRoomScreen(
    viewModel: ChithiGhorViewModel,
    onNavigateToLetterDetail: (LetterEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val publicLetters by viewModel.publicLetters.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var showDirectJumpDialog by remember { mutableStateOf(false) }
    var directJumpInput by remember { mutableStateOf("") }
    var shareLetterTarget by remember { mutableStateOf<LetterEntity?>(null) }

    val categories = listOf(
        "সব", "ভালোবাসা", "বন্ধুত্ব", "পরিবার", "স্মৃতি",
        "অভিমান", "না-বলা কথা", "জীবন", "অপেক্ষা",
        "বিদায়", "কৃতজ্ঞতা", "নিজের সাথে কথা", "উড়ো চিঠি"
    )

    if (shareLetterTarget != null) {
        ShareCardDialog(
            letter = shareLetterTarget!!,
            onDismiss = { shareLetterTarget = null },
            onShareAction = {}
        )
    }

    // Direct Letter Number Jump Dialog
    if (showDirectJumpDialog) {
        Dialog(onDismissRequest = { showDirectJumpDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MidnightNavyDark)
                    .border(1.dp, MutedGoldBright, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "চিঠি নং লিখুন",
                            color = MutedGoldBright,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        IconButton(onClick = { showDirectJumpDialog = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = SoftOffWhite)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "উদাহরণ: #চিঠি_নং_00461 বা 00461",
                        color = SoftOffWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = directJumpInput,
                        onValueChange = { directJumpInput = it },
                        placeholder = { Text("#চিঠি_নং_00461", color = SoftOffWhite.copy(alpha = 0.4f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("direct_letter_number_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MutedGoldBright,
                            unfocusedBorderColor = MutedGold.copy(alpha = 0.4f),
                            cursorColor = MutedGoldBright
                        ),
                        textStyle = TextStyle(color = SoftOffWhite, fontSize = 14.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val found = viewModel.findLetterByNumber(directJumpInput.trim())
                                if (found != null) {
                                    showDirectJumpDialog = false
                                    onNavigateToLetterDetail(found)
                                } else {
                                    Toast.makeText(context, "এই নম্বরের কোনো চিঠি পাওয়া যায়নি", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MutedGoldBright, contentColor = MidnightNavyDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text(text = "চিঠি খুলুন →", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("public_letter_room_screen")
    ) {
        // Room Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightNavy)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📖 চিঠিঘর",
                            color = SoftOffWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        WaxSealBadge(sealType = "crimson_wax", size = 20.dp)
                    }
                    Text(
                        text = "হাজারো হৃদয়ের না-বলা কথার মুক্ত আঙিনা",
                        color = MutedGoldBright,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )
                }

                // Direct jump button (#চিঠি নং)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MutedGold.copy(alpha = 0.15f))
                        .border(1.dp, MutedGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { showDirectJumpDialog = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("jump_to_number_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Pin, contentDescription = null, tint = MutedGoldBright, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "চিঠি নং", color = MutedGoldBright, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Search Bar (Search by letter number, title, words, author)
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("চিঠি নম্বর, বিষয়, অনুভূতি বা শব্দ খুঁজুন...", color = SoftOffWhite.copy(alpha = 0.4f), fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "অনুসন্ধান", tint = MutedGoldBright) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "পরিষ্কার", tint = SoftOffWhite.copy(alpha = 0.6f))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("search_letter_field"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedGoldBright,
                    unfocusedBorderColor = MutedGold.copy(alpha = 0.25f),
                    focusedContainerColor = MidnightNavySurface,
                    unfocusedContainerColor = MidnightNavySurface,
                    cursorColor = MutedGoldBright
                ),
                textStyle = TextStyle(color = SoftOffWhite, fontSize = 13.sp)
            )
        }

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) MutedGoldBright else MidnightNavySurface)
                        .border(
                            1.dp,
                            if (isSelected) MutedGoldBright else MutedGold.copy(alpha = 0.25f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.setSelectedCategory(cat) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("category_chip_$cat")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) MidnightNavyDark else SoftOffWhite,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Public Letters List
        if (publicLetters.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "“আজ চিঠিঘরের উঠোনটা একটু শান্ত।”",
                        color = SoftOffWhite,
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "কোনো চিঠি পাওয়া যায়নি। আপনার না-বলা কথাটি লিখে দিন।",
                        color = MutedGold.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(publicLetters, key = { it.id }) { letter ->
                    EnvelopeCard(
                        letter = letter,
                        onClick = { onNavigateToLetterDetail(letter) },
                        onToggleSave = { viewModel.toggleSave(letter) },
                        onToggleLike = { viewModel.toggleLike(letter) },
                        onShare = { shareLetterTarget = letter },
                        onReply = {
                            viewModel.startWriting(mode = "general", replyTo = letter)
                        }
                    )
                }
            }
        }
    }
}
