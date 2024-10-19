package com.cemerlang.dentalint.presentation.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.presentation.common.AlertDialogComponent

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    viewModel: ProfileViewModel
) {

    val preferences by viewModel.preferences.collectAsState()
    val openThemeDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ProfileTopBar(
                onBackClick = { onBackClick() },
                onLogoutClick = {
                    onLogoutClick()
                    viewModel.logoutUser()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp, 8.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = preferences.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = preferences.email,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                TextButton(onClick = { onEditClick() }) {
                    Text(text = "Edit")
                }
            }
            HorizontalDivider(modifier = Modifier.padding(0.dp, 12.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Notification",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = preferences.notification,
                    onCheckedChange = { viewModel.updatePrefs(PrefManager.Key.NOTIFICATION, it) },
                    modifier = Modifier.height(36.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Language",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = preferences.language,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = { openThemeDialog.value = true },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = preferences.theme,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }

    if (openThemeDialog.value) {
        OptionDialog(
            value = preferences.theme,
            options = listOf("System default", "Light", "Dark"),
            onDismiss = { openThemeDialog.value = false },
            onOptionSelected = {
                viewModel.updatePrefs(PrefManager.Key.THEME, it)
                openThemeDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(onBackClick: () -> Unit, onLogoutClick: () -> Unit) {
    val openLogoutDialog = remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos, contentDescription = "back")
            }
        },
        actions = {
            IconButton(onClick = { openLogoutDialog.value = true }) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "logout")
            }
        }
    )

    if (openLogoutDialog.value) {
        AlertDialogComponent(
            title = "Logout from Dentalint?",
            body = "Your account will be logged out.",
            confirmText = "Logout",
            dismissText = "Cancel",
            onDismissRequest = { openLogoutDialog.value = false },
            onConfirmation = {
                openLogoutDialog.value = false
                onLogoutClick()
            }
        )
    }
}

@Composable
fun OptionDialog(
    value: String,
    options: List<String>,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Choose Theme",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                options.forEach { label ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (value == label),
                                onClick = { onOptionSelected(label) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = (value == label),
                            onClick = null,
                            modifier = Modifier.padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
//    ThemeDialog("Light", {}, {})
}