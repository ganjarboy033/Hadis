package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hadith_bookmarks")
data class HadithBookmarkEntity(
    @PrimaryKey val hadithId: Int,
    val isFavorite: Boolean,
    val memorizationLevel: Int, // 0 = belum, 1 = sedang, 2 = hafal
    val lastReadTimestamp: Long
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val senderId: String,
    val senderName: String,
    val senderRole: String,
    val content: String,
    val encryptedPayload: String,
    val timestamp: Long,
    val isEncrypted: Boolean,
    val quotedHadithNumber: Int?,
    val isGuruAnnouncement: Boolean,
    val attachmentUrl: String?
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey val id: String,
    val guruId: String,
    val guruName: String,
    val title: String,
    val content: String,
    val targetClass: String,
    val timestamp: Long,
    val isUrgent: Boolean
)

@Entity(tableName = "shared_tasks")
data class SharedTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val assignedByGuru: String,
    val cloudDriveProvider: String,
    val cloudLink: String,
    val deadline: String,
    val isCompleted: Boolean
)

@Entity(tableName = "hangout_invitations")
data class HangoutEntity(
    @PrimaryKey val id: String,
    val creatorId: String,
    val creatorName: String,
    val placeName: String,
    val locationAddress: String,
    val distanceMeters: Int,
    val topic: String,
    val scheduledTime: String,
    val isHomeHangout: Boolean,
    val attendeesCount: Int
)

@Entity(tableName = "hadith_overrides")
data class HadithOverrideEntity(
    @PrimaryKey val hadithId: Int,
    val arabicText: String,
    val titleId: String,
    val titleSu: String,
    val titleEn: String,
    val translationId: String,
    val translationSu: String,
    val translationEn: String,
    val syarahExplanation: String,
    val syarahSu: String,
    val syarahEn: String,
    val updatedAt: Long
)
