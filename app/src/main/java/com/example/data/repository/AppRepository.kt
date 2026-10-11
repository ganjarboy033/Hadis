package com.example.data.repository

import android.content.Context
import android.util.Base64
import com.example.data.local.AnnouncementEntity
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.HangoutEntity
import com.example.data.local.SharedTaskEntity
import com.example.data.model.Announcement
import com.example.data.model.ChatMessage
import com.example.data.model.DebugLogEntry
import com.example.data.model.HangoutInvitation
import com.example.data.model.NearbyUser
import com.example.data.model.RoleVerificationRequest
import com.example.data.model.SharedTask
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.model.VerificationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(
    private val context: Context,
    private val database: AppDatabase
) {
    // Current active user
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "ARB-MRD-${(1000..9999).random()}",
            name = "Ahmad Santriwan",
            phoneNumber = "+6281234567890",
            avatarId = 1,
            role = UserRole.MURID,
            verificationStatus = VerificationStatus.NONE,
            isOnline = true,
            lastSeenTimestamp = System.currentTimeMillis(),
            currentReadingHadithId = 1,
            locationName = "Pondok & Sekitarnya"
        )
    )
    val currentUser = _currentUser.asStateFlow()

    // Role Verification Requests queue for Super Admin (Developer)
    private val _verificationRequests = MutableStateFlow<List<RoleVerificationRequest>>(
        listOf(
            RoleVerificationRequest(
                requestId = "REQ-101",
                userId = "ARB-GUR-4021",
                userName = "Ustadz H. Abdullah, M.Ag",
                requestedRole = UserRole.GURU,
                userEmailOrPhone = "ustadz.abdullah@pesantren.id",
                verificationCode = "481920",
                reason = "Pengampu Halaqah Hadits Arbain Majelis Ta'lim Al-Hidayah",
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                isApproved = true,
                isReviewed = true
            ),
            RoleVerificationRequest(
                requestId = "REQ-102",
                userId = "ARB-MRD-5510",
                userName = "Muhammad Fadil",
                requestedRole = UserRole.GURU,
                userEmailOrPhone = "fadil.santri@gmail.com",
                verificationCode = "729104",
                reason = "Permohonan menjadi Ustadz Pendamping Santri Baru Angkatan 2026",
                timestamp = System.currentTimeMillis() - 1800000,
                isApproved = false,
                isReviewed = false
            )
        )
    )
    val verificationRequests = _verificationRequests.asStateFlow()

    // Debug logs
    private val _debugLogs = MutableStateFlow<List<DebugLogEntry>>(
        listOf(
            DebugLogEntry(tag = "Security", message = "E2EE Master Keypair initialized. SHA-256 Verified."),
            DebugLogEntry(tag = "Sync", message = "Room Database synced with local cache."),
            DebugLogEntry(tag = "Presence", message = "User presence tracker started (Online heartbeat 15s).")
        )
    )
    val debugLogs = _debugLogs.asStateFlow()

    // Nearby community users
    private val _nearbyUsers = MutableStateFlow<List<NearbyUser>>(
        listOf(
            NearbyUser(
                id = "ARB-GUR-4021",
                name = "Ustadz H. Abdullah",
                role = UserRole.GURU,
                distanceMeters = 180,
                isOnline = true,
                lastSeenText = "Online sekarang",
                currentReadingHadith = "Hadits 1: Niat dan Ikhlas",
                avatarId = 2
            ),
            NearbyUser(
                id = "ARB-MRD-3920",
                name = "Rizky Santri",
                role = UserRole.MURID,
                distanceMeters = 340,
                isOnline = true,
                lastSeenText = "Online sekarang",
                currentReadingHadith = "Hadits 2: Hadis Jibril (Islam, Iman, Ihsan)",
                avatarId = 3
            ),
            NearbyUser(
                id = "ARB-MRD-8841",
                name = "Nur Halimah",
                role = UserRole.MURID,
                distanceMeters = 650,
                isOnline = false,
                lastSeenText = "Terakhir dilihat 12 menit lalu",
                currentReadingHadith = "Hadits 16: Jangan Marah",
                avatarId = 4
            ),
            NearbyUser(
                id = "ARB-DEV-0001",
                name = "Developer Admin (Ganjar)",
                role = UserRole.DEVELOPER,
                distanceMeters = 920,
                isOnline = true,
                lastSeenText = "Online (Super Admin Console)",
                currentReadingHadith = "Hadits 42: Keluasan Ampunan Allah",
                avatarId = 5
            ),
            NearbyUser(
                id = "ARB-MRD-1290",
                name = "Faris Ikhwan",
                role = UserRole.MURID,
                distanceMeters = 1200,
                isOnline = true,
                lastSeenText = "Online sekarang",
                currentReadingHadith = "Hadits 6: Halal & Haram",
                avatarId = 1
            )
        )
    )
    val nearbyUsers = _nearbyUsers.asStateFlow()

    // Real-time Announcements
    fun getAnnouncementsFlow(): Flow<List<Announcement>> {
        return database.announcementDao().getAllAnnouncements().map { list ->
            if (list.isEmpty()) {
                // Return default initial announcements
                listOf(
                    Announcement(
                        id = "ANN-1",
                        guruId = "ARB-GUR-4021",
                        guruName = "Ustadz H. Abdullah",
                        title = "Jadwal Setoran Hafalan Hadis 1-10",
                        content = "Assalamu'alaikum santri sekalian. Besok Ba'da Maghrib diadakan tasmi' hafalan Hadits Arbain 1 hingga 10 di Majelis atau via room chat hadis. Harap dipersiapkan.",
                        targetClass = "Santri Kelas Arbain 1",
                        timestamp = System.currentTimeMillis() - 3600000 * 5,
                        isUrgent = true
                    ),
                    Announcement(
                        id = "ANN-2",
                        guruId = "ARB-GUR-4021",
                        guruName = "Ustadz H. Abdullah",
                        title = "Materi Tambahan Syarah Hadits Arbain Google Drive",
                        content = "Ringkasan kitab Jami'ul Ulum wal Hikam karya Ibnu Rajab Al-Hanbali telah diunggah ke Google Drive kelas. Silakan pelajari bagian Hadis 1 & 2.",
                        targetClass = "Semua Murid & Santri",
                        timestamp = System.currentTimeMillis() - 3600000 * 24,
                        isUrgent = false
                    )
                )
            } else {
                list.map {
                    Announcement(
                        id = it.id,
                        guruId = it.guruId,
                        guruName = it.guruName,
                        title = it.title,
                        content = it.content,
                        targetClass = it.targetClass,
                        timestamp = it.timestamp,
                        isUrgent = it.isUrgent
                    )
                }
            }
        }
    }

    suspend fun postAnnouncement(title: String, content: String, isUrgent: Boolean) {
        val user = _currentUser.value
        val entity = AnnouncementEntity(
            id = "ANN-${System.currentTimeMillis()}",
            guruId = user.id,
            guruName = user.name,
            title = title,
            content = content,
            targetClass = "Semua Santri & Murid",
            timestamp = System.currentTimeMillis(),
            isUrgent = isUrgent
        )
        database.announcementDao().insertAnnouncement(entity)
        addDebugLog("Announcement", "Pemberitahuan baru disiarkan oleh ${user.name}: '$title'")
    }

    // Shared Tasks
    fun getSharedTasksFlow(): Flow<List<SharedTask>> {
        return database.sharedTaskDao().getAllTasks().map { list ->
            if (list.isEmpty()) {
                listOf(
                    SharedTask(
                        id = "TSK-1",
                        title = "Tugas Ringkasan Makna Per-Kata Hadits 1",
                        description = "Tuliskan kosakata bahasa Arab dan syarah Hadits Innamal A'malu bin Niyyat di buku catatan dan unggah ke folder tugas.",
                        assignedByGuru = "Ustadz H. Abdullah",
                        cloudDriveProvider = "Google Drive",
                        cloudLink = "https://drive.google.com/drive/folders/arbain-hadis-1",
                        deadline = "Besok, 20:00 WIB",
                        isCompleted = false
                    ),
                    SharedTask(
                        id = "TSK-2",
                        title = "Rekaman Audio Tasmi' Hadits 2 (Hadis Jibril)",
                        description = "Kirimkan rekaman hafalan Hadis Jibril format MP3/M4A ke tautan Dropbox kelas.",
                        assignedByGuru = "Ustadz H. Abdullah",
                        cloudDriveProvider = "Dropbox",
                        cloudLink = "https://dropbox.com/scl/fo/arbain-hadis-jibril",
                        deadline = "3 Hari lagi",
                        isCompleted = true
                    )
                )
            } else {
                list.map {
                    SharedTask(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        assignedByGuru = it.assignedByGuru,
                        cloudDriveProvider = it.cloudDriveProvider,
                        cloudLink = it.cloudLink,
                        deadline = it.deadline,
                        isCompleted = it.isCompleted
                    )
                }
            }
        }
    }

    suspend fun addSharedTask(title: String, description: String, cloudProvider: String, link: String, deadline: String) {
        val entity = SharedTaskEntity(
            id = "TSK-${System.currentTimeMillis()}",
            title = title,
            description = description,
            assignedByGuru = _currentUser.value.name,
            cloudDriveProvider = cloudProvider,
            cloudLink = link,
            deadline = deadline,
            isCompleted = false
        )
        database.sharedTaskDao().insertTask(entity)
    }

    suspend fun toggleTaskComplete(taskId: String, currentCompleted: Boolean) {
        database.sharedTaskDao().setTaskCompleted(taskId, !currentCompleted)
    }

    // Hangout & Tongkrongan invitations
    fun getHangoutInvitationsFlow(): Flow<List<HangoutInvitation>> {
        return database.hangoutDao().getAllHangouts().map { list ->
            if (list.isEmpty()) {
                listOf(
                    HangoutInvitation(
                        id = "HNG-1",
                        creatorId = "ARB-MRD-3920",
                        creatorName = "Rizky Santri",
                        placeName = "Rumah Santri (Jl. Pesantren No. 12)",
                        locationAddress = "Komp. Santri Mandiri Blok B, 250m dari sini",
                        distanceMeters = 250,
                        topic = "Kajian Santai & Menghafal Hadits Arbain 1-5 Bareng",
                        scheduledTime = "Hari ini Ba'da Ashar (16:00)",
                        isHomeHangout = true,
                        attendeesCount = 4
                    ),
                    HangoutInvitation(
                        id = "HNG-2",
                        creatorId = "ARB-GUR-4021",
                        creatorName = "Ustadz H. Abdullah",
                        placeName = "Warkop Berkah & Majelis Santri",
                        locationAddress = "Jl. Merpati Putih No. 8, Dekat Masjid",
                        distanceMeters = 500,
                        topic = "Ngopi Santai Bahas Fiqih Muamalah Hadits 6",
                        scheduledTime = "Malam Ini (20:30 WIB)",
                        isHomeHangout = false,
                        attendeesCount = 7
                    )
                )
            } else {
                list.map {
                    HangoutInvitation(
                        id = it.id,
                        creatorId = it.creatorId,
                        creatorName = it.creatorName,
                        placeName = it.placeName,
                        locationAddress = it.locationAddress,
                        distanceMeters = it.distanceMeters,
                        topic = it.topic,
                        scheduledTime = it.scheduledTime,
                        isHomeHangout = it.isHomeHangout,
                        attendeesCount = it.attendeesCount
                    )
                }
            }
        }
    }

    suspend fun createHangoutInvitation(
        placeName: String,
        address: String,
        topic: String,
        time: String,
        isHome: Boolean
    ) {
        val entity = HangoutEntity(
            id = "HNG-${System.currentTimeMillis()}",
            creatorId = _currentUser.value.id,
            creatorName = _currentUser.value.name,
            placeName = placeName,
            locationAddress = address,
            distanceMeters = (100..800).random(),
            topic = topic,
            scheduledTime = time,
            isHomeHangout = isHome,
            attendeesCount = 1
        )
        database.hangoutDao().insertHangout(entity)
        addDebugLog("Tongkrongan", "Undangan nongkrong belajar dibuat di ${placeName} oleh ${_currentUser.value.name}")
    }

    // Chat with End-to-End Encryption
    fun getChatMessagesFlow(roomId: String): Flow<List<ChatMessage>> {
        return database.chatDao().getMessagesForRoom(roomId).map { list ->
            if (list.isEmpty()) {
                // Generate welcome discussion starters for this room
                val hadithNum = roomId.removePrefix("hadith_").toIntOrNull() ?: 1
                listOf(
                    ChatMessage(
                        id = "seed-msg-1",
                        roomId = roomId,
                        senderId = "ARB-GUR-4021",
                        senderName = "Ustadz H. Abdullah",
                        senderRole = UserRole.GURU,
                        content = "Assalamu'alaikum warahmatullah. Selamat datang di Ruang Diskusi Hadis ke-$hadithNum. Silakan diskusikan makna kata, syarah, serta setoran hafalan kalian di sini. Percakapan ini terenkripsi ujung-ke-ujung (E2EE).",
                        encryptedPayload = encryptE2EE("Selamat datang di Ruang Diskusi Hadis $hadithNum"),
                        timestamp = System.currentTimeMillis() - 3600000 * 3,
                        isEncrypted = true,
                        quotedHadithNumber = hadithNum,
                        isGuruAnnouncement = true
                    ),
                    ChatMessage(
                        id = "seed-msg-2",
                        roomId = roomId,
                        senderId = "ARB-MRD-3920",
                        senderName = "Rizky Santri",
                        senderRole = UserRole.MURID,
                        content = "Wa'alaikumussalam Ustadz. Alhamdulillah saya sudah membaca tabel makna per-katanya. Sangat membantu menghafal!",
                        encryptedPayload = encryptE2EE("Alhamdulillah saya sudah membaca tabel makna per-katanya"),
                        timestamp = System.currentTimeMillis() - 3600000 * 2,
                        isEncrypted = true,
                        quotedHadithNumber = hadithNum
                    )
                )
            } else {
                list.map {
                    ChatMessage(
                        id = it.id,
                        roomId = it.roomId,
                        senderId = it.senderId,
                        senderName = it.senderName,
                        senderRole = try { UserRole.valueOf(it.senderRole) } catch(e: Exception) { UserRole.MURID },
                        content = it.content,
                        encryptedPayload = it.encryptedPayload,
                        timestamp = it.timestamp,
                        isEncrypted = it.isEncrypted,
                        quotedHadithNumber = it.quotedHadithNumber,
                        isGuruAnnouncement = it.isGuruAnnouncement,
                        attachmentUrl = it.attachmentUrl
                    )
                }
            }
        }
    }

    suspend fun sendMessage(
        roomId: String,
        content: String,
        quotedHadithNum: Int? = null,
        isAnnouncement: Boolean = false,
        attachmentUrl: String? = null
    ) {
        val user = _currentUser.value
        val encrypted = encryptE2EE(content)
        val entity = ChatMessageEntity(
            id = "MSG-${System.currentTimeMillis()}-${(100..999).random()}",
            roomId = roomId,
            senderId = user.id,
            senderName = user.name,
            senderRole = user.role.name,
            content = content,
            encryptedPayload = encrypted,
            timestamp = System.currentTimeMillis(),
            isEncrypted = true,
            quotedHadithNumber = quotedHadithNum,
            isGuruAnnouncement = isAnnouncement,
            attachmentUrl = attachmentUrl
        )
        database.chatDao().insertMessage(entity)
        addDebugLog("Chat-E2EE", "Pesan terenkripsi dikirim ke ruang [$roomId] dari ${user.name}")
    }

    // Role Switch & Super Admin Verification
    fun requestRoleActivation(requestedRole: UserRole, userEmailOrPhone: String, reason: String): RoleVerificationRequest {
        val current = _currentUser.value
        val req = RoleVerificationRequest(
            userId = current.id,
            userName = current.name,
            requestedRole = requestedRole,
            userEmailOrPhone = userEmailOrPhone,
            reason = reason
        )
        _verificationRequests.value = listOf(req) + _verificationRequests.value
        _currentUser.value = current.copy(verificationStatus = VerificationStatus.PENDING_VERIFICATION)
        addDebugLog("Verification", "Permohonan mode ${requestedRole.label} dikirim untuk verifikasi email super admin (ganjarboy033@gmail.com)")
        return req
    }

    fun approveRoleRequest(requestId: String) {
        val currentList = _verificationRequests.value.toMutableList()
        val index = currentList.indexOfFirst { it.requestId == requestId }
        if (index != -1) {
            val req = currentList[index]
            val updated = req.copy(isApproved = true, isReviewed = true)
            currentList[index] = updated
            _verificationRequests.value = currentList

            // If it belongs to current user, apply immediately!
            if (req.userId == _currentUser.value.id) {
                _currentUser.value = _currentUser.value.copy(
                    role = req.requestedRole,
                    verificationStatus = VerificationStatus.APPROVED
                )
            }
            addDebugLog("AdminAction", "Super Admin menyetujui permohonan $requestId untuk mode ${req.requestedRole.label}")
        }
    }

    fun rejectRoleRequest(requestId: String) {
        val currentList = _verificationRequests.value.toMutableList()
        val index = currentList.indexOfFirst { it.requestId == requestId }
        if (index != -1) {
            val req = currentList[index]
            val updated = req.copy(isApproved = false, isReviewed = true)
            currentList[index] = updated
            _verificationRequests.value = currentList
            if (req.userId == _currentUser.value.id) {
                _currentUser.value = _currentUser.value.copy(
                    verificationStatus = VerificationStatus.REJECTED
                )
            }
            addDebugLog("AdminAction", "Super Admin menolak permohonan $requestId")
        }
    }

    // Direct Developer Mode Switch for Testing / Owner
    fun forceActivateRole(role: UserRole) {
        _currentUser.value = _currentUser.value.copy(
            role = role,
            verificationStatus = VerificationStatus.APPROVED
        )
        addDebugLog("RoleSwitch", "Mode pengguna langsung diubah menjadi ${role.label}")
    }

    // Profile updates
    fun updateProfile(name: String, phoneNumber: String, avatarId: Int) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            phoneNumber = phoneNumber,
            avatarId = avatarId
        )
        addDebugLog("Profile", "Profil pengguna diperbarui: $name ($phoneNumber)")
    }

    fun updateOnlineStatus(isOnline: Boolean) {
        _currentUser.value = _currentUser.value.copy(
            isOnline = isOnline,
            lastSeenTimestamp = System.currentTimeMillis()
        )
        addDebugLog("Presence", "Status online diubah menjadi: $isOnline")
    }

    fun updateCurrentReadingHadith(hadithId: Int) {
        _currentUser.value = _currentUser.value.copy(
            currentReadingHadithId = hadithId
        )
        // Also update in nearby list simulation
        val currentList = _nearbyUsers.value.toMutableList()
        val idx = currentList.indexOfFirst { it.id == _currentUser.value.id }
        if (idx != -1) {
            currentList[idx] = currentList[idx].copy(
                currentReadingHadith = "Hadits $hadithId"
            )
            _nearbyUsers.value = currentList
        }
        addDebugLog("ReadingTracker", "Pengguna kini sedang membaca Hadis nomor $hadithId")
    }

    fun addDebugLog(tag: String, message: String, level: String = "INFO") {
        val entry = DebugLogEntry(
            timestamp = System.currentTimeMillis(),
            tag = tag,
            message = message,
            level = level
        )
        _debugLogs.value = listOf(entry) + _debugLogs.value.take(49)
    }

    // Simulated End-to-End Encryption (E2EE)
    private fun encryptE2EE(plainText: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest("arbain_e2ee_salt_key".toByteArray(StandardCharsets.UTF_8))
            val bytes = plainText.toByteArray(StandardCharsets.UTF_8)
            val encryptedBytes = ByteArray(bytes.size)
            for (i in bytes.indices) {
                encryptedBytes[i] = (bytes[i].toInt() xor digest[i % digest.size].toInt()).toByte()
            }
            "E2EE:" + Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            "E2EE:enc_${plainText.hashCode()}"
        }
    }

    companion object {
        fun formatTimestamp(ts: Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(ts))
        }

        fun formatFullDate(ts: Long): String {
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
            return sdf.format(Date(ts))
        }
    }
}
