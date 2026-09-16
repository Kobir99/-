package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.WaxSealBadge
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.DeepEmeraldDark
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite

data class OnboardingStep(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val sealType: String
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val steps = listOf(
        OnboardingStep(
            title = "স্বাগতম চিঠি ঘরে।",
            subtitle = "“এখানে কথারা চিঠি হয়ে থাকে।”",
            icon = Icons.Default.Drafts,
            sealType = "crimson_wax"
        ),
        OnboardingStep(
            title = "কাউকে কিছু বলতে পারছো না?",
            subtitle = "“লিখে ফেলো।”",
            icon = Icons.Default.EditNote,
            sealType = "golden_lotus"
        ),
        OnboardingStep(
            title = "পরিচয় গোপন রাখতে চাও?",
            subtitle = "“চিঠি যাবে, পরিচয় নয়।”",
            icon = Icons.Default.Lock,
            sealType = "emerald_seal"
        ),
        OnboardingStep(
            title = "কাউকে নয়, শুধু পৃথিবীকে কিছু বলতে চাও?",
            subtitle = "“উড়ো চিঠি লিখে দাও।”",
            icon = Icons.Default.NearMe,
            sealType = "crimson_wax"
        )
    )

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val currentStep = steps[currentStepIndex]
    val isLast = currentStepIndex == steps.size - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(MidnightNavyDark, MidnightNavy, DeepEmeraldDark)
                )
            )
            .padding(24.dp)
            .testTag("onboarding_screen")
    ) {
        // Skip button
        if (!isLast) {
            TextButton(
                onClick = onFinished,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 28.dp)
            ) {
                Text(
                    text = "এড়িয়ে যান",
                    color = MutedGold.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon emblem with wax seal
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MidnightNavy.copy(alpha = 0.8f))
                    .border(1.5.dp, MutedGold.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = currentStep.icon,
                    contentDescription = null,
                    tint = MutedGoldBright,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            WaxSealBadge(sealType = currentStep.sealType, size = 32.dp)

            Spacer(modifier = Modifier.height(36.dp))

            // Step Title
            Text(
                text = currentStep.title,
                color = SoftOffWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step Subtitle
            Text(
                text = currentStep.subtitle,
                color = MutedGoldBright,
                fontSize = 17.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )
        }

        // Bottom section: Indicators & CTA Button
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dots indicator
            Row(horizontalArrangement = Arrangement.Center) {
                steps.indices.forEach { index ->
                    val isSelected = index == currentStepIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) MutedGoldBright else SoftOffWhite.copy(alpha = 0.3f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (isLast) {
                Button(
                    onClick = onFinished,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedGoldBright,
                        contentColor = MidnightNavy
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_enter_button")
                ) {
                    Text(
                        text = "চিঠি ঘরে প্রবেশ করুন",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            } else {
                Button(
                    onClick = { currentStepIndex++ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmerald,
                        contentColor = SoftOffWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .testTag("onboarding_next_button")
                ) {
                    Text(
                        text = "পরবর্তী",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "পরবর্তী",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
