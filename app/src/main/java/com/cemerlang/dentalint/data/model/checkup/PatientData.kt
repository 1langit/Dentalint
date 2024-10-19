package com.cemerlang.dentalint.data.model.checkup

data class PatientData(
    val rekam_medis: String,
    val user_id: Int,
    val clinic_id: Int,
    val name: String,
    val nik: String,
    val jenis_kelamin: String,
    val golongan_darah: String,
    val tempat_lahir: String,
    val tanggal_lahir: String,
    val alamat: String,
    val no_hp: String
)
