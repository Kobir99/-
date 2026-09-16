package com.example.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.LetterEntity
import com.example.ui.components.EnvelopeCard
import com.example.ui.components.WaxSealBadge
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

@Composable
fun ProfileScreen(
    viewModel: ChithiGhorViewModel,
    onNavigateToLetterDetail: (LetterEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userAlias by viewModel.userAlias.collectAsStateWithLifecycle()
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsStateWithLifecycle()
    val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()

    val sentLetters by viewModel.sentLetters.collectAsStateWithLifecycle()
    val inboxLetters by viewModel.inboxLetters.collectAsStateWithLifecycle()
    val savedLetters by viewModel.savedLetters.collectAsStateWithLifecycle()
    val lettersToSelf by viewModel.lettersToSelf.collectAsStateWithLifecycle()
    val reports by viewModel.allReports.collectAsStateWithLifecycle()

    var showEditAliasDialog by remember { mutableStateOf(false) }
    var aliasDraft by remember { mutableStateOf(userAlias) }

    if (showEditAliasDialog) {
        AlertDialog(
            onDismissRequest = { showEditAliasDialog = false },
            title = {
                Text(
                    text = "ছদ্মনাম পরিবর্তন করুন",
                    color = SoftOffWhite,
                    fontFamily = FontFamily.Serif
                )
            },
            text = {
                Column {
                    Text(
                        text = "এই নামটি আপনার উন্মুক্ত চিঠির নিচে প্রদর্শিত হবে:",
                        color = SoftOffWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = aliasDraft,
                        onValueChange = { aliasDraft = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MutedGoldBright,
                            unfocusedBorderColor = MutedGold.copy(alpha = 0.3f),
                            cursorColor = MutedGoldBright
                        ),
                        textStyle = TextStyle(color = SoftOffWhite)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setUserAlias(aliasDraft)
                        showEditAliasDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MutedGoldBright, contentColor = MidnightNavyDark)
                ) {
                    Text("সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditAliasDialog = false }) {
                    Text("বাতিল", color = SoftOffWhite)
                }
            },
            containerColor = MidnightNavyCard
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightNavyDark)
            .padding(horizontal = 20.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
    ) {
        // Literary Profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MidnightNavyCard)
                    .border(1.dp, MutedGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(MidnightNavyDark)
                            .border(2.dp, MutedGoldBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        WaxSealBadge(sealType = "golden_lotus", size = 48.dp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = userAlias,
                            color = SoftOffWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(onClick = {
                            aliasDraft = userAlias
                            showEditAliasDialog = true
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "ছদ্মনাম সম্পাদনা",
                                tint = MutedGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "চিঠিঘরের সম্মানিত সদস্য",
                        color = MutedGoldBright,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Literary Stat Row (no public ranking, just personal record)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${sentLetters.size}", color = SoftOffWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "লেখা চিঠি", color = SoftOffWhite.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(MutedGold.copy(alpha = 0.3f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${inboxLetters.size}", color = SoftOffWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "পাওয়া চিঠি", color = SoftOffWhite.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(MutedGold.copy(alpha = 0.3f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${savedLetters.size}", color = SoftOffWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "সংরক্ষিত", color = SoftOffWhite.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Section: নিজের কাছে লেখা চিঠি (Letters to Self)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "📝 নিজের কাছে লেখা চিঠি",
                color = SoftOffWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (lettersToSelf.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightNavySurface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "নিজেকে কোনো চিঠি লেখেননি। ছোটবেলার বা ভবিষ্যতের নিজেকে কিছু বলতে চিঠি লিখুন।",
                        color = SoftOffWhite.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lettersToSelf.forEach { letter ->
                        EnvelopeCard(
                            letter = letter,
                            onClick = { onNavigateToLetterDetail(letter) },
                            onToggleSave = { viewModel.toggleSave(letter) },
                            onToggleLike = { viewModel.toggleLike(letter) },
                            onShare = {}
                        )
                    }
                }
            }
        }

        // Settings & Features
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "অ্যাপ সেটিংস ও গোপনীয়তা",
                color = SoftOffWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightNavySurface)
                    .padding(14.dp)
            ) {
                // Sound Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = MutedGoldBright, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "চিঠির শব্দ (Envelope Sound)", color = SoftOffWhite, fontSize = 13.sp)
                            Text(text = "চিঠি খোলা ও সিল করার মৃদু শব্দ", color = SoftOffWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = isSoundEnabled,
                        onCheckedChange = { viewModel.toggleSound() },
                        colors = SwitchDefaults.colors(checkedThumbColor = MutedGoldBright, checkedTrackColor = MidnightNavyCard)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Export Letters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val exportText = buildString {
                                appendLine("চিঠি ঘর - আমার চিঠির সংকলন")
                                appendLine("============================")
                                sentLetters.forEach { l ->
                                    appendLine("\n${l.letterNumber} - ${l.title}")
                                    appendLine("প্রাপক: ${l.recipient}")
                                    appendLine(l.content)
                                    appendLine("----------------------------")
                                }
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, exportText)
                                putExtra(Intent.EXTRA_SUBJECT, "চিঠি ঘর ব্যাকআপ")
                            }
                            context.startActivity(Intent.createChooser(intent, "চিঠি রপ্তানি করুন"))
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = MutedGoldBright, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "চিঠিগুলো টেক্সট ফাইলে রপ্তানি করুন", color = SoftOffWhite, fontSize = 13.sp)
                            Text(text = "আপনার সব চিঠি ডিভাইসে সেভ থাকবে", color = SoftOffWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Admin Moderation Toggle (Review reported letters)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = CrimsonWaxSeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "মডারেশন প্যানেল (সুরক্ষা)", color = SoftOffWhite, fontSize = 13.sp)
                            Text(text = "রিপোর্ট ও ক্ষতিকর কন্টেন্ট পর্যবেক্ষণ", color = SoftOffWhite.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                    }
                    Switch(
                        checked = isAdminMode,
                        onCheckedChange = { viewModel.toggleAdminMode() },
                        colors = SwitchDefaults.colors(checkedThumbColor = CrimsonWaxSeal, checkedTrackColor = MidnightNavyCard)
                    )
                }
            }
        }

        // Admin Moderation Queue (Shown when admin mode is toggled)
        if (isAdminMode) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightNavyCard)
                        .border(1.dp, CrimsonWaxSeal.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = CrimsonWaxSeal)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "চিঠি ঘর মডারেশন কিউ (${reports.size})",
                            color = SoftOffWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (reports.isEmpty()) {
                        Text(
                            text = "কোনো অমীমাংসিত রিপোর্ট নেই। চিঠি ঘরের পরিবেশ পরিচ্ছন্ন ও নিরাপদ।",
                            color = SoftOffWhite.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    } else {
                        reports.forEach { report ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MidnightNavyDark)
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "${report.letterNumber} — অভিযোগ #${report.id}",
                                        color = MutedGoldBright,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "কারণ: ${report.reason}",
                                        color = CrimsonWaxSeal,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Button(
                                        onClick = {
                                            viewModel.moderateHideLetter(report.letterId)
                                            Toast.makeText(context, "চিঠিটি মডারেশন দ্বারা লুকানো হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonWaxSeal),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("চিঠি লুকান (Hide)", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
