package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface HadithBookmarkDao {
    @Query("SELECT * FROM hadith_bookmarks")
    fun getAllBookmarks(): Flow<List<HadithBookmarkEntity>>

    @Query("SELECT * FROM hadith_bookmarks WHERE hadithId = :hadithId")
    suspend fun getBookmark(hadithId: Int): HadithBookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBookmark(bookmark: HadithBookmarkEntity)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE roomId = :roomId ORDER BY timestamp ASC")
    fun getMessagesForRoom(roomId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("SELECT COUNT(*) FROM chat_messages")
    fun getTotalMessageCount(): Flow<Int>
}

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)
}

@Dao
interface SharedTaskDao {
    @Query("SELECT * FROM shared_tasks ORDER BY isCompleted ASC")
    fun getAllTasks(): Flow<List<SharedTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: SharedTaskEntity)

    @Query("UPDATE shared_tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun setTaskCompleted(taskId: String, completed: Boolean)
}

@Dao
interface HangoutDao {
    @Query("SELECT * FROM hangout_invitations ORDER BY distanceMeters ASC")
    fun getAllHangouts(): Flow<List<HangoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHangout(hangout: HangoutEntity)
}

@Dao
interface HadithOverrideDao {
    @Query("SELECT * FROM hadith_overrides")
    fun getAllOverrides(): Flow<List<HadithOverrideEntity>>

    @Query("SELECT * FROM hadith_overrides WHERE hadithId = :hadithId")
    suspend fun getOverride(hadithId: Int): HadithOverrideEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOverride(override: HadithOverrideEntity)

    @Query("DELETE FROM hadith_overrides WHERE hadithId = :hadithId")
    suspend fun deleteOverride(hadithId: Int)
}

@Database(
    entities = [
        HadithBookmarkEntity::class,
        ChatMessageEntity::class,
        AnnouncementEntity::class,
        SharedTaskEntity::class,
        HangoutEntity::class,
        HadithOverrideEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hadithBookmarkDao(): HadithBookmarkDao
    abstract fun chatDao(): ChatDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun sharedTaskDao(): SharedTaskDao
    abstract fun hangoutDao(): HangoutDao
    abstract fun hadithOverrideDao(): HadithOverrideDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hadits_arbain_komunitas.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
