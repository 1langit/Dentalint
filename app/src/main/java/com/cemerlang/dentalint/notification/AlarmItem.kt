package com.cemerlang.dentalint.notification

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val title: String,
    val content: String
)
