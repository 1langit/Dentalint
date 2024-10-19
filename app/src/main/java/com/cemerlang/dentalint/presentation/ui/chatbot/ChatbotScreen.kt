package com.cemerlang.dentalint.presentation.ui.chatbot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cemerlang.dentalint.data.model.chatbot.Participant
import com.cemerlang.dentalint.presentation.common.AlertDialogComponent
import com.cemerlang.dentalint.presentation.theme.DentalintTheme

@Composable
fun ChatbotScreen(navController: NavController, viewModel: ChatbotViewModel = hiltViewModel()) {

    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = { ChatTopBar(navController, viewModel) },
        bottomBar = { ChatBottomBar(viewModel) }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(messages.size) {
                if (messages[it].participant == Participant.USER) {
                    ChatRight(messages[it].text)
                } else {
                    ChatLeft(messages[it].text, messages[it].participant == Participant.ERROR)
                }
            }
            if (isLoading) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 20.dp),
                        modifier = Modifier.padding(8.dp, 4.dp, 60.dp, 4.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }

        LaunchedEffect(messages) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(navController: NavController, viewModel: ChatbotViewModel) {
    val openResetDialog = remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Chatbot",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos, contentDescription = "back")
            }
        },
        actions = {
            IconButton(onClick = { openResetDialog.value = true }) {
                Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "restart")
            }
        }
    )

    if (openResetDialog.value) {
        AlertDialogComponent(
            title = "Reset Chat?",
            body = "Your chat history with Denta will be erased.",
            confirmText = "Reset",
            dismissText = "Cancel",
            onDismissRequest = { openResetDialog.value = false },
            onConfirmation = {
                openResetDialog.value = false
                viewModel.resetChat()
            }
        )
    }
}

@Composable
fun ChatBottomBar(viewModel: ChatbotViewModel) {

    var input by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    BottomAppBar(
        containerColor = Color.Transparent
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Type message") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            ),
            trailingIcon = {
                FilledIconButton(
                    onClick = {
                        if (input.isNotBlank() && !isLoading) {
                            viewModel.sendMessage(input)
                            input = ""
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp),
                    enabled = !isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            maxLines = 6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 8.dp)
        )
    }
}

@Composable
fun ChatRight(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(20.dp, 0.dp, 20.dp, 20.dp),
            modifier = Modifier.padding(60.dp, 4.dp, 8.dp, 4.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun ChatLeft(message: String, isError: Boolean = false) {
    Surface(
        color = if (!isError) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 20.dp),
        modifier = Modifier.padding(8.dp, 4.dp, 60.dp, 4.dp)
    ) {
        Text(
            text = message.trim(),
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotScreenPreview() {
    DentalintTheme {
        ChatbotScreen(rememberNavController())
    }
}