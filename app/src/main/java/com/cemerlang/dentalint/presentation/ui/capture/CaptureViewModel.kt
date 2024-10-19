package com.cemerlang.dentalint.presentation.ui.capture

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.CaptureDao
import com.cemerlang.dentalint.data.model.capture.CaptureData
import com.cemerlang.dentalint.data.model.capture.CaptureEntity
import com.cemerlang.dentalint.data.model.capture.CaptureResponse
import com.cemerlang.dentalint.data.network.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor(
    private val prefManager: PrefManager,
//    private val captureDao: CaptureDao
) : ViewModel() {

    private val _captures = MutableStateFlow(CaptureResponse(emptyList()))
    val captures: StateFlow<CaptureResponse> = _captures.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val api = ApiClient.getApiInstance()

    fun fetchCaptures() {
        viewModelScope.launch {
            try {
                val response = api.getCapture("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}")
                response.data.map { it.created_at = formatDate(it.created_at) }
                _captures.value = response
//                captureDao.replaceCaptures(response.data.map { it.toEntity() })
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun newCapture(file: File, callback: (Result<CaptureData>) -> Unit) {
        _isLoading.value = true
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        viewModelScope.launch {
            try {
                val response = api.addCapture(
                    token = "Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}",
                    image = imagePart
                )
                callback(Result.success(response.data))
                _isLoading.value = false
                AnalyticsHelper.logFeature("new_capture")
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
                callback(Result.failure(Exception("Terjadi kesalahan jaringan")))
                _isLoading.value = false
            } catch (e: HttpException) {
                when (e.code()) {
                    500 -> callback(Result.failure(Exception(e.response()?.errorBody()?.string()?.let { parseErrorMessage(it) } ?: e.message())))
                    else -> callback(Result.failure(Exception("Gagal melakukan scan")))
                }
                Log.e("api", "HTTP error: ${e.message()}")
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
                callback(Result.failure(Exception("Gagal melakukan scan")))
                _isLoading.value = false
            }
        }
    }

    fun getCaptureById(id: Int, callback: (CaptureData) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.getCapture("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}", id)
                response.data.created_at = formatDate(response.data.created_at)
                callback(response.data)
//                captureDao.replaceCaptures(response.data.map { it.toEntity() })
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy • HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("error")
        } catch (e: JSONException) {
            "Gagal melakukan scan"
        }
    }

//    private fun CaptureData.toEntity(): CaptureEntity {
//        return CaptureEntity(
//            id = this.id,
//            user_id = this.user_id,
//            captureClass = this.`class`,
//            image = this.image,
//            result = this.result,
//            created_at = this.created_at
//        )
//    }
}