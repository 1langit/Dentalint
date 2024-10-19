package com.cemerlang.dentalint.presentation.ui.checkup.patient

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.data.model.checkup.PatientData

@Composable
fun PatientRegisterSuccessScreen(patient: PatientData, onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Registration Success!",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Your Medical Record Number:",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = patient.rekam_medis,
            style = MaterialTheme.typography.titleLarge,
        )
        TextButton(
            onClick = {
                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = android.content.ClipData.newPlainText("mrdical record", patient.rekam_medis)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(context, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(text = "Copy")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { onBackClick() },
            modifier = Modifier.fillMaxWidth()
                .padding(28.dp, 0.dp)
        ) {
            Text(text = "Continue to Appointment")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PatientRegisterSuccessScreenPreview() {
//    PatientRegisterSuccessScreen()
}