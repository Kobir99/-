package com.example.data.repository

import com.example.data.local.LetterDao
import com.example.data.local.LetterEntity
import com.example.data.local.ReportEntity
import kotlinx.coroutines.flow.Flow

class LetterRepository(private val letterDao: LetterDao) {

    val publicLetters: Flow<List<LetterEntity>> = letterDao.getPublicLetters()
    val inboxLetters: Flow<List<LetterEntity>> = letterDao.getInboxLetters()
    val sentLetters: Flow<List<LetterEntity>> = letterDao.getSentLetters()
    val draftLetters: Flow<List<LetterEntity>> = letterDao.getDraftLetters()
    val savedLetters: Flow<List<LetterEntity>> = letterDao.getSavedLetters()
    val archivedLetters: Flow<List<LetterEntity>> = letterDao.getArchivedLetters()
    val scheduledLetters: Flow<List<LetterEntity>> = letterDao.getScheduledLetters()
    val lettersToSelf: Flow<List<LetterEntity>> = letterDao.getLettersToSelf()
    val flyingLetters: Flow<List<LetterEntity>> = letterDao.getFlyingLetters()
    val allReports: Flow<List<ReportEntity>> = letterDao.getAllReports()
    val totalCount: Flow<Int> = letterDao.getTotalLettersCount()

    fun getPublicLettersByCategory(category: String): Flow<List<LetterEntity>> {
        return if (category == "সব" || category.isEmpty()) {
            letterDao.getPublicLetters()
        } else {
            letterDao.getPublicLettersByCategory(category)
        }
    }

    fun getRepliesForLetter(letterId: Long): Flow<List<LetterEntity>> {
        return letterDao.getRepliesForLetter(letterId)
    }

    fun searchPublicLetters(query: String): Flow<List<LetterEntity>> {
        return letterDao.searchPublicLetters(query)
    }

    fun searchAllLetters(query: String): Flow<List<LetterEntity>> {
        return letterDao.searchAllLetters(query)
    }

    suspend fun getLetterById(id: Long): LetterEntity? = letterDao.getLetterById(id)

    suspend fun getLetterByNumber(number: String): LetterEntity? = letterDao.getLetterByNumber(number)

    suspend fun generateNextLetterNumber(): String {
        val maxId = letterDao.getMaxLetterId() ?: 0L
        val nextNum = maxId + 1
        return String.format("#চিঠি_নং_%05d", nextNum)
    }

    suspend fun insertLetter(letter: LetterEntity): Long = letterDao.insertLetter(letter)

    suspend fun updateLetter(letter: LetterEntity) = letterDao.updateLetter(letter)

    suspend fun deleteLetter(letter: LetterEntity) = letterDao.deleteLetter(letter)

    suspend fun toggleSave(letter: LetterEntity) {
        letterDao.updateSavedStatus(letter.id, !letter.isSaved)
    }

    suspend fun toggleLike(letter: LetterEntity) {
        val newHasLiked = !letter.hasLiked
        val newCount = if (newHasLiked) letter.likesCount + 1 else (letter.likesCount - 1).coerceAtLeast(0)
        letterDao.updateLikeStatus(letter.id, newHasLiked, newCount)
    }

    suspend fun toggleArchive(letter: LetterEntity) {
        letterDao.updateArchivedStatus(letter.id, !letter.isArchived)
    }

    suspend fun submitReport(letter: LetterEntity, reason: String) {
        val report = ReportEntity(
            letterId = letter.id,
            letterNumber = letter.letterNumber,
            reason = reason
        )
        letterDao.insertReport(report)
    }

    suspend fun hideLetterByModeration(letterId: Long) {
        letterDao.hideLetterByModeration(letterId)
    }
}
