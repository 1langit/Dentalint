package com.cemerlang.dentalint.presentation.ui.notes

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.notes.NotesRequest
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent
import com.cemerlang.dentalint.presentation.ui.notes.components.SectionTitleComponent
import com.google.firebase.analytics.FirebaseAnalytics

@Composable
fun NotesAddScreen(
    onBackClick: () -> Unit = {},
    viewModel: NotesViewModel = hiltViewModel()
) {

    var foodBeverages by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isMorningSelected by remember { mutableStateOf(false) }
    var isNightSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Add Note",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Date: August 08, 2024")
            ToothBrushing(
                state = listOf(isMorningSelected, isNightSelected),
                onStateChange = listOf({ isMorningSelected = it }, { isNightSelected = it })
            )
            NoteInput(
                title = "Food and Beverages",
                icon = Icons.Outlined.FoodBank,
                input = foodBeverages,
                onInputChange = { foodBeverages = it }
            )
            NoteInput(
                title = "Note",
                icon = Icons.AutoMirrored.Outlined.Notes,
                input = note,
                onInputChange = { note = it }
            )
            Button(
                onClick = {
                    viewModel.addNotes(
                        NotesRequest(
                            fnb = foodBeverages,
                            note = note.ifBlank { null },
                            times = listOf(
                                if (isMorningSelected) "morning" else null,
                                if (isNightSelected) "night" else null
                            ).filterNotNull()
                        )
                    ) { result ->
                        if (result.isSuccess) {
                            onBackClick()
                            Toast.makeText(context, "Catatan disimpan", Toast.LENGTH_SHORT).show()
                            val analytics = FirebaseAnalytics.getInstance(context)
                            analytics.logEvent("add_notes", null)
                        } else {
                            Toast.makeText(context, "${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun ToothBrushing(state: List<Boolean>, onStateChange: List<(Boolean) -> Unit>) {
    Column {
        SectionTitleComponent(
            title = "Brushing Teeth",
            iconPainter = painterResource(R.drawable.ic_toothbrush)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterChip(
                selected = state[0],
                onClick = { onStateChange[0](!state[0]) },
                label = {
                    Text(
                        text = "Morning",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.WbSunny,
                        contentDescription = "morning",
                        tint = Color(0xFFAEA700)
                    )
                }
            )
            FilterChip(
                selected = state[1],
                onClick = { onStateChange[1](!state[1]) },
                label = {
                    Text(
                        text = "Night",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.NightsStay,
                        contentDescription = "night",
                        tint = Color(0xFFAEA700)
                    )
                }
            )
        }
    }
}

@Composable
fun NoteInput(title: String, icon: ImageVector, input: String, onInputChange: (String) -> Unit) {
    Column {
        SectionTitleComponent(
            title = title,
            iconVector = icon
        )
        TextField(
            value = input,
            onValueChange = { onInputChange(it) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun AddNotesScreenPreview() {
    NotesAddScreen()
}