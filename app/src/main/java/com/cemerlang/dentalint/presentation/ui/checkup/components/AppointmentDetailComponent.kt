package com.cemerlang.dentalint.presentation.ui.checkup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.data.model.checkup.AppointmentData

@Composable
fun AppointmentDetailComponent(appointment: AppointmentData, modifier: Modifier = Modifier, highlightQueue: Boolean = false) {
    Column(
        modifier = modifier
            .padding(20.dp)
    ) {
        if (highlightQueue) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your Queue Number:",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = appointment.no_antrian,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            RowComponent(
                name = "Queue Number",
                value = appointment.no_antrian
            )
        }
        RowComponent(
            name = "Clinic",
            value = appointment.clinic.clinic_name
        )
        RowComponent(
            name = "Medical Record Number",
            value = appointment.rekam_medis
        )
        RowComponent(
            name = "Queue Scheduling",
            value = appointment.status
        )
        RowComponent(
            name = "Queue Date and Time ",
            value = appointment.schedule
        )
        RowComponent(
            name = "Policlinic",
            value = appointment.polyclinic
        )
        RowComponent(
            name = "Payment Type",
            value = appointment.payment
        )
    }
}

@Composable
fun RowComponent(name: String, value: String) {
    Text(
        text = name,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = value,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(20.dp))
}