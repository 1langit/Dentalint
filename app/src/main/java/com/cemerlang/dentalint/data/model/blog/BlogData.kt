package com.cemerlang.dentalint.data.model.blog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blogs")
data class BlogData(
    @PrimaryKey
    val id: String,
    val title: String,
    val image: String,
    val source: String,
    val content: String,
    var created_at: String,
    val updated_at: String
)