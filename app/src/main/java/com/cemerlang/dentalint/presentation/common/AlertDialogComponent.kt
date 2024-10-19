package com.cemerlang.dentalint.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.R

@Composable
fun AlertDialogComponent(
    title: String,
    body: String,
    confirmText: String? = null,
    dismissText: String,
    image: Painter? = null,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit = {}
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = body)
                image?.let {
                    Image(
                        painter = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            confirmText?.let {
                Button(
                    onClick = { onConfirmation() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = confirmText)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = dismissText)
            }
        }
    )
}

@Preview
@Composable
fun AlertDialogComponentPreview() {
    AlertDialogComponent(
        title = "Title",
        body = "body",
        image = painterResource(R.drawable.img_preview),
        confirmText = "Confirm",
        dismissText = "Cancel",
        onDismissRequest = {}
    )
}