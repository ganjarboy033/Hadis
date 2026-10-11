package com.example.data.model

import java.util.UUID

enum class UserRole(val label: String, val badgeColorHex: Long) {
    MURID("Mode Murid", 0xFF10B981),      // Emerald Green
    GURU("Mode Guru", 0xFFF59E0B),        // Amber Gold
    DEVELOPER("Mode Developer", 0xFF6366F1) // Indigo/Purple
}

enum class VerificationStatus {
    NONE,
    PENDING_VERIFICATION,
    APPROVED,
    REJECTED
}

data class UserProfile(
    val id: String = "ARB-${(1000..9999).random()}",
    val name: String = "Santri Arbain",
    val phoneNumber: String = "",
    val avatarId: Int = 1,
    val role: UserRole = UserRole.MURID,
    val verificationStatus: VerificationStatus = VerificationStatus.NONE,
    val isOnline: Boolean = true,
    val lastSeenTimestamp: Long = System.currentTimeMillis(),
    val currentReadingHadithId: Int? = 1,
    val latitude: Double = -6.2088,
    val longitude: Double = 106.8456,
    val locationName: String = "Jakarta Pusat"
)

data class RoleVerificationRequest(
    val requestId: String = UUID.randomUUID().toString().take(8),
    val userId: String,
    val userName: String,
    val requestedRole: UserRole,
    val userEmailOrPhone: String,
    val verificationCode: String = "${(100000..999999).random()}",
    val reason: String,
    val timestamp: Long = System.currentTimeMillis(),
    var isApproved: Boolean = false,
    var isReviewed: Boolean = false
)

enum class AppLanguage(val label: String, val code: String) {
    INDONESIA("Bahasa Indonesia", "id"),
    SUNDA("Basa Sunda", "su"),
    ENGLISH("English", "en")
}

enum class TranslationLanguage(val label: String, val code: String) {
    INDONESIA("Bahasa Indonesia", "id"),
    SUNDA("Basa Sunda", "su"),
    ENGLISH("English", "en")
}

data class WordTranslation(
    val arabic: String,
    val transliteration: String,
    val meaningId: String,
    val meaningSu: String = "",
    val meaningEn: String = ""
) {
    fun getMeaning(lang: TranslationLanguage): String {
        return when (lang) {
            TranslationLanguage.SUNDA -> if (meaningSu.isNotBlank()) meaningSu else meaningId
            TranslationLanguage.ENGLISH -> if (meaningEn.isNotBlank()) meaningEn else meaningId
            TranslationLanguage.INDONESIA -> meaningId
        }
    }
}

data class Hadith(
    val id: Int, // 1 to 42
    val number: Int,
    val titleArabic: String,
    var titleId: String,
    var titleSu: String = "",
    var titleEn: String = "",
    val rawi: String,
    var arabicText: String,
    var translationId: String,
    var translationSu: String = "",
    var translationEn: String = "",
    val words: List<WordTranslation>,
    var syarahExplanation: String,
    var syarahSu: String = "",
    var syarahEn: String = "",
    val category: String,
    var isFavorite: Boolean = false,
    var memorizationLevel: Int = 0 // 0: Belum, 1: Sedang Menghafal, 2: Sudah Hafal
) {
    fun getTitle(lang: TranslationLanguage): String {
        return when (lang) {
            TranslationLanguage.SUNDA -> if (titleSu.isNotBlank()) titleSu else titleId
            TranslationLanguage.ENGLISH -> if (titleEn.isNotBlank()) titleEn else titleId
            TranslationLanguage.INDONESIA -> titleId
        }
    }

    fun getTranslation(lang: TranslationLanguage): String {
        return when (lang) {
            TranslationLanguage.SUNDA -> if (translationSu.isNotBlank()) translationSu else translationId
            TranslationLanguage.ENGLISH -> if (translationEn.isNotBlank()) translationEn else translationId
            TranslationLanguage.INDONESIA -> translationId
        }
    }

    fun getSyarah(lang: TranslationLanguage): String {
        return when (lang) {
            TranslationLanguage.SUNDA -> if (syarahSu.isNotBlank()) syarahSu else syarahExplanation
            TranslationLanguage.ENGLISH -> if (syarahEn.isNotBlank()) syarahEn else syarahExplanation
            TranslationLanguage.INDONESIA -> syarahExplanation
        }
    }
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String, // "hadith_1" .. "hadith_42" or "direct_user..."
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val content: String,
    val encryptedPayload: String, // E2EE ciphertext simulation
    val timestamp: Long = System.currentTimeMillis(),
    val isEncrypted: Boolean = true,
    val quotedHadithNumber: Int? = null,
    val isGuruAnnouncement: Boolean = false,
    val attachmentUrl: String? = null
)

data class Announcement(
    val id: String = UUID.randomUUID().toString(),
    val guruId: String,
    val guruName: String,
    val title: String,
    val content: String,
    val targetClass: String = "Semua Santri & Murid",
    val timestamp: Long = System.currentTimeMillis(),
    val isUrgent: Boolean = true
)

data class SharedTask(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val assignedByGuru: String,
    val cloudDriveProvider: String, // "Google Drive" or "Dropbox"
    val cloudLink: String,
    val deadline: String,
    val isCompleted: Boolean = false
)

data class HangoutInvitation(
    val id: String = UUID.randomUUID().toString(),
    val creatorId: String,
    val creatorName: String,
    val placeName: String,
    val locationAddress: String,
    val distanceMeters: Int,
    val topic: String,
    val scheduledTime: String,
    val isHomeHangout: Boolean = true,
    val attendeesCount: Int = 1
)

data class NearbyUser(
    val id: String,
    val name: String,
    val role: UserRole,
    val distanceMeters: Int,
    val isOnline: Boolean,
    val lastSeenText: String,
    val currentReadingHadith: String,
    val avatarId: Int
)

data class DebugLogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String,
    val message: String,
    val level: String = "INFO"
)
