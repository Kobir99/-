package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.LetterEntity
import com.example.ui.components.EnvelopeCard
import com.example.ui.components.ShareCardDialog
import com.example.ui.components.WaxSealBadge
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MidnightNavySurface
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MailboxScreen(
    viewModel: ChithiGhorViewModel,
    onNavigateToLetterDetail: (LetterEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.mailboxTab.collectAsStateWithLifecycle()

    val inboxLetters by viewModel.inboxLetters.collectAsStateWithLifecycle()
    val sentLetters by viewModel.sentLetters.collectAsStateWithLifecycle()
    val draftLetters by viewModel.draftLetters.collectAsStateWithLifecycle()
    val savedLetters by viewModel.savedLetters.collectAsStateWithLifecycle()
    val archivedLetters by viewModel.archivedLetters.collectAsStateWithLifecycle()
    val scheduledLetters by viewModel.scheduledLetters.collectAsStateWithLifecycle()

    var shareTarget by remember { mutableStateOf<LetterEntity?>(null) }

    if (shareTarget != null) {
        ShareCardDialog(
            letter = shareTarget!!,
            onDismiss = { shareTarget = null },
            onShareAction = {}
        )
    }

    val tabs = listOf(
        Pair("inbox", "পাওয়া চিঠি"),
        Pair("sent", "পাঠানো চিঠি"),
        Pair("drafts", "খসড়া"),
        Pair("saved", "সংরক্ষিত"),
        Pair("archived", "আর্কাইভ"),
        Pair("scheduled", "⏳ অপেক্ষমাণ")
    )

    val currentLetters = when (activeTab) {
        "sent" -> sentLetters
        "drafts" -> draftLetters
        "saved" -> savedLetters
        "archived" -> archivedLetters
        "scheduled" -> scheduledLetters
        else -> inboxLetters
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("mailbox_screen")
    ) {
        // Mailbox Header
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
                            text = "📮 ডাকবাক্স",
                            color = SoftOffWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        WaxSealBadge(sealType = "golden_lotus", size = 20.dp)
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "আপনার ব্যক্তিগত চিঠির নিরাপদ কোণ",
                        color = MutedGoldBright,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )
                }

                // Total mail count badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavyCard)
                        .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "মোট: ${currentLetters.size}টি",
                        color = MutedGoldBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Horizontal Scrollable Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightNavySurface)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs) { (tabKey, tabTitle) ->
                val isSelected = activeTab == tabKey
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) MutedGoldBright else MidnightNavyDark)
                        .border(
                            1.dp,
                            if (isSelected) MutedGoldBright else MutedGold.copy(alpha = 0.25f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.setMailboxTab(tabKey) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("mailbox_tab_$tabKey")
                ) {
                    Text(
                        text = tabTitle,
                        color = if (isSelected) MidnightNavyDark else SoftOffWhite,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Letter List or Empty state
        if (currentLetters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MidnightNavyCard)
                            .border(1.dp, MutedGold.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (activeTab) {
                            "drafts" -> Icons.Default.Drafts
                            "saved" -> Icons.Default.Bookmark
                            "scheduled" -> Icons.Default.HourglassEmpty
                            "archived" -> Icons.Default.Archive
                            else -> Icons.Default.MailOutline
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MutedGoldBright,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    val emptyMessage = when (activeTab) {
                        "drafts" -> "“অসমাপ্ত কথাগুলো এখানে অপেক্ষা করছে।”"
                        "saved" -> "“যে চিঠিগুলো মনে রাখতে চাও, তারা এখানে থাকবে।”"
                        "scheduled" -> "“যে চিঠিটা অপেক্ষা করছে, নির্দিষ্ট দিনে তার সিল খুলবে।”"
                        "archived" -> "“পুরনো দিনের স্মৃতিগুলো এখানে শান্তিতে রক্ষিত।”"
                        "sent" -> "“এখনও কোনো চিঠি পাঠানো হয়নি।”"
                        else -> "“আজ ডাকবাক্সটা নীরব। 📮”"
                    }

                    Text(
                        text = emptyMessage,
                        color = SoftOffWhite,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "নতুন অনুভূতি খামে ভরতে চিঠি লিখুন।",
                        color = MutedGold.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(currentLetters, key = { it.id }) { letter ->
                    if (letter.isScheduled && letter.scheduledOpenDate > System.currentTimeMillis()) {
                        // Sealed future letter card
                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val openDateStr = sdf.format(Date(letter.scheduledOpenDate))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MidnightNavyCard)
                                .border(1.dp, MutedGoldBright.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = MutedGoldBright, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = letter.letterNumber,
                                            color = MutedGoldBright,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = letter.title,
                                        color = SoftOffWhite,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "⏳ সিলমোহর খুলবে: $openDateStr",
                                        color = MutedGoldBright,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                WaxSealBadge(sealType = "crimson_wax", size = 36.dp)
                            }
                        }
                    } else {
                        EnvelopeCard(
                            letter = letter,
                            onClick = { onNavigateToLetterDetail(letter) },
                            onToggleSave = { viewModel.toggleSave(letter) },
                            onToggleLike = { viewModel.toggleLike(letter) },
                            onShare = { shareTarget = letter },
                            onReply = {
                                viewModel.startWriting(mode = "general", replyTo = letter)
                            }
                        )
                    }
                }
            }
        }
    }
}
