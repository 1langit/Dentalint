package com.cemerlang.dentalint.data.model.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesData(
    @PrimaryKey
    val id: Int,
    val user_id: Int,
    val times: List<String>,
    val fnb: String,
    val note: String?,
    val title: String,
    var created_at: String
)