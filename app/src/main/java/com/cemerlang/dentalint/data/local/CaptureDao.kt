package com.cemerlang.dentalint.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cemerlang.dentalint.data.model.capture.CaptureEntity

@Dao
interface CaptureDao {

    @Query("SELECT * FROM captures ORDER BY created_at DESC")
    suspend fun getCaptures() : List<CaptureEntity>

    @Query("SELECT * FROM captures WHERE id = :captureId")
    suspend fun getCapture(captureId: String): CaptureEntity

    @Query("SELECT * FROM captures ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestCapture(): CaptureEntity?

    @Query("DELETE FROM captures")
    suspend fun deleteCaptures()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaptures(captures: List<CaptureEntity>)

    @Transaction
    suspend fun replaceCaptures(captures: List<CaptureEntity>) {
        deleteCaptures()
        insertCaptures(captures)
    }
}