package com.cemerlang.dentalint.data.model.chatbot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String = "",
    val participant: Participant = Participant.USER,
)