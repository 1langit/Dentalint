package com.cemerlang.dentalint.data.model.checkup

data class AppointmentRequest(
    val rekam_medis: String,
    val clinic_id: Int,
    val schedule: String,
    val status: String,
    val polyclinic: String,
    val payment: String
)
