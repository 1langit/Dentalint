package com.cemerlang.dentalint.presentation.ui.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun NotificationScreen(onBackClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Notification",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
//        LazyColumn(modifier = Modifier.padding(innerPadding)) {
//            items(5) {
//                NotificationCard()
//            }
//        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
//                text = "No new notifications.\nYou’re all caught up!",
                text = "Tidak ada notifikasi baru",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun NotificationCard() {
    Card(
        onClick = { /*TODO*/ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 8.dp)
        ) {
            Text(
                text = "Let's fill in your daily notes!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Today you haven't filled in your daily notes☹\uFE0F",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    NotificationScreen()
}