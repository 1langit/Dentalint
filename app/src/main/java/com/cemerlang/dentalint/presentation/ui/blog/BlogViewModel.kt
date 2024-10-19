package com.cemerlang.dentalint.presentation.ui.blog

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.BlogDao
import com.cemerlang.dentalint.data.model.blog.BlogData
import com.cemerlang.dentalint.data.model.blog.BlogResponse
import com.cemerlang.dentalint.data.network.ApiClient
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
class BlogViewModel @Inject constructor(
    private val prefManager: PrefManager,
    private val blogDao: BlogDao
) : ViewModel() {

    private val _blogs = MutableStateFlow(BlogResponse(emptyList()))
    val blogs: StateFlow<BlogResponse> = _blogs.asStateFlow()
    private val api = ApiClient.getApiInstance()

    fun getBlogs() {
        viewModelScope.launch {
            _blogs.value = BlogResponse(blogDao.getBlogs())
            if (blogs.value.data.isEmpty()) {
                fetchBlogs()
            }
        }
    }

    fun fetchBlogs() {
        viewModelScope.launch {
            try {
                val response = api.getBlogs("Bearer ${prefManager.getString(PrefManager.Key.TOKEN)}")
                response.data.map { it.created_at = formatDate(it.created_at) }
                _blogs.value = response
                blogDao.replaceBlogs(response.data)
            } catch (e: IOException) {
                Log.e("api", "Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("api", "HTTP error: ${e.message()}")
            } catch (e: Exception) {
                Log.e("api", "Unexpected error: ${e.message}")
            }
        }
    }

    fun getBlogById(id: String, callback: (BlogData) -> Unit) {
        viewModelScope.launch {
            callback(blogDao.getBlog(id))
        }
        AnalyticsHelper.logFeature("read_blog")
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }
}