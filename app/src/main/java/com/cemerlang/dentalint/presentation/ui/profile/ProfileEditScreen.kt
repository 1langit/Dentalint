package com.cemerlang.dentalint.presentation.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cemerlang.dentalint.data.model.profile.UserUpdateRequest
import com.cemerlang.dentalint.presentation.common.CommonTopBarComponent

@Composable
fun ProfileEditScreen(
    onBackClick: () -> Unit = {},
    viewModel: ProfileViewModel
) {

    val preferences by viewModel.preferences.collectAsState()
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        name = preferences.name
    }

    Scaffold(
        topBar = {
            CommonTopBarComponent(
                title = "Edit Profile",
                onBackClick = { onBackClick() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp, 4.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(
                        text = "Full Name",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.updateUser(UserUpdateRequest(name)) { result ->
                        if (result.isSuccess) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        } else {
                            Toast.makeText(context, "${result.exceptionOrNull()}", Toast.LENGTH_SHORT).show()
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

@Preview
@Composable
private fun ProfileEditScreenPreview() {
//    ProfileEditScreen()
}