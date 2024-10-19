package com.cemerlang.dentalint.data.model.notes

data class NotesRequest(
    val fnb: String,
    val note: String? = null,
    val times: List<String>
)