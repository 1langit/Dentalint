package com.cemerlang.dentalint.data.model.login

data class LoginResponse(
    val `data`: LoginData,
    val token: String
)