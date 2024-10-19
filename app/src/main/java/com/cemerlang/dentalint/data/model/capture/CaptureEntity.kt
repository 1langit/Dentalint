package com.cemerlang.dentalint.data.model.capture

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captures")
data class CaptureEntity(
    @PrimaryKey
    val id: Int,
    val user_id: Int,
    @ColumnInfo(name = "class")
    val captureClass: String,
    val image: String,
    val result: String,
    val created_at: String
)