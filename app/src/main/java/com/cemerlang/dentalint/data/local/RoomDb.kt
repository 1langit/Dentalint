package com.cemerlang.dentalint.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cemerlang.dentalint.data.model.blog.BlogData
import com.cemerlang.dentalint.data.model.capture.CaptureData
import com.cemerlang.dentalint.data.model.capture.CaptureEntity
import com.cemerlang.dentalint.data.model.chatbot.ChatMessage
import com.cemerlang.dentalint.data.model.notes.NotesData

@Database(
    entities = [ChatMessage::class, BlogData::class, NotesData::class, CaptureEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase() {

    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun blogDao(): BlogDao
    abstract fun notesDao(): NotesDao
    abstract fun captureDao(): CaptureDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDb? = null

        fun getDatabase(context: Context): RoomDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "dentalint_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}