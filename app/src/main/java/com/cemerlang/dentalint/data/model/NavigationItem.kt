package com.cemerlang.dentalint.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

//data class NavigationItem(
//    val label: String,
//    val icon: Painter
//)

data class NavigationItem(
    val label: String,
    val screen: @Composable() (() -> Unit)? = null,
    val icon: Pair<Painter, Painter>
//    val icon: Pair<ImageVector, ImageVector>
)