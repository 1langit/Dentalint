package com.cemerlang.dentalint.presentation.ui.capture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.capture.CaptureData
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun CaptureHistoryScreen(
    onBackClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    viewModel: CaptureViewModel = hiltViewModel()
) {
    viewModel.fetchCaptures()
    val captures by viewModel.captures.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Capture History",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp, 0.dp)
        ) {
            items(captures.data.size) {
                CaptureCard(captures.data[it]) {
                    onItemClick(captures.data[it].id)
                }
            }
        }
    }
}

@Composable
fun CaptureCard(capture: CaptureData, onClick: () -> Unit = {}) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = capture.image,
                placeholder = painterResource(id = R.drawable.img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = capture.`class`.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (capture.`class` == "healthy") Color(0xFF568F55) else Color.Red
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = (capture.created_at),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = capture.result,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CaptureHistoryScreenPreview() {
    CaptureHistoryScreen()
}