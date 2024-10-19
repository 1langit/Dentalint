package com.cemerlang.dentalint.data.model.profile

data class UserPreferences(
    var name: String = "",
    var email: String = "",
    var notification: Boolean = true,
    var language: String = "English",
    var theme: String = "System default"
)