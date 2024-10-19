package com.cemerlang.dentalint.presentation.ui.checkup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.data.model.checkup.AppointmentListResponse
import com.cemerlang.dentalint.data.model.checkup.AppointmentRequest
import com.cemerlang.dentalint.data.model.checkup.AppointmentResponse
import com.cemerlang.dentalint.data.model.checkup.ClinicData
import com.cemerlang.dentalint.data.model.checkup.ClinicListResponse
import com.cemerlang.dentalint.data.model.checkup.ClinicResponse
import com.cemerlang.dentalint.data.model.checkup.PatientData
import com.cemerlang.dentalint.data.model.checkup.PatientRequest
import com.cemerlang.dentalint.data.model.checkup.PatientResponse
import com.cemerlang.dentalint.data.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CheckupViewModel @Inject constructor(
    private val prefManager: PrefManager
) : ViewModel() {

    private val _clinics = MutableStateFlow(ClinicListResponse(emptyList()))
    val clinics: StateFlow<ClinicListResponse> = _clinics.asStateFlow()

    private val _selectedClinic = MutableStateFlow(ClinicData())
    val selectedClinic: StateFlow<ClinicData> = _selectedClinic.asStateFlow()

    private val api = ApiClient.getApiInstance()

    fun selectClinic(id: Int) {
        getClinic(id) { clinic ->
            _selectedClinic.value = clinic.data
        }
    }

    fun getClinics() {
        viewModelScope.launch {
            try {
                _clinics.value = api.getClinics("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}")
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun getClinic(id: Int, callback: (ClinicResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.getClinic("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}", id)
                callback(response)
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun registerAppointment(appointment: AppointmentRequest, callback: (Result<AppointmentData>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.addAppointment("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}", appointment)
                callback(Result.success(response.data))
                AnalyticsHelper.logFeature("appointment_register")
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
                callback(Result.failure(e))
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
                callback(Result.failure(e))
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
                callback(Result.failure(e))
            }
        }
    }

    fun registerPatient(patient: PatientRequest, callback: (Result<PatientData>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.addPatient("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}", patient)
                callback(Result.success(response.data))
                AnalyticsHelper.logFeature("patient_register")
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
                callback(Result.failure(e))
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
                callback(Result.failure(e))
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
                callback(Result.failure(e))
            }
        }
    }

    fun getUsername() : String {
        return prefManager.getString(PrefManager.Key.NAME)
    }
}