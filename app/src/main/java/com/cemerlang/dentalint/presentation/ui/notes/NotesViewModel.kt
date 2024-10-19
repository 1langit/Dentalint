package com.cemerlang.dentalint.presentation.ui.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.NotesDao
import com.cemerlang.dentalint.data.model.notes.NotesData
import com.cemerlang.dentalint.data.model.notes.NotesRequest
import com.cemerlang.dentalint.data.model.notes.NotesResponse
import com.cemerlang.dentalint.data.network.ApiClient
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val prefManager: PrefManager,
    private val notesDao: NotesDao
) : ViewModel() {

    private val _notes = MutableStateFlow(NotesResponse(emptyList()))
    val notes: StateFlow<NotesResponse> = _notes.asStateFlow()
    private val api = ApiClient.getApiInstance()

    fun fetchNotes() {
        viewModelScope.launch {
            try {
                val response = api.getNotes("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}")
                response.data.map { it.created_at = formatDate(it.created_at) }
                _notes.value = response
                notesDao.replaceNotes(response.data)
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
                _notes.value = NotesResponse(notesDao.getNotes())
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun addNotes(notes: NotesRequest, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                api.addNotes(
                    "Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}",
                    notes
                )
                callback(Result.success(Unit))
                AnalyticsHelper.logFeature("add_notes")
            } catch (e: IOException) {
                callback(Result.failure(Exception("Terjadi kesalahan jaringan")))
                Log.e("RegisterUser", "Network error: ${e.message}")
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> callback(Result.failure(Exception("Tidak boleh kosong")))
                    500 -> callback(Result.failure(Exception("Tidak dapat terhubung ke server")))
                    else -> callback(Result.failure(Exception("Terjadi kesalahan")))
                }
                Log.e("RegisterUser", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                callback(Result.failure(Exception("Terjadi kesalahan")))
                Log.e("RegisterUser", "Unexpected error: ${e.message}")
            }
        }
    }

    fun getNoteById(id: String, callback: (NotesData) -> Unit) {
        viewModelScope.launch {
            callback(notesDao.getNote(id))
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy • HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    fun requestNotificationPermission() {}
}