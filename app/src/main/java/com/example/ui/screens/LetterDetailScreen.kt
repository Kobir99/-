package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.LetterEntity
import com.example.ui.components.PostalPostmarkStamp
import com.example.ui.components.ReportLetterDialog
import com.example.ui.components.ShareCardDialog
import com.example.ui.components.WaxSealBadge
import com.example.ui.components.getPaperThemeColors
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LetterDetailScreen(
    letter: LetterEntity,
    viewModel: ChithiGhorViewModel,
    onBackClick: () -> Unit,
    onReplyClick: (LetterEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentSelectedLetter by viewModel.selectedLetter.collectAsStateWithLifecycle()
    val displayLetter = currentSelectedLetter ?: letter

    var showReportDialog by remember { mutableStateOf(false) }
    var showShareModal by remember { mutableStateOf(false) }

    val theme = getPaperThemeColors(displayLetter.paperStyle, isDarkTheme = true)

    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val dateStr = sdf.format(Date(displayLetter.createdAt))

    if (showReportDialog) {
        ReportLetterDialog(
            letter = displayLetter,
            onDismiss = { showReportDialog = false },
            onSubmitReport = { reason ->
                viewModel.submitReport(displayLetter, reason) {
                    Toast.makeText(context, "রিপোর্ট সফলভাবে জমা দেওয়া হয়েছে", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showShareModal) {
        ShareCardDialog(
            letter = displayLetter,
            onDismiss = { showShareModal = false },
            onShareAction = {
                viewModel.shareLetterText(context, displayLetter)
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .testTag("letter_detail_screen")
    ) {
        // Distraction-free top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightNavy)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ফিরে যান",
                    tint = SoftOffWhite
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = displayLetter.letterNumber,
                    color = MutedGoldBright,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = displayLetter.category,
                    color = SoftOffWhite.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showReportDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "রিপোর্ট",
                        tint = SoftOffWhite.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                WaxSealBadge(sealType = displayLetter.sealType, size = 26.dp)
            }
        }

        // Reading parchment area
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.backgroundBrush)
                        .border(1.dp, theme.borderColor, RoundedCornerShape(20.dp))
                        .padding(22.dp)
                ) {
                    Column {
                        // Letter Header with Postal cancellation stamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                if (displayLetter.isFlyingLetter) {
                                    Text(
                                        text = "🕊️ উড়ো চিঠি",
                                        color = theme.accentColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else if (displayLetter.isToSelf) {
                                    Text(
                                        text = "📝 নিজেকে চিঠি (${displayLetter.selfTargetType})",
                                        color = theme.accentColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    Text(
                                        text = "প্রাপক: ${displayLetter.recipient}",
                                        color = theme.accentColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                                Text(
                                    text = dateStr,
                                    color = theme.subtextColor,
                                    fontSize = 11.sp
                                )
                            }

                            PostalPostmarkStamp(dateText = "চিঠি ঘর ডাক", serialText = displayLetter.letterNumber)
                        }

                        // Thread relationship banner
                        if (displayLetter.replyToLetterNumber != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(theme.subtextColor.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "↓ এই চিঠিটি ${displayLetter.replyToLetterNumber}-এর উত্তর হিসেবে লেখা হয়েছে",
                                    color = theme.subtextColor,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Title
                        Text(
                            text = displayLetter.title,
                            color = theme.textColor,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Letter Content (Rich literary serif formatting & line height)
                        Text(
                            text = displayLetter.content,
                            color = theme.textColor,
                            fontSize = 16.sp,
                            lineHeight = 28.sp,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // Sender Signature & Seal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            WaxSealBadge(sealType = displayLetter.sealType, size = 36.dp)

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "ইতি,",
                                    color = theme.subtextColor,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Serif
                                )
                                val senderText = if (displayLetter.isAnonymous) {
                                    "— ${displayLetter.anonymousAlias}"
                                } else {
                                    "— ${displayLetter.senderName}"
                                }
                                Text(
                                    text = senderText,
                                    color = theme.accentColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                }
            }

            // Resonance Section: “চিঠিটা তোমার মনে রইলো?”
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightNavyCard)
                        .border(1.dp, MutedGold.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "“চিঠিটা তোমার মনে রইলো?”",
                        color = MutedGoldBright,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Like
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { viewModel.toggleLike(displayLetter) },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (displayLetter.hasLiked) CrimsonWaxSeal.copy(alpha = 0.2f) else MidnightNavyDark)
                            ) {
                                Icon(
                                    imageVector = if (displayLetter.hasLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "ভালো লেগেছে",
                                    tint = if (displayLetter.hasLiked) CrimsonWaxSeal else SoftOffWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ভালো লেগেছে",
                                color = SoftOffWhite.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }

                        // Bookmark / Save
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { viewModel.toggleSave(displayLetter) },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (displayLetter.isSaved) MutedGold.copy(alpha = 0.2f) else MidnightNavyDark)
                            ) {
                                Icon(
                                    imageVector = if (displayLetter.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "রেখে দিন",
                                    tint = if (displayLetter.isSaved) MutedGoldBright else SoftOffWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রেখে দিন",
                                color = SoftOffWhite.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }

                        // Share
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { showShareModal = true },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(MidnightNavyDark)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "শেয়ার",
                                    tint = SoftOffWhite
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "শেয়ার",
                                color = SoftOffWhite.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Signature Feature 16: “এই চিঠির উত্তরে একটি চিঠি লিখুন।” (Reply Through Letter)
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = { onReplyClick(displayLetter) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald,
                        contentColor = SoftOffWhite
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MutedGold.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("reply_through_letter_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Reply,
                        contentDescription = "উত্তর",
                        tint = MutedGoldBright,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "এই চিঠির উত্তরে একটি চিঠি লিখুন",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "চিঠিটির সাথে আপনার উত্তর একটি ধারাবাহিক সুতোয় বাঁধা থাকবে।",
                    color = SoftOffWhite.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
