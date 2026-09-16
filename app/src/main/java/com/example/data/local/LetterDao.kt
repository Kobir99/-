package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LetterDao {

    @Query("SELECT * FROM letters WHERE isDraft = 0 AND isArchived = 0 AND isPublic = 1 ORDER BY createdAt DESC")
    fun getPublicLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isDraft = 0 AND isArchived = 0 AND isPublic = 1 AND category = :category ORDER BY createdAt DESC")
    fun getPublicLettersByCategory(category: String): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isDraft = 0 AND isReceivedByMe = 1 AND isArchived = 0 ORDER BY createdAt DESC")
    fun getInboxLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isDraft = 0 AND isSentByMe = 1 AND isArchived = 0 ORDER BY createdAt DESC")
    fun getSentLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isDraft = 1 ORDER BY createdAt DESC")
    fun getDraftLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isSaved = 1 ORDER BY createdAt DESC")
    fun getSavedLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isArchived = 1 ORDER BY createdAt DESC")
    fun getArchivedLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isScheduled = 1 ORDER BY scheduledOpenDate ASC")
    fun getScheduledLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isToSelf = 1 ORDER BY createdAt DESC")
    fun getLettersToSelf(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isFlyingLetter = 1 AND isDraft = 0 ORDER BY createdAt DESC")
    fun getFlyingLetters(): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE id = :id LIMIT 1")
    suspend fun getLetterById(id: Long): LetterEntity?

    @Query("SELECT * FROM letters WHERE letterNumber = :letterNumber LIMIT 1")
    suspend fun getLetterByNumber(letterNumber: String): LetterEntity?

    @Query("SELECT * FROM letters WHERE replyToId = :letterId ORDER BY createdAt ASC")
    fun getRepliesForLetter(letterId: Long): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE isPublic = 1 AND isDraft = 0 AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR letterNumber LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR senderName LIKE '% ' || :query || '%')")
    fun searchPublicLetters(query: String): Flow<List<LetterEntity>>

    @Query("SELECT * FROM letters WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR letterNumber LIKE '%' || :query || '%')")
    fun searchAllLetters(query: String): Flow<List<LetterEntity>>

    @Query("SELECT COUNT(*) FROM letters WHERE isDraft = 0")
    fun getTotalLettersCount(): Flow<Int>

    @Query("SELECT MAX(id) FROM letters")
    suspend fun getMaxLetterId(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetter(letter: LetterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetters(letters: List<LetterEntity>)

    @Update
    suspend fun updateLetter(letter: LetterEntity)

    @Delete
    suspend fun deleteLetter(letter: LetterEntity)

    @Query("UPDATE letters SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: Long, isSaved: Boolean)

    @Query("UPDATE letters SET hasLiked = :hasLiked, likesCount = :likesCount WHERE id = :id")
    suspend fun updateLikeStatus(id: Long, hasLiked: Boolean, likesCount: Int)

    @Query("UPDATE letters SET isArchived = :isArchived WHERE id = :id")
    suspend fun updateArchivedStatus(id: Long, isArchived: Boolean)

    // Reports
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Query("SELECT * FROM reports ORDER BY reportedAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("UPDATE letters SET isPublic = 0 WHERE id = :letterId")
    suspend fun hideLetterByModeration(letterId: Long)
}
