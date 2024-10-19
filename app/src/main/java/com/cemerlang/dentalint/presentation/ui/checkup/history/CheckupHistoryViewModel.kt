package com.cemerlang.dentalint.presentation.ui.checkup.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.data.model.checkup.AppointmentListResponse
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
class CheckupHistoryViewModel @Inject constructor(
    private val prefManager: PrefManager
) : ViewModel() {

    private val _appointments = MutableStateFlow(AppointmentListResponse(emptyList()))
    val appointments: StateFlow<AppointmentListResponse> = _appointments.asStateFlow()

    private var selectedAppointment: AppointmentData? = null

    private val api = ApiClient.getApiInstance()

    fun getAppointments() {
        viewModelScope.launch {
            try {
                _appointments.value = api.getAppointments("Bearer ${prefManager.getString(
                    PrefManager.Key.TOKEN)}")
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun selectAppointment(appointment: AppointmentData) {
        selectedAppointment = appointment
    }

    fun getSelectedAppointment(): AppointmentData? {
        return selectedAppointment
    }
}