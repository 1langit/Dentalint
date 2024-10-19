package com.cemerlang.dentalint.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.BlogDao
import com.cemerlang.dentalint.data.model.blog.BlogData
import com.cemerlang.dentalint.data.model.capture.CaptureEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val blogDao: BlogDao
) : ViewModel() {

//    private val _latestCapture = MutableStateFlow<CaptureEntity>()
//    val latestCapture: StateFlow<CaptureEntity> = _latestCapture.asStateFlow()

    private val _newestBlogs = MutableStateFlow<List<BlogData>>(emptyList())
    val newestBlogs: StateFlow<List<BlogData>> = _newestBlogs.asStateFlow()

    init {
        viewModelScope.launch {
            _newestBlogs.value = blogDao.getNewestBlogs()
        }
    }
}