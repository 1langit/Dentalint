package com.cemerlang.dentalint.data.model.checkup

data class PatientRequest(
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
