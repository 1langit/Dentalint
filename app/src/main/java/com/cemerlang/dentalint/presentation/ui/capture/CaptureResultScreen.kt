package com.cemerlang.dentalint.presentation.ui.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.capture.CaptureData
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun CaptureResultScreen(
    captureId: Int?,
    onBackClick: () -> Unit = {},
    viewModel: CaptureViewModel = hiltViewModel()
) {

    var capture by remember { mutableStateOf<CaptureData?>(null) }
    captureId?.let {
        LaunchedEffect(captureId) {
            viewModel.getCaptureById(captureId) { result ->
                capture = result
            }
        }
    }
    val scrollState = rememberScrollState()
    val defaultModifier = Modifier.padding(20.dp, 0.dp)

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Capture Result",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            capture?.let { cap ->
                AsyncImage(
                    model = cap.image,
                    placeholder = painterResource(R.drawable.img_preview),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = defaultModifier.fillMaxWidth()
                ) {
                    Text(
                        text = cap.created_at,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = cap.`class`.replaceFirstChar { it.uppercase() },
                        color = if (cap.`class` == "healthy") Color(0xFF568F55) else Color.Red,
                    )
                }
                if (cap.result.contains("\n\n")) {
                    Column(modifier = defaultModifier) {
                        Text(
                            text = "Indicators",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.outline
                        )
                        IndicatorItem(name = "Healthy", shapeColor = Color.Green)
                        IndicatorItem(name = "Initial-Caries", shapeColor = Color.Yellow)
                        IndicatorItem(name = "Moderate-Caries", shapeColor = Color(0xFFFFA500))
                        IndicatorItem(name = "Extensive-Caries", shapeColor = Color.Red)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    ResultParagraph(
                        title = "Description",
                        content = cap.result.split("\n\n")[0],
                        modifier = defaultModifier
                    )
                    ResultParagraph(
                        title = "Recommendation",
                        content = cap.result.split("\n\n")[1],
                        modifier = defaultModifier
                    )
                } else {
                    Text(
                        text = cap.result,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Justify,
                        lineHeight = 20.sp,
                        modifier = defaultModifier
                    )
                }
            }
        }
    }
}

@Composable
fun IndicatorItem(name: String, shapeColor: Color, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(shapeColor))
        Text(
            text = name,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun ResultParagraph(title: String, content: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Justify,
            lineHeight = 20.sp,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun CaptureResultScreenPreview() {
    CaptureResultScreen(0)
}