package com.cemerlang.dentalint.data.model.checkup

data class AppointmentData (
    val no_antrian: String,
    val rekam_medis: String,
    val clinic_id: Int,
    val schedule: String,
    val status: String,
    val polyclinic: String,
    val payment: String,
    val created_at: String,
    val updated_at: String,
    val clinic: ClinicName,
    val isDone: Boolean
)