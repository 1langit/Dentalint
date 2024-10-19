package com.cemerlang.dentalint.presentation.ui.blog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.presentation.common.HorizontalCardComponent
import com.cemerlang.dentalint.presentation.ui.checkup.CheckupScreen

@Composable
fun BlogScreen(viewModel: BlogViewModel = hiltViewModel(), onItemClick: (String) -> Unit = {}) {

    LaunchedEffect(Unit) {
        viewModel.fetchBlogs()
    }
    viewModel.getBlogs()
    val blogs by viewModel.blogs.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 0.dp)
    ) {
        items(blogs.data.size) {
            HorizontalCardComponent(
                image = blogs.data[it].image,
                cropImage = true,
                subtitle = blogs.data[it].created_at,
                title = blogs.data[it].title,
                content = blogs.data[it].source
            ) {
                onItemClick(blogs.data[it].id)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlogScreenPreview() {
    BlogScreen()
}