package com.cemerlang.dentalint.data.model.checkup

data class ClinicData(
    val id: Int = 0,
    val clinic_name: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val logo: String = "",
    val address: String = "",
    val link: String = "",
    val result: String = "",
    val created_at: String = ""
)
