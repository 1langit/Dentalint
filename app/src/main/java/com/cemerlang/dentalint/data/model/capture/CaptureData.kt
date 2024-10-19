package com.cemerlang.dentalint.data.model.capture

data class CaptureData(
    val id: Int,
    val user_id: Int,
    val `class`: String,
    val image: String,
    val result: String,
    var created_at: String
)