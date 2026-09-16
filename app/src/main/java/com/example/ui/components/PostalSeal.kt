package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.MutedGoldPale

@Composable
fun WaxSealBadge(
    sealType: String = "crimson_wax",
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    val (bgColor, accentColor) = when (sealType) {
        "golden_lotus" -> Pair(MutedGold, MutedGoldPale)
        "emerald_seal" -> Pair(DeepEmerald, Color(0xFF8CE3C3))
        else -> Pair(CrimsonWaxSeal, Color(0xFFFFD4D4))
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.5.dp, accentColor.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Inner concentric wax ring
        Box(
            modifier = Modifier
                .size(size * 0.72f)
                .clip(CircleShape)
                .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when (sealType) {
                "golden_lotus" -> {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "সিলমোহর",
                        tint = accentColor,
                        modifier = Modifier.size(size * 0.45f)
                    )
                }
                "emerald_seal" -> {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "সিলমোহর",
                        tint = accentColor,
                        modifier = Modifier.size(size * 0.42f)
                    )
                }
                else -> {
                    Text(
                        text = "চি",
                        color = accentColor,
                        fontSize = (size.value * 0.38f).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}

@Composable
fun PostalPostmarkStamp(
    modifier: Modifier = Modifier,
    dateText: String = "চিঠি ঘর ডাক",
    serialText: String = "ডাকটিকিট"
) {
    Row(
        modifier = modifier
            .rotate(-3f)
            .border(1.dp, MutedGold.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
            .background(MutedGold.copy(alpha = 0.08f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = dateText,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = MutedGoldBright,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = serialText,
                fontSize = 8.sp,
                color = MutedGold.copy(alpha = 0.8f)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        // Wavy postmark cancellation lines
        Canvas(modifier = Modifier.size(width = 24.dp, height = 16.dp)) {
            val wavePath1 = Path().apply {
                moveTo(0f, size.height * 0.25f)
                cubicTo(size.width * 0.3f, 0f, size.width * 0.7f, size.height * 0.5f, size.width, size.height * 0.25f)
            }
            val wavePath2 = Path().apply {
                moveTo(0f, size.height * 0.75f)
                cubicTo(size.width * 0.3f, size.height * 0.5f, size.width * 0.7f, size.height, size.width, size.height * 0.75f)
            }
            drawPath(wavePath1, color = MutedGold.copy(alpha = 0.6f), style = Stroke(width = 1.2f))
            drawPath(wavePath2, color = MutedGold.copy(alpha = 0.6f), style = Stroke(width = 1.2f))
        }
    }
}
