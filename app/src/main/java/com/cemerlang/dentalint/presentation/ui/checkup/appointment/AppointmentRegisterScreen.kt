package com.cemerlang.dentalint.presentation.ui.checkup.appointment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.checkup.AppointmentData
import com.cemerlang.dentalint.data.model.checkup.AppointmentRequest
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent
import com.cemerlang.dentalint.presentation.ui.checkup.CheckupViewModel
import com.cemerlang.dentalint.presentation.ui.checkup.components.DateTextFieldComponent
import com.cemerlang.dentalint.presentation.ui.checkup.components.DropdownComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentRegisterScreen(
    onBackClick: () -> Unit = {},
    onRegisterAppointmentSuccess: (AppointmentData) -> Unit = {},
    onRegisterPatientClick: () -> Unit = {},
    viewModel: CheckupViewModel
) {

    val clinic by viewModel.selectedClinic.collectAsState()
    var form by remember {
        mutableStateOf(
            AppointmentRequest(
                rekam_medis = "",
                clinic_id = 0,
                schedule = "",
                status = "",
                polyclinic = "",
                payment = ""
            )
        )
    }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(clinic) {
        form = form.copy(clinic_id = clinic.id)
    }

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Register an Appointment",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(20.dp, 8.dp)
        ) {
            Text(
                text = "Selected Clinic",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    AsyncImage(
                        model = clinic.logo,
                        placeholder = painterResource(R.drawable.img_placeholder),
                        contentDescription = clinic.clinic_name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            text = clinic.clinic_name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = clinic.address,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(0.dp))
            Text(
                text = "Register an Appointment Form",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = form.rekam_medis,
                onValueChange = { form = form.copy(rekam_medis = it) },
                label = { Text(text = "Medical Record Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
//            OutlinedTextField(
//                value = viewModel.getUsername(),
//                onValueChange = {},
//                label = { Text(text = "Name") },
//                colors = OutlinedTextFieldDefaults.colors(
//                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
//                    disabledBorderColor = MaterialTheme.colorScheme.secondary
//                ),
//                readOnly = true,
//                enabled = false,
//                singleLine = true,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusable(false)
//            )
            DropdownComponent(
                label = "Queue Scheduling",
                value = form.status,
                onValueChange = { form = form.copy(status = it) },
                options = listOf("Penjadwalan baru", "Penjadwalan ulang"),
                optionValues = listOf("new", "reschedule")
            )
            DateTextFieldComponent(
                label = "Queue Date and Time",
                value = form.schedule,
                onValueChange = { form = form.copy(schedule = it) }
            )
            DropdownComponent(
                label = "Policlinic",
                value = form.polyclinic,
                onValueChange = { form = form.copy(polyclinic = it) },
                options = listOf("Poliklinik 1", "Poliklinik 2"),
                optionValues = listOf("Poliklinik 1", "Poliklinik 2")
            )
            DropdownComponent(
                label = "Payment Type",
                value = form.payment,
                onValueChange = { form = form.copy(payment = it) },
                options = listOf("Bayar sendiri", "BPJS", "Lain-lain"),
                optionValues = listOf("Bayar sendiri", "BPJS", "Lain-lain")
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Button(
                    onClick = {
                        viewModel.registerAppointment(form) { result ->
                            if (result.isSuccess && result.getOrNull() != null) {
                                onRegisterAppointmentSuccess(result.getOrNull()!!)
                            } else {
                                Toast.makeText(
                                    context,
                                    result.exceptionOrNull().toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Register")
                }
                TextButton(
                    onClick = { onRegisterPatientClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Don't have a medical record number?")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppointmentRegisterScreenPreview() {
//    AppointmentRegisterScreen()
}