package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.data.local.LetterEntity
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite

@Composable
fun ShareCardDialog(
    letter: LetterEntity,
    onDismiss: () -> Unit,
    onShareAction: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MidnightNavyCard)
                .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Top close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "চিঠির পোস্টকার্ড",
                        color = MutedGoldBright,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = SoftOffWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Postcard visual card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFFCF9F2), Color(0xFFF4EAD8))
                            )
                        )
                        .border(1.dp, Color(0xFFC7B38D), RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        // Postcard Header: Brand & Postal Stamp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "চিঠি ঘর",
                                    color = Color(0xFF1E2430),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = "“কিছু কথা উড়ে বেড়াক হৃদয় থেকে হৃদয়ে।”",
                                    color = Color(0xFF5E5445),
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            PostalPostmarkStamp(serialText = letter.letterNumber)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Letter Number Badge
                        Text(
                            text = letter.letterNumber,
                            color = Color(0xFF8B6727),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = letter.title,
                            color = Color(0xFF1F1A12),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Excerpt text
                        Text(
                            text = "«${letter.content.take(160)}...»",
                            color = Color(0xFF3B3325),
                            fontSize = 13.sp,
                            lineHeight = 21.sp,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Sender representation (respecting privacy!)
                        val senderLabel = if (letter.isAnonymous) {
                            "— ${letter.anonymousAlias}"
                        } else {
                            "— ${letter.senderName}"
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = senderLabel,
                                color = Color(0xFF8B6727),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Serif
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = "লিঙ্ক",
                                    tint = Color(0xFF5E5445),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "chithighor.app",
                                    color = Color(0xFF5E5445),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Share Button
                Button(
                    onClick = {
                        onDismiss()
                        onShareAction()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedGoldBright,
                        contentColor = MidnightNavy
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "শেয়ার",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "বন্ধুদের সাথে শেয়ার করুন",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
