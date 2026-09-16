package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import kotlinx.coroutines.delay

@Composable
fun RandomLetterPickerModal(
    availableLetters: List<LetterEntity>,
    onDismiss: () -> Unit,
    onLetterOpened: (LetterEntity) -> Unit
) {
    var isSelecting by remember { mutableStateOf(true) }
    var selectedLetter by remember { mutableStateOf<LetterEntity?>(null) }

    val scaleAnim = remember { Animatable(0.7f) }
    val rotationAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1.05f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        scaleAnim.animateTo(1.0f, animationSpec = tween(300))
        delay(1200)

        // Select random letter
        if (availableLetters.isNotEmpty()) {
            selectedLetter = availableLetters.random()
        }
        isSelecting = false
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MidnightNavyDark)
                .border(1.5.dp, MutedGold.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(24.dp)
                .testTag("random_letter_modal")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎲 হঠাৎ একটি চিঠি",
                            color = MutedGoldBright,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = SoftOffWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isSelecting) {
                    // Selecting state: animated stack of envelopes
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(scaleAnim.value),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background envelopes in the pile
                        Box(
                            modifier = Modifier
                                .size(110.dp, 76.dp)
                                .rotate(-12f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF26334D))
                                .border(1.dp, MutedGold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .size(110.dp, 76.dp)
                                .rotate(9f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF334466))
                                .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        )
                        // Active envelope being picked
                        Box(
                            modifier = Modifier
                                .size(120.dp, 84.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFFF9F5EA), Color(0xFFEAD8B8))
                                    )
                                )
                                .border(1.5.dp, MutedGoldBright, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            WaxSealBadge(size = 32.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ডাকবাক্স থেকে একটি চিঠি তোলা হচ্ছে...",
                        color = MutedGoldBright,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "হাজারো না-বলা কথার ভিড় থেকে...",
                        color = SoftOffWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Letter Revealed
                    val letter = selectedLetter
                    if (letter != null) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(MutedGold.copy(alpha = 0.15f))
                                .border(1.5.dp, MutedGoldBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = "চিঠি খুলেছে",
                                tint = MutedGoldBright,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = letter.letterNumber,
                            color = MutedGoldBright,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“${letter.title}”",
                            color = SoftOffWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "«${letter.content.take(120)}...»",
                            color = SoftOffWhite.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val senderText = if (letter.isAnonymous) "— ${letter.anonymousAlias}" else "— ${letter.senderName}"
                        Text(
                            text = senderText,
                            color = MutedGold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                onDismiss()
                                onLetterOpened(letter)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MutedGoldBright,
                                contentColor = MidnightNavy
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("open_revealed_letter_button")
                        ) {
                            Text(
                                text = "চিঠিটা পড়ুন →",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Text(
                            text = "আজ চিঠিঘরের উঠোনটা একটু শান্ত। কোনো চিঠি পাওয়া যায়নি।",
                            color = SoftOffWhite,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
