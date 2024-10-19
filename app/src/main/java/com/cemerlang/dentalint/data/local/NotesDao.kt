package com.cemerlang.dentalint.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cemerlang.dentalint.data.model.notes.NotesData

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    suspend fun getNotes() : List<NotesData>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNote(noteId: String): NotesData

    @Query("DELETE FROM notes")
    suspend fun deleteNotes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NotesData>)

    @Transaction
    suspend fun replaceNotes(notes: List<NotesData>) {
        deleteNotes()
        insertNotes(notes)
    }
}