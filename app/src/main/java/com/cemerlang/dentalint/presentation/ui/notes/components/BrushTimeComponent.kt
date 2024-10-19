package com.cemerlang.dentalint.presentation.ui.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BrushTimeComponent(time: BrushTime) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = when (time) {
                BrushTime.MORNING -> Icons.Outlined.WbSunny
                BrushTime.NIGHT -> Icons.Outlined.NightsStay
            },
            contentDescription = time.name,
            tint = Color(0xFFAEA700)
        )
        Text(
            text = time.name.lowercase().replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

enum class BrushTime(time: String) {
    MORNING("morning"),
    NIGHT("night")
}