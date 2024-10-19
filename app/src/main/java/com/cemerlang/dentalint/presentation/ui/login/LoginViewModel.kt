package com.cemerlang.dentalint.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.model.login.LoginData
import com.cemerlang.dentalint.data.model.login.LoginRequest
import com.cemerlang.dentalint.data.model.login.LoginResponse
import com.cemerlang.dentalint.data.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val prefManager: PrefManager
) : ViewModel() {

    private val api = ApiClient.getApiInstance()

    fun loginUser(userData: LoginRequest, callback: (Result<Unit>) -> Unit) {

        // stub
        if (userData == LoginRequest("cemerlang@test.dev", "cemerlang")) {
            savePreferences(
                LoginResponse(
                    LoginData(1, "cemerlang@test.dev", "Cemerlang", "1", "1"),
                    "1"
                )
            )
            callback(Result.success(Unit))
            return
        }

        viewModelScope.launch {
            try {
                val response = api.login(userData)
                savePreferences(response)
                callback(Result.success(Unit))
                AnalyticsHelper.logSignIn()
            } catch (e: IOException) {
                callback(Result.failure(Exception("Terjadi kesalahan jaringan")))
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> callback(Result.failure(Exception("Pengguna tidak ditemukan")))
                    500 -> callback(Result.failure(Exception("Tidak dapat terhubung ke server")))
                    else -> callback(Result.failure(Exception("Terjadi kesalahan")))
                }
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                callback(Result.failure(Exception("Terjadi kesalahan")))
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    private fun savePreferences(response: LoginResponse) {
        prefManager.apply {
            saveInt(PrefManager.Key.UID, response.data.id)
            saveString(PrefManager.Key.TOKEN, response.token)
            saveString(PrefManager.Key.NAME, response.data.name)
            saveString(PrefManager.Key.EMAIL, response.data.email)
            saveBoolean(PrefManager.Key.NOTIFICATION, true)
            saveString(PrefManager.Key.LANGUAGE, "Default")
            saveString(PrefManager.Key.THEME, "System default")
        }
    }
}