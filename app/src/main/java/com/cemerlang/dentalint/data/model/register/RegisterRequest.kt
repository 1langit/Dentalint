package com.cemerlang.dentalint.data.model.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)