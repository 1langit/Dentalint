package com.cemerlang.dentalint.presentation.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.notes.NotesData
import com.cemerlang.dentalint.presentation.ui.notes.components.BrushTime
import com.cemerlang.dentalint.presentation.ui.notes.components.BrushTimeComponent

@Composable
fun NotesScreen(
    onAddClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    viewModel: NotesViewModel = hiltViewModel()
) {

    viewModel.fetchNotes()
    val notes by viewModel.notes.collectAsState()

    Box {
        if (notes.data.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notes.data.size) {
                    NotesCard(notes.data[it]) {
                        onItemClick(notes.data[it].id)
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Tap ",
                    color = MaterialTheme.colorScheme.outline
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.NoteAdd,
                    contentDescription = "new capture",
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = " to add new note",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        FloatingActionButton(
            onClick = { onAddClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.NoteAdd,
                contentDescription = "new capture",
            )
        }
    }
}

@Composable
fun NotesCard(notes: NotesData, onClick: () -> Unit = {}) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp, 12.dp)
        ) {
            Text(
                text = notes.created_at,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.size(0.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_toothbrush),
                        contentDescription = "food",
                        modifier = Modifier.padding(6.dp)
                    )
                }
                if (notes.times.contains("morning")) {
                    BrushTimeComponent(time = BrushTime.MORNING)
                }
                Spacer(modifier = Modifier.size(0.dp))
                if (notes.times.contains("night")) {
                    BrushTimeComponent(time = BrushTime.NIGHT)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FoodBank,
                        contentDescription = "food",
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Text(
                    text = notes.fnb,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            notes.note?.let {
                Spacer(modifier = Modifier.size(0.dp))
                Text(
                    text = notes.note,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesScreen({}, {})
}