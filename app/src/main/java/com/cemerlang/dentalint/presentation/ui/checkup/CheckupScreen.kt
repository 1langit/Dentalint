package com.cemerlang.dentalint.presentation.ui.checkup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.presentation.common.HorizontalCardComponent
import com.cemerlang.dentalint.presentation.theme.DentalintTheme

@Composable
fun CheckupScreen(
    viewModel: CheckupViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit = {}
) {

    viewModel.getClinics()
    val clinics by viewModel.clinics.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 0.dp)
    ) {
        items(clinics.data.size) {
            HorizontalCardComponent(
                image = clinics.data[it].logo,
                cropImage = false,
                subtitle = "300 m",
                title = clinics.data[it].clinic_name,
                content = clinics.data[it].address
            ) {
                onItemClick(clinics.data[it].id)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckupScreenPreview() {
    CheckupScreen()
}