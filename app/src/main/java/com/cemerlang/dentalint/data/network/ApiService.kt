package com.cemerlang.dentalint.data.network

import com.cemerlang.dentalint.data.model.blog.BlogResponse
import com.cemerlang.dentalint.data.model.capture.CaptureResponse
import com.cemerlang.dentalint.data.model.capture.CaptureResult
import com.cemerlang.dentalint.data.model.checkup.AppointmentRequest
import com.cemerlang.dentalint.data.model.checkup.AppointmentListResponse
import com.cemerlang.dentalint.data.model.checkup.AppointmentResponse
import com.cemerlang.dentalint.data.model.checkup.ClinicListResponse
import com.cemerlang.dentalint.data.model.checkup.ClinicResponse
import com.cemerlang.dentalint.data.model.checkup.PatientRequest
import com.cemerlang.dentalint.data.model.checkup.PatientResponse
import com.cemerlang.dentalint.data.model.login.LoginRequest
import com.cemerlang.dentalint.data.model.login.LoginResponse
import com.cemerlang.dentalint.data.model.notes.NotesRequest
import com.cemerlang.dentalint.data.model.notes.NotesResponse
import com.cemerlang.dentalint.data.model.profile.UserUpdateRequest
import com.cemerlang.dentalint.data.model.register.RegisterRequest
import com.cemerlang.dentalint.data.model.user.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // ---------------------------------- User
    @POST("users/register")
    suspend fun register(
        @Body body: RegisterRequest
    )

    @POST("users/login")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse

    @GET("users")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): UserResponse

    @PATCH("users")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: UserUpdateRequest
    )

    // ---------------------------------- Blog
    @GET("blogs")
    suspend fun getBlogs(
        @Header("Authorization") token: String
    ): BlogResponse

    // ---------------------------------- Notes
    @GET("notes")
    suspend fun getNotes(
        @Header("Authorization") token: String
    ): NotesResponse

    @POST("notes")
    suspend fun addNotes(
        @Header("Authorization") token: String,
        @Body body: NotesRequest
    )

    // ---------------------------------- Capture
    @GET("captures")
    suspend fun getCapture(
        @Header("Authorization") token: String
    ): CaptureResponse

    @GET("captures/{id}")
    suspend fun getCapture(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): CaptureResult

    @POST("captures")
    @Multipart
    suspend fun addCapture(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): CaptureResult

    // ---------------------------------- Checkup
    @GET("clinics")
    suspend fun getClinics(
        @Header("Authorization") token: String,
    ): ClinicListResponse

    @GET("clinics/{id}")
    suspend fun getClinic(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ClinicResponse

    @POST("patients")
    suspend fun addPatient(
        @Header("Authorization") token: String,
        @Body body: PatientRequest
    ): PatientResponse

    @GET("appointments")
    suspend fun getAppointments(
        @Header("Authorization") token: String,
    ): AppointmentListResponse

    @POST("appointments")
    suspend fun addAppointment(
        @Header("Authorization") token: String,
        @Body body: AppointmentRequest
    ): AppointmentResponse
}