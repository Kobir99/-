package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.WaxSealBadge
import com.example.ui.components.getPaperThemeColors
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.DeepEmerald
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MidnightNavySurface
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite
import com.example.viewmodel.ChithiGhorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WriteLetterScreen(
    viewModel: ChithiGhorViewModel,
    onLetterSent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val replyingToLetter by viewModel.replyingToLetter.collectAsStateWithLifecycle()
    val writeModeState by viewModel.writeMode.collectAsStateWithLifecycle()

    var letterType by remember(writeModeState) {
        mutableStateOf(
            when (writeModeState) {
                "flying" -> "উড়ো চিঠি"
                "self" -> "নিজের কাছে"
                "scheduled" -> "⏳ পরে খুলবে"
                else -> "সাধারণ চিঠি"
            }
        )
    }

    var recipient by remember { mutableStateOf(if (writeModeState == "flying") "পৃথিবীকে" else if (writeModeState == "self") "নিজেকে" else "কেউ একজন") }
    var subject by remember { mutableStateOf("") }
    var letterContent by remember { mutableStateOf("") }

    // Privacy / Anonymous toggle
    var isAnonymous by remember { mutableStateOf(true) }
    var anonymousAlias by remember { mutableStateOf("একজন অচেনা মানুষ") }

    // Category
    val categories = listOf(
        "না-বলা কথা", "ভালোবাসা", "বন্ধুত্ব", "পরিবার",
        "স্মৃতি", "অভিমান", "জীবন", "অপেক্ষা",
        "বিদায়", "কৃতজ্ঞতা", "নিজের সাথে কথা", "উড়ো চিঠি"
    )
    var selectedCategory by remember { mutableStateOf(if (writeModeState == "flying") "উড়ো চিঠি" else if (writeModeState == "self") "নিজের সাথে কথা" else "না-বলা কথা") }

    // Self Target
    val selfTargets = listOf("আজকের আমাকে", "ছোটবেলার আমাকে", "ভবিষ্যতের আমাকে", "যে মানুষটা হতে চাই তাকে")
    var selectedSelfTarget by remember { mutableStateOf(selfTargets[0]) }

    // Scheduled letter delay selection
    var scheduledDays by remember { mutableLongStateOf(30L) }

    // Style tools
    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }
    var isCenterAlign by remember { mutableStateOf(false) }
    var selectedPaperStyle by remember { mutableStateOf("warm_ivory") }
    var selectedSealType by remember { mutableStateOf("crimson_wax") }

    val recipientOptions = listOf("কেউ একজন", "নাম", "ডাকনাম", "Username", "নিজেকে", "পৃথিবীকে")
    val anonymousOptions = listOf("— একজন অচেনা মানুষ", "— একজন পাঠক", "— কেউ একজন", "— নামহীন")

    val paperTheme = getPaperThemeColors(selectedPaperStyle, isDarkTheme = true)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("write_letter_screen"),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "চিঠি লিখুন",
                        color = SoftOffWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "মনের কথাগুলো যত্নে খামে ভরুন",
                        color = MutedGoldBright,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif
                    )
                }

                WaxSealBadge(sealType = selectedSealType, size = 34.dp)
            }
        }

        // Replying context indicator
        if (replyingToLetter != null) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MutedGold.copy(alpha = 0.15f))
                        .border(1.dp, MutedGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "↓ উত্তর লিখছেন:",
                                color = MutedGoldBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${replyingToLetter!!.letterNumber} — “${replyingToLetter!!.title}”",
                                color = SoftOffWhite,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearReply() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "বাতিল", tint = SoftOffWhite)
                        }
                    }
                }
            }
        }

        // Letter Type Chips
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "চিঠির ধরন:",
                color = SoftOffWhite.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("সাধারণ চিঠি", "উড়ো চিঠি", "নিজের কাছে", "⏳ পরে খুলবে").forEach { type ->
                    val isSelected = letterType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) MutedGoldBright else MidnightNavySurface)
                            .border(
                                1.dp,
                                if (isSelected) MutedGoldBright else MutedGold.copy(alpha = 0.3f),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                letterType = type
                                if (type == "উড়ো চিঠি") {
                                    recipient = "পৃথিবীকে"
                                    selectedCategory = "উড়ো চিঠি"
                                } else if (type == "নিজের কাছে") {
                                    recipient = "নিজেকে"
                                    selectedCategory = "নিজের সাথে কথা"
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = type,
                            color = if (isSelected) MidnightNavyDark else SoftOffWhite,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Letter to Self Target options
        if (letterType == "নিজের কাছে") {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "কাকে চিঠি:",
                    color = SoftOffWhite.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selfTargets.forEach { target ->
                        val isSelected = selectedSelfTarget == target
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) DeepEmerald else MidnightNavySurface)
                                .border(1.dp, if (isSelected) Color(0xFF57D1A2) else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { selectedSelfTarget = target }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = target,
                                color = SoftOffWhite,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Scheduled Letter date options
        if (letterType == "⏳ পরে খুলবে") {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavySurface)
                        .border(1.dp, MutedGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = MutedGoldBright)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "কখন সিলমোহর খুলবে?",
                                color = SoftOffWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(
                                Pair("৩০ দিন পর", 30L),
                                Pair("৬ মাস পর", 180L),
                                Pair("১ বছর পর", 365L),
                                Pair("২০২৭ সালে", 500L)
                            ).forEach { (label, days) ->
                                val isSelected = scheduledDays == days
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) MutedGoldBright else MidnightNavyCard)
                                        .clickable { scheduledDays = days }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) MidnightNavyDark else SoftOffWhite,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recipient Selection & Subject
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "প্রাপক:",
                    color = SoftOffWhite.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = recipient,
                onValueChange = { recipient = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipient_input"),
                textStyle = TextStyle(color = SoftOffWhite, fontSize = 14.sp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedGoldBright,
                    unfocusedBorderColor = MutedGold.copy(alpha = 0.3f),
                    cursorColor = MutedGoldBright
                )
            )

            // Recipient Quick chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                recipientOptions.forEach { opt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MidnightNavySurface)
                            .clickable { recipient = opt }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = opt, color = MutedGold.copy(alpha = 0.8f), fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subject (Optional)
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                placeholder = { Text("চিঠির বিষয় বা শিরোনাম (ঐচ্ছিক)", color = SoftOffWhite.copy(alpha = 0.4f), fontSize = 14.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("subject_input"),
                textStyle = TextStyle(color = SoftOffWhite, fontSize = 14.sp, fontFamily = FontFamily.Serif),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedGoldBright,
                    unfocusedBorderColor = MutedGold.copy(alpha = 0.3f),
                    cursorColor = MutedGoldBright
                )
            )
        }

        // Anonymous Toggle (পরিচয় গোপন রাখুন)
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MidnightNavySurface)
                    .border(1.dp, MutedGold.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "গোপনীয়তা",
                                tint = if (isAnonymous) MutedGoldBright else SoftOffWhite.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "🔒 পরিচয় গোপন রাখুন",
                                    color = SoftOffWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "“চিঠি যাবে, পরিচয় নয়।”",
                                    color = MutedGold.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MutedGoldBright,
                                checkedTrackColor = MidnightNavyCard,
                                uncheckedThumbColor = SoftOffWhite.copy(alpha = 0.5f),
                                uncheckedTrackColor = MidnightNavyDark
                            ),
                            modifier = Modifier.testTag("anonymous_toggle")
                        )
                    }

                    if (isAnonymous) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "প্রেরকের ছদ্মনাম নির্বাচন করুন:",
                            color = SoftOffWhite.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            anonymousOptions.forEach { alias ->
                                val cleanAlias = alias.replace("— ", "")
                                val isSelected = anonymousAlias == cleanAlias
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) MutedGold.copy(alpha = 0.25f) else MidnightNavyDark)
                                        .border(1.dp, if (isSelected) MutedGoldBright else Color.Transparent, RoundedCornerShape(6.dp))
                                        .clickable { anonymousAlias = cleanAlias }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = alias,
                                        color = if (isSelected) MutedGoldBright else SoftOffWhite.copy(alpha = 0.7f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Minimal Writing Toolbar (Formatting, Paper style, Seal)
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MidnightNavySurface)
                    .border(1.dp, MutedGold.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text tools
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { isBold = !isBold },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatBold,
                            contentDescription = "Bold",
                            tint = if (isBold) MutedGoldBright else SoftOffWhite.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { isItalic = !isItalic },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatItalic,
                            contentDescription = "Italic",
                            tint = if (isItalic) MutedGoldBright else SoftOffWhite.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { isCenterAlign = !isCenterAlign },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isCenterAlign) Icons.Default.FormatAlignCenter else Icons.Default.FormatAlignLeft,
                            contentDescription = "Align",
                            tint = if (isCenterAlign) MutedGoldBright else SoftOffWhite.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Paper Style swatches
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        Pair("warm_ivory", Color(0xFFFAF7EE)),
                        Pair("kraft_parchment", Color(0xFFDECFA5)),
                        Pair("midnight_charcoal", Color(0xFF161E2E)),
                        Pair("emerald_tint", Color(0xFF0F3B2E))
                    ).forEach { (style, color) ->
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    1.5.dp,
                                    if (selectedPaperStyle == style) MutedGoldBright else Color.Gray,
                                    CircleShape
                                )
                                .clickable { selectedPaperStyle = style }
                        )
                    }
                }

                // Seal Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("crimson_wax", "golden_lotus", "emerald_seal").forEach { seal ->
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .clickable { selectedSealType = seal }
                        ) {
                            WaxSealBadge(sealType = seal, size = 22.dp)
                        }
                    }
                }
            }
        }

        // Category selection
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "বিভাগ নির্বাচন করুন:",
                color = SoftOffWhite.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) MutedGoldBright else MidnightNavySurface)
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) MidnightNavyDark else SoftOffWhite.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Large Writing Canvas (Feels like writing on authentic paper)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(paperTheme.backgroundBrush)
                    .border(1.dp, paperTheme.borderColor, RoundedCornerShape(16.dp))
                    .padding(18.dp)
                    .testTag("writing_canvas")
            ) {
                if (letterContent.isEmpty()) {
                    Text(
                        text = "«“যে কথাটা মুখে বলতে পারোনি, সেটাই লিখে ফেলো…”»",
                        color = paperTheme.subtextColor.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 26.sp
                    )
                }

                BasicTextField(
                    value = letterContent,
                    onValueChange = { letterContent = it },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = TextStyle(
                        color = paperTheme.textColor,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                        textAlign = if (isCenterAlign) TextAlign.Center else TextAlign.Start,
                        lineHeight = 26.sp
                    ),
                    cursorBrush = SolidColor(paperTheme.accentColor)
                )
            }
        }

        // Action Buttons: Save Draft & Send Letter
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Draft Button
                OutlinedButton(
                    onClick = {
                        if (letterContent.isBlank()) {
                            Toast.makeText(context, "চিঠিতে কিছু কথা লিখুন", Toast.LENGTH_SHORT).show()
                            return@OutlinedButton
                        }
                        viewModel.saveDraft(
                            title = subject,
                            content = letterContent,
                            recipient = recipient,
                            category = selectedCategory,
                            paperStyle = selectedPaperStyle,
                            sealType = selectedSealType,
                            onSuccess = {
                                Toast.makeText(context, "খসড়া সংরক্ষিত হয়েছে 📜", Toast.LENGTH_SHORT).show()
                                onLetterSent()
                            }
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("save_draft_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MutedGoldBright),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MutedGold.copy(alpha = 0.6f))
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "খসড়া সংরক্ষণ", fontSize = 13.sp)
                }

                // Send Button
                Button(
                    onClick = {
                        if (letterContent.isBlank()) {
                            Toast.makeText(context, "চিঠিতে কিছু কথা লিখুন", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val now = System.currentTimeMillis()
                        val scheduledDate = if (letterType == "⏳ পরে খুলবে") {
                            now + (scheduledDays * 86400000L)
                        } else 0L

                        viewModel.sendLetter(
                            title = subject,
                            content = letterContent,
                            recipient = recipient,
                            isAnonymous = isAnonymous,
                            anonymousAlias = anonymousAlias,
                            category = selectedCategory,
                            paperStyle = selectedPaperStyle,
                            sealType = selectedSealType,
                            isFlyingLetter = (letterType == "উড়ো চিঠি"),
                            isToSelf = (letterType == "নিজের কাছে"),
                            selfTargetType = if (letterType == "নিজের কাছে") selectedSelfTarget else "",
                            isScheduled = (letterType == "⏳ পরে খুলবে"),
                            scheduledOpenDate = scheduledDate,
                            replyTo = replyingToLetter,
                            onSuccess = {
                                val message = if (letterType == "⏳ পরে খুলবে") {
                                    "চিঠিটি সিলমোহর দিয়ে সংরক্ষিত হলো ⏳"
                                } else if (letterType == "উড়ো চিঠি") {
                                    "উড়ো চিঠিটি ডানা মেললো 🕊️"
                                } else {
                                    "চিঠি ডাকবাক্সে জমা হয়েছে 📮"
                                }
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                onLetterSent()
                            }
                        )
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("send_letter_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MutedGoldBright,
                        contentColor = MidnightNavyDark
                    )
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (letterType == "উড়ো চিঠি") "উড়িয়ে দিন 🕊️" else "চিঠি পাঠান 📮",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
