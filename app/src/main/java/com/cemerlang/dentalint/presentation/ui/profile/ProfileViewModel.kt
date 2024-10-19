package com.cemerlang.dentalint.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.RoomDb
import com.cemerlang.dentalint.data.model.profile.UserPreferences
import com.cemerlang.dentalint.data.model.profile.UserUpdateRequest
import com.cemerlang.dentalint.data.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefManager: PrefManager,
    private val database: RoomDb
) : ViewModel() {

    private val _preferences = MutableStateFlow(UserPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()
    private val api = ApiClient.getApiInstance()


    init {
        _preferences.value = UserPreferences(
            name = prefManager.getString(PrefManager.Key.NAME),
            email = prefManager.getString(PrefManager.Key.EMAIL),
            notification = prefManager.getBoolean(PrefManager.Key.NOTIFICATION),
            language = prefManager.getString(PrefManager.Key.LANGUAGE),
            theme = prefManager.getString(PrefManager.Key.THEME)
        )
    }

    fun isLoggedIn(callback: (Boolean, String) -> Unit) {

        if (prefManager.getString(PrefManager.Key.TOKEN).isBlank()) {
            callback(false, "")
            return
        }

        viewModelScope.launch {
            try {
                val response = ApiClient.getApiInstance().getUser(
                    "Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}"
                )
                prefManager.apply {
                    saveInt(PrefManager.Key.UID, response.data.id)
                    saveString(PrefManager.Key.NAME, response.data.name)
                    saveString(PrefManager.Key.EMAIL, response.data.email)
                }
                callback(true, "")
            } catch (e: IOException) {
                callback(true, "Terjadi kesalahan jaringan")
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> callback(false, "Sesi berakhir. Silahkan login")
                    500 -> callback(true, "Tidak dapat terhubung ke server")
                    else -> callback(true, "Terjadi kesalahan")
                }
            }
        }
    }

    fun updatePrefs(key: PrefManager.Key, newValue: String) {
        prefManager.saveString(key, newValue)
        _preferences.value = when (key) {
            PrefManager.Key.NAME -> _preferences.value.copy(name = newValue)
            PrefManager.Key.EMAIL -> _preferences.value.copy(email = newValue)
            PrefManager.Key.LANGUAGE -> _preferences.value.copy(language = newValue)
            PrefManager.Key.THEME -> _preferences.value.copy(theme = newValue)
            else -> _preferences.value
        }
    }

    fun updatePrefs(key: PrefManager.Key, newValue: Boolean) {
        prefManager.saveBoolean(key, newValue)
        _preferences.value = when (key) {
            PrefManager.Key.NOTIFICATION -> _preferences.value.copy(notification = newValue)
            else -> _preferences.value
        }
    }

    fun updateUser(user: UserUpdateRequest, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                api.updateProfile(
                    token = "Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}",
                    body = user
                )
                updatePrefs(PrefManager.Key.NAME, user.name)
                callback(Result.success(Unit))
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

    fun logoutUser() {
        prefManager.clear()
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}