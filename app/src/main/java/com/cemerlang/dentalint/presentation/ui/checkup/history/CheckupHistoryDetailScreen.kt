package com.cemerlang.dentalint.presentation.ui.checkup.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent
import com.cemerlang.dentalint.presentation.ui.checkup.components.AppointmentDetailComponent

@Composable
fun CheckupHistoryDetailScreen(appointment: AppointmentData, onBackClick: () -> Unit = {}) {

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Detail History",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        AppointmentDetailComponent(
            appointment = appointment,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
private fun CheckupHistoryDetailScreenPreview() {
//    CheckupHistoryDetailScreen()
}