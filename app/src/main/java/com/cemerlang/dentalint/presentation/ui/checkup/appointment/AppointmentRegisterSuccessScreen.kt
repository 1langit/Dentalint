package com.cemerlang.dentalint.presentation.ui.checkup.appointment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.presentation.ui.checkup.components.AppointmentDetailComponent

@Composable
fun AppointmentRegisterSuccessScreen(appointment: AppointmentData, onBackClick: () -> Unit = {}) {
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Registration Success!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            AppointmentDetailComponent(appointment = appointment, highlightQueue = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onBackClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            ) {
                Text(text = "Back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppointmentRegisterSuccessScreenPreview() {
//    AppointmentRegisterSuccessScreen()
}