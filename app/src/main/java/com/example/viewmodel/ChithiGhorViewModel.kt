package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChithiGhorDatabase
import com.example.data.local.LetterEntity
import com.example.data.local.ReportEntity
import com.example.data.repository.LetterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChithiGhorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LetterRepository
    init {
        val database = ChithiGhorDatabase.getDatabase(application, viewModelScope)
        repository = LetterRepository(database.letterDao())
    }

    // App Navigation / Flow state
    private val _hasSeenOnboarding = MutableStateFlow(false)
    val hasSeenOnboarding = _hasSeenOnboarding.asStateFlow()

    private val _currentScreen = MutableStateFlow("home") // "home", "write", "mailbox", "letters", "profile", "detail"
    val currentScreen = _currentScreen.asStateFlow()

    private val _selectedLetter = MutableStateFlow<LetterEntity?>(null)
    val selectedLetter = _selectedLetter.asStateFlow()

    // Public room filtering & searching
    private val _selectedCategory = MutableStateFlow("সব")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Mailbox active tab
    private val _mailboxTab = MutableStateFlow("inbox") // "inbox", "sent", "drafts", "saved", "archived", "scheduled"
    val mailboxTab = _mailboxTab.asStateFlow()

    // Sound effect setting (default OFF as per prompt)
    private val _isSoundEnabled = MutableStateFlow(false)
    val isSoundEnabled = _isSoundEnabled.asStateFlow()

    // User Alias
    private val _userAlias = MutableStateFlow("একলা পথিক")
    val userAlias = _userAlias.asStateFlow()

    // Admin Mode toggle
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode = _isAdminMode.asStateFlow()

    // Replying context
    private val _replyingToLetter = MutableStateFlow<LetterEntity?>(null)
    val replyingToLetter = _replyingToLetter.asStateFlow()

    // Write initial type
    private val _writeMode = MutableStateFlow("general") // "general", "flying", "self", "scheduled"
    val writeMode = _writeMode.asStateFlow()

    // Public letters stream with category & search filtering
    val publicLetters: StateFlow<List<LetterEntity>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query ->
        Pair(category, query)
    }.flatMapLatest { (category, query) ->
        if (query.isNotBlank()) {
            repository.searchPublicLetters(query)
        } else {
            repository.getPublicLettersByCategory(category)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inboxLetters: StateFlow<List<LetterEntity>> = repository.inboxLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sentLetters: StateFlow<List<LetterEntity>> = repository.sentLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val draftLetters: StateFlow<List<LetterEntity>> = repository.draftLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedLetters: StateFlow<List<LetterEntity>> = repository.savedLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archivedLetters: StateFlow<List<LetterEntity>> = repository.archivedLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scheduledLetters: StateFlow<List<LetterEntity>> = repository.scheduledLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lettersToSelf: StateFlow<List<LetterEntity>> = repository.lettersToSelf
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flyingLetters: StateFlow<List<LetterEntity>> = repository.flyingLetters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalLettersCount: StateFlow<Int> = repository.totalCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Methods
    fun completeOnboarding() {
        _hasSeenOnboarding.value = true
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun openLetterDetail(letter: LetterEntity) {
        _selectedLetter.value = letter
        _currentScreen.value = "detail"
    }

    fun closeLetterDetail() {
        _selectedLetter.value = null
        _currentScreen.value = "letters"
    }

    fun startWriting(mode: String = "general", replyTo: LetterEntity? = null) {
        _writeMode.value = mode
        _replyingToLetter.value = replyTo
        _currentScreen.value = "write"
    }

    fun clearReply() {
        _replyingToLetter.value = null
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setMailboxTab(tab: String) {
        _mailboxTab.value = tab
    }

    fun toggleSound() {
        _isSoundEnabled.value = !_isSoundEnabled.value
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    fun setUserAlias(alias: String) {
        if (alias.isNotBlank()) {
            _userAlias.value = alias.trim()
        }
    }

    fun toggleSave(letter: LetterEntity) {
        viewModelScope.launch {
            repository.toggleSave(letter)
            if (_selectedLetter.value?.id == letter.id) {
                _selectedLetter.value = _selectedLetter.value?.copy(isSaved = !letter.isSaved)
            }
        }
    }

    fun toggleLike(letter: LetterEntity) {
        viewModelScope.launch {
            repository.toggleLike(letter)
            if (_selectedLetter.value?.id == letter.id) {
                val newHasLiked = !letter.hasLiked
                val newCount = if (newHasLiked) letter.likesCount + 1 else (letter.likesCount - 1).coerceAtLeast(0)
                _selectedLetter.value = _selectedLetter.value?.copy(hasLiked = newHasLiked, likesCount = newCount)
            }
        }
    }

    fun toggleArchive(letter: LetterEntity) {
        viewModelScope.launch {
            repository.toggleArchive(letter)
            if (_selectedLetter.value?.id == letter.id) {
                _selectedLetter.value = _selectedLetter.value?.copy(isArchived = !letter.isArchived)
            }
        }
    }

    fun deleteLetter(letter: LetterEntity) {
        viewModelScope.launch {
            repository.deleteLetter(letter)
            if (_selectedLetter.value?.id == letter.id) {
                _selectedLetter.value = null
                _currentScreen.value = "mailbox"
            }
        }
    }

    fun sendLetter(
        title: String,
        content: String,
        recipient: String,
        isAnonymous: Boolean,
        anonymousAlias: String,
        category: String,
        paperStyle: String,
        sealType: String,
        isFlyingLetter: Boolean,
        isToSelf: Boolean,
        selfTargetType: String,
        isScheduled: Boolean,
        scheduledOpenDate: Long,
        replyTo: LetterEntity? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val letterNumber = repository.generateNextLetterNumber()
            val sender = if (isAnonymous) anonymousAlias else _userAlias.value

            val entity = LetterEntity(
                letterNumber = letterNumber,
                title = if (title.isBlank()) "একটি চিঠি" else title,
                content = content,
                recipient = recipient,
                senderName = sender,
                isAnonymous = isAnonymous,
                anonymousAlias = anonymousAlias,
                category = category,
                paperStyle = paperStyle,
                sealType = sealType,
                isFlyingLetter = isFlyingLetter,
                isToSelf = isToSelf,
                selfTargetType = selfTargetType,
                isScheduled = isScheduled,
                scheduledOpenDate = scheduledOpenDate,
                isSealed = isScheduled,
                isDraft = false,
                isSentByMe = true,
                isPublic = !isToSelf && !isScheduled,
                replyToId = replyTo?.id,
                replyToLetterNumber = replyTo?.letterNumber,
                createdAt = System.currentTimeMillis()
            )

            repository.insertLetter(entity)
            _replyingToLetter.value = null
            onSuccess()
        }
    }

    fun saveDraft(
        title: String,
        content: String,
        recipient: String,
        category: String,
        paperStyle: String,
        sealType: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val letterNumber = repository.generateNextLetterNumber()
            val entity = LetterEntity(
                letterNumber = letterNumber,
                title = if (title.isBlank()) "অসমাপ্ত চিঠি" else title,
                content = content,
                recipient = recipient,
                senderName = _userAlias.value,
                category = category,
                paperStyle = paperStyle,
                sealType = sealType,
                isDraft = true,
                isSentByMe = true,
                isPublic = false,
                createdAt = System.currentTimeMillis()
            )
            repository.insertLetter(entity)
            onSuccess()
        }
    }

    fun submitReport(letter: LetterEntity, reason: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.submitReport(letter, reason)
            onDone()
        }
    }

    fun moderateHideLetter(letterId: Long) {
        viewModelScope.launch {
            repository.hideLetterByModeration(letterId)
        }
    }

    suspend fun findLetterByNumber(number: String): LetterEntity? {
        val formatted = if (number.startsWith("#")) number else "#$number"
        return repository.getLetterByNumber(formatted)
    }

    fun shareLetterText(context: Context, letter: LetterEntity) {
        val shareText = buildString {
            appendLine("চিঠি ঘর 📮")
            appendLine(letter.letterNumber)
            appendLine("“${letter.title}”")
            appendLine("────────────")
            appendLine(letter.content)
            appendLine("────────────")
            appendLine(if (letter.isAnonymous) "— ${letter.anonymousAlias}" else "— ${letter.senderName}")
            appendLine("\nকিছু কথা উড়ে বেড়াক হৃদয় থেকে হৃদয়ে।")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, letter.title)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "চিঠি শেয়ার করুন")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
