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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.LetterEntity
import com.example.ui.theme.CrimsonWaxSeal
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MutedGold
import com.example.ui.theme.MutedGoldBright
import com.example.ui.theme.SoftOffWhite

@Composable
fun ReportLetterDialog(
    letter: LetterEntity,
    onDismiss: () -> Unit,
    onSubmitReport: (String) -> Unit
) {
    val reportReasons = listOf(
        "ব্যক্তিগত ফোন/ঠিকানা বা গোপন তথ্য প্রকাশ",
        "হয়রানি বা হুমকি প্রদান",
        "অশালীন বা আক্রমণাত্মক ভাষা",
        "স্প্যাম বা অযাচিত বিজ্ঞাপন",
        "অন্যের পরিচয় নকল করা (Impersonation)"
    )

    var selectedReason by remember { mutableStateOf(reportReasons[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MidnightNavyDark)
                .border(1.dp, CrimsonWaxSeal.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "রিপোর্ট",
                            tint = CrimsonWaxSeal,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "চিঠি রিপোর্ট করুন",
                            color = SoftOffWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${letter.letterNumber} — “${letter.title}”",
                    color = MutedGoldBright,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "রিপোর্টের কারণ নির্বাচন করুন:",
                    color = SoftOffWhite.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                reportReasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = reason }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedReason == reason),
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CrimsonWaxSeal,
                                unselectedColor = SoftOffWhite.copy(alpha = 0.5f)
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = reason,
                            color = SoftOffWhite,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onSubmitReport(selectedReason)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonWaxSeal,
                        contentColor = SoftOffWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("submit_report_button")
                ) {
                    Text(
                        text = "রিপোর্ট জমা দিন",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
