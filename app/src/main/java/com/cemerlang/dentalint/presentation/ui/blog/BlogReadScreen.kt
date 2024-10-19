package com.cemerlang.dentalint.presentation.ui.blog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.blog.BlogData
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun BlogReadScreen(
    blogId: String?,
    onBackClick: () -> Unit,
    viewModel: BlogViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    var blog by remember { mutableStateOf<BlogData?>(null) }

    blogId?.let {
        LaunchedEffect(blogId) {
            viewModel.getBlogById(blogId) { result ->
                blog = result
//            isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Detail",
                onBackClick = { onBackClick() }
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = blog?.image,
                placeholder = painterResource(id = R.drawable.img_placeholder),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = blog?.title ?: "How to Brush Your Teeth",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(16.dp, 0.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = blog?.content ?: stringResource(R.string.lorem),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp, 0.dp)
            )
            Text(
                text = "Sumber: ${blog?.source}" ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
            )
            Text(
                text = blog?.created_at ?: "-",
                color = MaterialTheme.colorScheme.outline,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                ),
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlogReadScreenPreview() {
    BlogReadScreen("1", {})
}