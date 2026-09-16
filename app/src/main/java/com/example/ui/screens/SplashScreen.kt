package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.WaxSealBadge
import com.example.ui.theme.DeepEmeraldDark
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.MutedGoldPale
import com.example.ui.theme.SoftOffWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val letterOffsetY = remember { Animatable(80f) }
    val letterAlpha = remember { Animatable(0f) }
    val letterScale = remember { Animatable(0.8f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Step 1: Letter appears and animates upwards into the mailbox/doorway slot
        letterAlpha.animateTo(1f, tween(600))
        letterOffsetY.animateTo(0f, tween(900, easing = FastOutSlowInEasing))
        letterScale.animateTo(1.0f, tween(600))
        textAlpha.animateTo(1f, tween(800))
        delay(1400)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MidnightNavyDark, MidnightNavy, DeepEmeraldDark)
                )
            )
            .clickable { onSplashFinished() }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Doorway / Postbox silhouette container
            Box(
                modifier = Modifier
                    .size(140.dp, 170.dp)
                    .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF131D33))
                    .border(
                        2.dp,
                        Brush.verticalGradient(listOf(MutedGoldBright, MutedGold.copy(alpha = 0.4f))),
                        RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Mailbox Slot
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 42.dp)
                        .size(width = 80.dp, height = 10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF090D17))
                        .border(1.dp, MutedGold.copy(alpha = 0.5f), RoundedCornerShape(5.dp))
                )

                // Animated folded letter entering the doorway / slot
                Box(
                    modifier = Modifier
                        .offset(y = letterOffsetY.value.dp)
                        .alpha(letterAlpha.value)
                        .scale(letterScale.value)
                        .size(width = 72.dp, height = 48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFFAF6EB), Color(0xFFEADBBE))
                            )
                        )
                        .border(1.dp, MutedGoldBright, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    WaxSealBadge(sealType = "crimson_wax", size = 20.dp)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Exact brand name: চিঠি ঘর
            Text(
                text = "চিঠি ঘর",
                color = SoftOffWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Primary tagline
            Text(
                text = "“কিছু কথা উড়ে বেড়াক হৃদয় থেকে হৃদয়ে।”",
                color = MutedGoldBright,
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}
