package com.cemerlang.dentalint.di

import android.content.Context
import com.cemerlang.dentalint.data.local.BlogDao
import com.cemerlang.dentalint.data.local.CaptureDao
import com.cemerlang.dentalint.data.local.ChatMessageDao
import com.cemerlang.dentalint.data.local.NotesDao
import com.cemerlang.dentalint.data.local.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDb {
        return RoomDb.getDatabase(context)
    }

    @Provides
    fun provideChatMessageDao(database: RoomDb): ChatMessageDao {
        return database.chatMessageDao()
    }

    @Provides
    fun provideBlogsDao(database: RoomDb): BlogDao {
        return database.blogDao()
    }

    @Provides
    fun provideNotesDao(database: RoomDb): NotesDao {
        return database.notesDao()
    }

    @Provides
    fun provideCapturesDao(database: RoomDb): CaptureDao {
        return database.captureDao()
    }
}