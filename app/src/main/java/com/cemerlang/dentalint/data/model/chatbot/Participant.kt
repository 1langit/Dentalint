package com.cemerlang.dentalint.data.model.chatbot

enum class Participant(type: String) {
    USER("user"),
    MODEL("model"),
    ERROR("error")
}