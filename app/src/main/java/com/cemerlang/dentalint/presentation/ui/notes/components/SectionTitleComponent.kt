package com.cemerlang.dentalint.presentation.ui.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitleComponent(title: String, iconVector: ImageVector? = null, iconPainter: Painter? = null) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconVector?.let { Icon(imageVector = it, contentDescription = null) }
        iconPainter?.let { Icon(painter = it, contentDescription = null) }
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold
        )
    }
}