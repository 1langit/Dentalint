package com.cemerlang.dentalint.presentation.ui.checkup.patient

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.checkup.PatientData
import com.cemerlang.dentalint.data.model.checkup.PatientRequest
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent
import com.cemerlang.dentalint.presentation.ui.checkup.CheckupViewModel
import com.cemerlang.dentalint.presentation.ui.checkup.components.DateTextFieldComponent
import com.cemerlang.dentalint.presentation.ui.checkup.components.DropdownComponent

@Composable
fun PatientRegisterScreen(
    onBackClick: () -> Unit = {},
    onRegisterPatientSuccess: (PatientData) -> Unit = {},
    onRegisterAppointmentClick: () -> Unit = {},
    viewModel: CheckupViewModel
) {

    val clinic by viewModel.selectedClinic.collectAsState()
    var form by remember {
        mutableStateOf(
            PatientRequest(
                clinic_id = clinic.id,
                name = "",
                nik = "",
                jenis_kelamin = "",
                golongan_darah = "",
                tempat_lahir = "",
                tanggal_lahir = "",
                alamat = "",
                no_hp = ""
            )
        )
    }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Register New Patient",
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
                text = "Register New Patient Form",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = form.name,
                onValueChange = { form = form.copy(name = it) },
                label = { Text(text = "Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.nik,
                onValueChange = { form = form.copy(nik = it) },
                label = { Text(text = "NIK") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            DropdownComponent(
                label = "Gender",
                value = form.jenis_kelamin,
                onValueChange = { form = form.copy(jenis_kelamin = it) },
                options = listOf("Laki-laki", "Perempuan"),
                optionValues = listOf("L", "P")
            )
            DropdownComponent(
                label = "Blood Type",
                value = form.golongan_darah,
                onValueChange = { form = form.copy(golongan_darah = it) },
                options = listOf("A", "B", "AB", "O"),
                optionValues = listOf("A", "B", "AB", "O")
            )
            OutlinedTextField(
                value = form.tempat_lahir,
                onValueChange = { form = form.copy(tempat_lahir = it) },
                label = { Text(text = "Place of Birth") },
                modifier = Modifier.fillMaxWidth()
            )
            DateTextFieldComponent(
                label = "Date of Birth",
                value = form.tanggal_lahir,
                onValueChange = { form = form.copy(tanggal_lahir = it) }
            )
            OutlinedTextField(
                value = form.alamat,
                onValueChange = { form = form.copy(alamat = it) },
                label = { Text(text = "Address") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.no_hp,
                onValueChange = { form = form.copy(no_hp = it) },
                label = { Text(text = "Phone Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Button(
                    onClick = {
                        viewModel.registerPatient(form) { result ->
                            if (result.isSuccess && result.getOrNull() != null) {
                                onRegisterPatientSuccess(result.getOrNull()!!)
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
                    onClick = { onRegisterAppointmentClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Have a medical record number?")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PatientRegisterScreenPreview() {
//    PatientRegisterScreen()
}