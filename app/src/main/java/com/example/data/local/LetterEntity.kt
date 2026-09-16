package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "letters")
data class LetterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val letterNumber: String, // e.g. "#চিঠি_নং_00461"
    val title: String,
    val content: String,
    val recipient: String, // "কেউ একজন", "নাম", "নিজেকে", etc.
    val senderName: String, // Public alias or custom name
    val isAnonymous: Boolean = false,
    val anonymousAlias: String = "একজন অচেনা মানুষ", // "একজন অচেনা মানুষ", "একজন পাঠক", "কেউ একজন", "নামহীন"
    val category: String = "না-বলা কথা", // ভালোবাসা, বন্ধুত্ব, পরিবার, স্মৃতি, অভিমান, না-বলা কথা, জীবন, অপেক্ষা, বিদায়, কৃতজ্ঞতা, নিজের সাথে কথা, উড়ো চিঠি
    val paperStyle: String = "warm_ivory", // "warm_ivory", "kraft_parchment", "midnight_charcoal", "emerald_tint"
    val sealType: String = "crimson_wax", // "crimson_wax", "golden_lotus", "classic_postage", "emerald_seal"
    val isFlyingLetter: Boolean = false, // উড়ো চিঠি
    val isToSelf: Boolean = false, // নিজেকে চিঠি
    val selfTargetType: String = "", // আজকের আমাকে, ছোটবেলার আমাকে, ভবিষ্যতের আমাকে, যে মানুষটা হতে চাই তাকে
    val isScheduled: Boolean = false, // ⏳ পরে খুলবে
    val scheduledOpenDate: Long = 0L,
    val isSealed: Boolean = false,
    val isDraft: Boolean = false,
    val isArchived: Boolean = false,
    val isSaved: Boolean = false,
    val isSentByMe: Boolean = false,
    val isReceivedByMe: Boolean = false,
    val isPublic: Boolean = true,
    val isFeatured: Boolean = false,
    val likesCount: Int = 0,
    val hasLiked: Boolean = false,
    val replyToId: Long? = null,
    val replyToLetterNumber: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val letterId: Long,
    val letterNumber: String,
    val reason: String,
    val reportedAt: Long = System.currentTimeMillis(),
    val status: String = "Pending" // Pending, Reviewed, Dismissed, Hidden
)
