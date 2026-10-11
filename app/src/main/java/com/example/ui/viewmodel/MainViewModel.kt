package com.example.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Announcement
import com.example.data.model.ChatMessage
import com.example.data.model.DebugLogEntry
import com.example.data.model.Hadith
import com.example.data.model.HangoutInvitation
import com.example.data.model.NearbyUser
import com.example.data.model.RoleVerificationRequest
import com.example.data.model.SharedTask
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.repository.AppRepository
import com.example.data.repository.HadithRepository
import com.example.ui.theme.AppThemePalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val hadithRepo = HadithRepository(database)
    val appRepo = AppRepository(application, database)

    // Current navigation state
    // 0: Slide Hadis, 1: Slide Chat (42 Blok), 2: Slide Komunitas/Sekitar, 3: Slide Dasbor Analitik, 4: Slide Profil & Pengaturan
    private val _currentSlideIndex = MutableStateFlow(0)
    val currentSlideIndex: StateFlow<Int> = _currentSlideIndex.asStateFlow()

    // Sub-screen navigation states (with BackHandler support)
    private val _selectedHadithDetail = MutableStateFlow<Hadith?>(null)
    val selectedHadithDetail: StateFlow<Hadith?> = _selectedHadithDetail.asStateFlow()

    private val _activeChatRoomId = MutableStateFlow<String?>(null)
    val activeChatRoomId: StateFlow<String?> = _activeChatRoomId.asStateFlow()

    // Hadith filtering & search
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Semua")
    val onlyFavorites = MutableStateFlow(false)

    // Language Preferences
    val appLanguage = MutableStateFlow(com.example.data.model.AppLanguage.INDONESIA)
    val translationLanguage = MutableStateFlow(com.example.data.model.TranslationLanguage.INDONESIA)

    // Theme & UI Preferences
    val themePalette = MutableStateFlow(AppThemePalette.EMERALD_GREEN)
    val isDarkMode = MutableStateFlow(false)
    val arabicFontSize = MutableStateFlow(24f)

    // Daily Reminder settings
    val dailyReminderEnabled = MutableStateFlow(true)
    val reminderHour = MutableStateFlow(5)
    val reminderMinute = MutableStateFlow(30)

    // Active instant announcement alert for Guru broadcasts
    private val _urgentBannerAnnouncement = MutableStateFlow<Announcement?>(null)
    val urgentBannerAnnouncement: StateFlow<Announcement?> = _urgentBannerAnnouncement.asStateFlow()

    // Reactive data from repositories
    val currentUser: StateFlow<UserProfile> = appRepo.currentUser
    val verificationRequests: StateFlow<List<RoleVerificationRequest>> = appRepo.verificationRequests
    val debugLogs: StateFlow<List<DebugLogEntry>> = appRepo.debugLogs
    val nearbyUsers: StateFlow<List<NearbyUser>> = appRepo.nearbyUsers

    val bookmarksFlow = hadithRepo.getBookmarksFlow()
    val overridesFlow = hadithRepo.getOverridesFlow()
    val announcementsFlow = appRepo.getAnnouncementsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val sharedTasksFlow = appRepo.getSharedTasksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val hangoutInvitationsFlow = appRepo.getHangoutInvitationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined Hadiths with bookmarks and Developer overrides
    private val baseHadithsFlow = combine(bookmarksFlow, overridesFlow) { bookmarks, overrides ->
        val bookmarkMap = bookmarks.associateBy { it.hadithId }
        val overrideMap = overrides.associateBy { it.hadithId }
        hadithRepo.getAllHadiths().map { h ->
            val b = bookmarkMap[h.id]
            val o = overrideMap[h.id]
            val base = if (o != null) {
                h.copy(
                    arabicText = o.arabicText,
                    titleId = o.titleId,
                    titleSu = o.titleSu,
                    titleEn = o.titleEn,
                    translationId = o.translationId,
                    translationSu = o.translationSu,
                    translationEn = o.translationEn,
                    syarahExplanation = o.syarahExplanation,
                    syarahSu = o.syarahSu,
                    syarahEn = o.syarahEn
                )
            } else h

            base.copy(
                isFavorite = b?.isFavorite ?: false,
                memorizationLevel = b?.memorizationLevel ?: 0
            )
        }
    }

    val hadithsWithProgress = combine(
        baseHadithsFlow,
        searchQuery,
        selectedCategory,
        onlyFavorites
    ) { hadithsList, query, category, favOnly ->
        hadithsList.filter { h ->
            val matchQuery = query.isEmpty() ||
                    h.number.toString().contains(query) ||
                    h.titleId.contains(query, ignoreCase = true) ||
                    h.titleSu.contains(query, ignoreCase = true) ||
                    h.titleEn.contains(query, ignoreCase = true) ||
                    h.arabicText.contains(query) ||
                    h.translationId.contains(query, ignoreCase = true) ||
                    h.translationSu.contains(query, ignoreCase = true) ||
                    h.translationEn.contains(query, ignoreCase = true)
            val matchCat = category == "Semua" || h.category == category
            val matchFav = !favOnly || h.isFavorite
            matchQuery && matchCat && matchFav
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), hadithRepo.getAllHadiths())

    // Active room chat messages
    private val _activeRoomMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val activeRoomMessages: StateFlow<List<ChatMessage>> = _activeRoomMessages.asStateFlow()

    fun setSlideIndex(index: Int) {
        _currentSlideIndex.value = index
    }

    fun openHadithDetail(hadith: Hadith) {
        _selectedHadithDetail.value = hadith
        viewModelScope.launch {
            hadithRepo.updateLastRead(hadith.id)
            appRepo.updateCurrentReadingHadith(hadith.id)
        }
    }

    fun closeHadithDetail() {
        _selectedHadithDetail.value = null
    }

    fun openChatRoom(roomId: String) {
        _activeChatRoomId.value = roomId
        viewModelScope.launch {
            appRepo.getChatMessagesFlow(roomId).collect { list ->
                _activeRoomMessages.value = list
            }
        }
    }

    fun closeChatRoom() {
        _activeChatRoomId.value = null
    }

    fun toggleFavorite(hadithId: Int, currentFav: Boolean) {
        viewModelScope.launch {
            hadithRepo.toggleFavorite(hadithId, currentFav)
        }
    }

    fun setMemorization(hadithId: Int, level: Int) {
        viewModelScope.launch {
            hadithRepo.setMemorizationLevel(hadithId, level)
        }
    }

    fun sendChatMessage(content: String, quotedHadithNum: Int? = null) {
        val roomId = _activeChatRoomId.value ?: return
        if (content.isBlank()) return
        viewModelScope.launch {
            val isGuru = currentUser.value.role == UserRole.GURU
            appRepo.sendMessage(
                roomId = roomId,
                content = content,
                quotedHadithNum = quotedHadithNum,
                isAnnouncement = isGuru && content.startsWith("/pengumuman")
            )
        }
    }

    fun postGuruAnnouncement(title: String, content: String, isUrgent: Boolean) {
        viewModelScope.launch {
            appRepo.postAnnouncement(title, content, isUrgent)
            if (isUrgent) {
                _urgentBannerAnnouncement.value = Announcement(
                    guruId = currentUser.value.id,
                    guruName = currentUser.value.name,
                    title = title,
                    content = content,
                    isUrgent = true
                )
            }
            Toast.makeText(getApplication(), "Pengumuman berhasil disiarkan ke semua murid!", Toast.LENGTH_SHORT).show()
        }
    }

    fun dismissUrgentBanner() {
        _urgentBannerAnnouncement.value = null
    }

    fun createSharedTask(title: String, description: String, cloudProvider: String, link: String, deadline: String) {
        viewModelScope.launch {
            appRepo.addSharedTask(title, description, cloudProvider, link, deadline)
            Toast.makeText(getApplication(), "Tugas bersama tersimpan & dibagikan!", Toast.LENGTH_SHORT).show()
        }
    }

    fun toggleTaskComplete(taskId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            appRepo.toggleTaskComplete(taskId, currentStatus)
        }
    }

    fun createHangout(placeName: String, address: String, topic: String, time: String, isHome: Boolean) {
        viewModelScope.launch {
            appRepo.createHangoutInvitation(placeName, address, topic, time, isHome)
            Toast.makeText(getApplication(), "Undangan nongkrong belajar berhasil dibuat!", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestRoleVerification(role: UserRole, emailOrPhone: String, reason: String) {
        appRepo.requestRoleActivation(role, emailOrPhone, reason)
        Toast.makeText(
            getApplication(),
            "Permohonan verifikasi dikirim ke Super Admin (ganjarboy033@gmail.com)",
            Toast.LENGTH_LONG
        ).show()
    }

    fun approveRoleRequest(requestId: String) {
        appRepo.approveRoleRequest(requestId)
        Toast.makeText(getApplication(), "Permohonan $requestId Disetujui!", Toast.LENGTH_SHORT).show()
    }

    fun rejectRoleRequest(requestId: String) {
        appRepo.rejectRoleRequest(requestId)
        Toast.makeText(getApplication(), "Permohonan $requestId Ditolak.", Toast.LENGTH_SHORT).show()
    }

    fun forceSwitchRole(role: UserRole) {
        appRepo.forceActivateRole(role)
        Toast.makeText(getApplication(), "Mode beralih ke: ${role.label}", Toast.LENGTH_SHORT).show()
    }

    fun updateProfile(name: String, phone: String, avatarId: Int) {
        appRepo.updateProfile(name, phone, avatarId)
        Toast.makeText(getApplication(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
    }

    fun updateOnlineStatus(isOnline: Boolean) {
        appRepo.updateOnlineStatus(isOnline)
    }

    fun sendDebugReport(problemTitle: String, details: String) {
        appRepo.addDebugLog("BugReport", "Laporan dikirim ke ganjarboy033@gmail.com: $problemTitle - $details", "WARN")
        Toast.makeText(
            getApplication(),
            "Laporan debug dikirim ke developer: ganjarboy033@gmail.com",
            Toast.LENGTH_LONG
        ).show()
    }

    fun triggerTestNotification() {
        val hadith = hadithRepo.getAllHadiths().random()
        Toast.makeText(
            getApplication(),
            "🔔 Pengingat Harian: Hadits ${hadith.number} (${hadith.titleId})",
            Toast.LENGTH_LONG
        ).show()
    }

    fun saveHadithOverride(
        hadithId: Int,
        arabicText: String,
        titleId: String,
        titleSu: String,
        titleEn: String,
        translationId: String,
        translationSu: String,
        translationEn: String,
        syarahExplanation: String,
        syarahSu: String = "",
        syarahEn: String = ""
    ) {
        viewModelScope.launch {
            hadithRepo.saveHadithOverride(
                hadithId = hadithId,
                arabicText = arabicText,
                titleId = titleId,
                titleSu = titleSu,
                titleEn = titleEn,
                translationId = translationId,
                translationSu = translationSu,
                translationEn = translationEn,
                syarahExplanation = syarahExplanation,
                syarahSu = syarahSu,
                syarahEn = syarahEn
            )
            // If current detail is open, update in-memory instance
            _selectedHadithDetail.value?.let { current ->
                if (current.id == hadithId) {
                    _selectedHadithDetail.value = current.copy(
                        arabicText = arabicText,
                        titleId = titleId,
                        titleSu = titleSu,
                        titleEn = titleEn,
                        translationId = translationId,
                        translationSu = translationSu,
                        translationEn = translationEn,
                        syarahExplanation = syarahExplanation,
                        syarahSu = syarahSu,
                        syarahEn = syarahEn
                    )
                }
            }
            Toast.makeText(getApplication(), "Teks hadis berhasil diperbarui oleh Developer!", Toast.LENGTH_SHORT).show()
        }
    }

    fun resetHadithToDefault(hadithId: Int) {
        viewModelScope.launch {
            hadithRepo.resetHadithToDefault(hadithId)
            val defaultHadith = hadithRepo.getHadithById(hadithId)
            if (defaultHadith != null && _selectedHadithDetail.value?.id == hadithId) {
                _selectedHadithDetail.value = defaultHadith
            }
            Toast.makeText(getApplication(), "Teks hadis dikembalikan ke naskah asli", Toast.LENGTH_SHORT).show()
        }
    }
}
