package com.cemerlang.dentalint.presentation.ui.checkup.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.presentation.common.CheckupCardComponent
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun CheckupHistoryScreen(
    onBackClick: () -> Unit = {},
    onItemClick: () -> Unit = {},
    viewModel: CheckupHistoryViewModel
) {

    viewModel.getAppointments()
    val appointments by viewModel.appointments.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "History",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp, 0.dp)
        ) {
            items(appointments.data.size) {
                CheckupCardComponent(appointments.data[it]) {
                    viewModel.selectAppointment(appointments.data[it])
                    onItemClick()
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            }
        }
    }
}

@Preview
@Composable
private fun CheckupHistoryScreenPreview() {
//    CheckupHistoryScreen()
}