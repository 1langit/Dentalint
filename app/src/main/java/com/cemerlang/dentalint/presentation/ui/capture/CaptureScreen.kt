package com.cemerlang.dentalint.presentation.ui.capture

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.presentation.common.AlertDialogComponent
import java.io.File
import java.io.InputStream

@Composable
fun CaptureScreen(
    onAnalyzeComplete: (Int) -> Unit = {},
    onLoading: (Boolean) -> Unit = {},
//    isQuickCaptureNav: Boolean = false,
    viewModel: CaptureViewModel = hiltViewModel()
) {

    var imageFile by remember { mutableStateOf<File?>(null) }
    val context = LocalContext.current
    val openInstructionDialog = remember { mutableStateOf(false) }

    // Temporary file to store the image from camera
    val tempImageFile = remember { createTempImageFile(context) }

    // Camera picker
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            imageFile = tempImageFile // Store the temporary file
        }
    }

    // Gallery picker
    val getImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageFile = uriToFile(context, it) // Convert Uri to File
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(FileProvider.getUriForFile(context, "${context.packageName}.provider", tempImageFile))
        } else {
            // Handle the case when permission is denied
        }
    }

    // Check permissions
    val hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

//    var isQuickAction by remember { mutableStateOf(isQuickCaptureNav) }

//    LaunchedEffect(isQuickCaptureNav) {
//        if (isQuickCaptureNav) {
//            if (hasCameraPermission) {
//                takePictureLauncher.launch(FileProvider.getUriForFile(context, "${context.packageName}.provider", tempImageFile))
//            } else {
//                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//            }
//            isQuickAction = false
//        }
//    }

    val isLoading by viewModel.isLoading.collectAsState()
    onLoading(isLoading)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(28.dp, 0.dp)
        ) {
            if (imageFile != null) {
                Text(
                    text = "Preview image",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val imageBitmap = imageFile?.let { imageFromFile(it) }
                imageBitmap?.let {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alpha = if (isLoading) 0.5f else 1f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 280.dp)
                                .padding(24.dp, 0.dp)
                        )
                        if (isLoading) {
                            CircularProgressIndicator()
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Instruction",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    IconButton(
                        onClick = { openInstructionDialog.value = true },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = "instruction"
                        )
                    }
                }
            } else {
                Text(
                    text = "Instruction",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Take a photo of your teeth from the camera or by uploading a photo like the following example",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(R.drawable.img_sample),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(28.dp))
            if (imageFile != null) {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Analyzing...",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.newCapture(imageFile!!) { result ->
                            if (result.isSuccess) {
                                result.getOrNull()?.id?.let { onAnalyzeComplete(it) }
                                imageFile = null
                            } else {
                                Toast.makeText(
                                    context,
                                    "${result.exceptionOrNull()?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Analyze")
                }
                OutlinedButton(
                    onClick = { imageFile = null },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Delete photo")
                }
            } else {
                Button(
                    onClick = {
                        if (hasCameraPermission) {
                            takePictureLauncher.launch(FileProvider.getUriForFile(context, "${context.packageName}.provider", tempImageFile))
                        } else {
                            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Take photo")
                }
                OutlinedButton(
                    onClick = {
                        getImageLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Upload photo from files")
                }
            }
        }
    }

    if (openInstructionDialog.value) {
        AlertDialogComponent(
            title = "Instruction",
            body = "Take a photo of your teeth from the camera or by uploading a photo like the following example",
            image = painterResource(R.drawable.img_sample),
            dismissText = "Close",
            onDismissRequest = { openInstructionDialog.value = false }
        )
    }
}

@Composable
fun imageFromFile(file: File): ImageBitmap? {
    return remember(file) {
        try {
            BitmapFactory.decodeFile(file.absolutePath).asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}

fun createTempImageFile(context: Context): File {
    return File.createTempFile("temp_image", ".jpg", context.cacheDir).apply {
        deleteOnExit() // Ensures the file gets deleted later
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("gallery_image", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}

@Preview(showBackground = true)
@Composable
fun CaptureScreenPreview() {
    CaptureScreen()
}