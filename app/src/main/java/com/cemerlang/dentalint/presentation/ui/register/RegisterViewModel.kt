package com.cemerlang.dentalint.presentation.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.data.network.ApiClient
import com.cemerlang.dentalint.data.model.register.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel : ViewModel() {

    private val api = ApiClient.getApiInstance()

    fun registerUser(userData: RegisterRequest, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                api.register(userData)
                callback(Result.success(Unit))
            } catch (e: IOException) {
                callback(Result.failure(Exception("Terjadi kesalahan jaringan")))
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> callback(Result.failure(Exception("Pengguna sudah terdaftar")))
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
}