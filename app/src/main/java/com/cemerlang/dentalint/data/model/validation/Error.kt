package com.cemerlang.dentalint.data.model.validation

data class Error(
    val code: String,
    val exact: Boolean,
    val inclusive: Boolean,
    val message: String,
    val minimum: Int,
    val path: List<String>,
    val type: String
)