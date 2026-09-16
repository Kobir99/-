package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LetterEntity
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.DeepEmeraldLight
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite

@Composable
fun EnvelopeCard(
    letter: LetterEntity,
    onClick: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleLike: () -> Unit,
    onShare: () -> Unit,
    onReply: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = getPaperThemeColors(letter.paperStyle, isDarkTheme = true)
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape)
            .clip(shape)
            .background(theme.backgroundBrush)
            .border(1.dp, theme.borderColor, shape)
            .clickable(onClick = onClick)
            .testTag("envelope_card_${letter.id}")
            .padding(18.dp)
    ) {
        Column {
            // Header Row: Letter Number, Category, and Postal Seal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MutedGold.copy(alpha = 0.15f))
                            .border(0.8.dp, MutedGold.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = letter.category,
                            color = MutedGoldBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (letter.isFlyingLetter) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DeepEmeraldLight.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "🕊️ উড়ো চিঠি",
                                color = Color(0xFF68D391),
                                fontSize = 10.sp
                            )
                        }
                    }

                    if (letter.replyToLetterNumber != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "↓ উত্তর ${letter.replyToLetterNumber}",
                            color = theme.subtextColor,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = letter.letterNumber,
                        color = MutedGoldBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    WaxSealBadge(sealType = letter.sealType, size = 26.dp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = letter.title,
                color = theme.textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Preview excerpt
            Text(
                text = letter.content.replace("\n", " "),
                color = theme.subtextColor,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily.Serif,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom row: Sender Alias & Clean Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sender representation
                val senderDisplay = if (letter.isAnonymous) {
                    "— ${letter.anonymousAlias}"
                } else {
                    "— ${letter.senderName}"
                }
                Text(
                    text = senderDisplay,
                    color = theme.accentColor,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium
                )

                // Actions: Heart, Bookmark, Share, Reply
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Like / Heart action
                    IconButton(
                        onClick = onToggleLike,
                        modifier = Modifier.size(36.dp).testTag("like_button_${letter.id}")
                    ) {
                        Icon(
                            imageVector = if (letter.hasLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "ভালো লেগেছে",
                            tint = if (letter.hasLiked) CrimsonWaxSeal else theme.subtextColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Bookmark / Save
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(36.dp).testTag("save_button_${letter.id}")
                    ) {
                        Icon(
                            imageVector = if (letter.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "সংরক্ষণ",
                            tint = if (letter.isSaved) MutedGoldBright else theme.subtextColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Share
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(36.dp).testTag("share_button_${letter.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = theme.subtextColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Reply via letter
                    if (onReply != null) {
                        IconButton(
                            onClick = onReply,
                            modifier = Modifier.size(36.dp).testTag("reply_button_${letter.id}")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Reply,
                                contentDescription = "চিঠির উত্তর দিন",
                                tint = MutedGoldBright,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
