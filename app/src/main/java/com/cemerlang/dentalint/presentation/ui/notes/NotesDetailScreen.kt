package com.cemerlang.dentalint.presentation.ui.notes

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.notes.NotesData
import com.cemerlang.dentalint.notification.AlarmItem
import com.cemerlang.dentalint.notification.Scheduler
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent
import com.cemerlang.dentalint.presentation.ui.notes.components.BrushTime
import com.cemerlang.dentalint.presentation.ui.notes.components.BrushTimeComponent
import com.cemerlang.dentalint.presentation.ui.notes.components.SectionTitleComponent
import java.time.LocalDateTime

@Composable
fun NotesDetailScreen(
    noteId: String?,
    onBackClick: () -> Unit = {},
    viewModel: NotesViewModel = hiltViewModel()
) {

//    val context = LocalContext.current
//    var hasNotificationPermission by remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        } else mutableStateOf(true)
//    }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            hasNotificationPermission = isGranted
//        }
//    )
//
//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
//        }
//    }

    var notes by remember { mutableStateOf<NotesData?>(null) }
    noteId?.let {
        LaunchedEffect(noteId) {
            viewModel.getNoteById(noteId) { result ->
                notes = result
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Notes Detail",
                onBackClick = {
                    onBackClick()
//                    if (hasNotificationPermission) {
//                        val notification = NotificationCompat.Builder(context, "notes")
//                            .setSmallIcon(R.drawable.ic_launcher_foreground)
//                            .setContentTitle("Hello world")
//                            .setContentText("This is a description")
//                            .build()
//                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                        notificationManager.notify(1, notification)
//                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = notes?.created_at ?: "")
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionTitleComponent(
                    title = "Detail",
                    iconPainter = painterResource(R.drawable.ic_toothbrush)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (notes?.times?.contains("morning") == true) {
                        BrushTimeComponent(time = BrushTime.MORNING)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    if (notes?.times?.contains("night") == true) {
                        BrushTimeComponent(time = BrushTime.NIGHT)
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitleComponent(
                    title = "Food and Beverages",
                    iconVector = Icons.Outlined.FoodBank
                )
                Text(text = notes?.fnb ?: "")
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitleComponent(
                    title = "Note",
                    iconVector = Icons.AutoMirrored.Outlined.Notes
                )
                Text(text = notes?.note ?: "")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesDetailScreenPreview() {
    NotesDetailScreen("0")
}