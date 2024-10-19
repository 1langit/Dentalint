package com.cemerlang.dentalint.presentation.ui.chatbot

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cemerlang.dentalint.BuildConfig
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import com.cemerlang.dentalint.data.PrefManager
import com.cemerlang.dentalint.data.local.ChatMessageDao
import com.cemerlang.dentalint.data.model.chatbot.ChatMessage
import com.cemerlang.dentalint.data.model.chatbot.Participant
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val prefManager: PrefManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            chatMessageDao.getMessages().collect {
                _messages.value = it
                if (messages.value.isEmpty()) {
                    addInitialMessage()
                }
            }
        }
        AnalyticsHelper.logFeature("chatbot")
    }

    private val generativeModel = GenerativeModel(
        apiKey = BuildConfig.GEMINI_API_KEY,
        modelName = "gemini-1.5-flash",
        systemInstruction = content { text(context.getString(R.string.chat_instruction)) },
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
        )
    )

    private val chat = generativeModel.startChat(
        history = messages.value.map {
            content(role = it.participant.name) { text(it.text) }
        }
    )

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatMessageDao.insertMessage(ChatMessage(text = message))
            _isLoading.value = true
            try {
                val response = chat.sendMessage(message)
                response.text?.let {
                    chatMessageDao.insertMessage(
                        ChatMessage(text = it, participant = Participant.MODEL)
                    )
                    _isLoading.value = false
                }
            } catch (e: IOException) {
                chatMessageDao.insertMessage(
                    ChatMessage(
                        text = "Network error",
                        participant = Participant.ERROR
                    )
                )
                _isLoading.value = false
            } catch (e: Exception) {
                chatMessageDao.insertMessage(
                    ChatMessage(
                        text = e.localizedMessage ?: e.message ?: "Something went wrong",
                        participant = Participant.ERROR
                    )
                )
                _isLoading.value = false
            }
        }
    }

    fun resetChat() {
        chat.history.clear()
        viewModelScope.launch {
            chatMessageDao.deleteMessages()
//            addInitialMessage()
        }
    }

    private suspend fun addInitialMessage() {
        chatMessageDao.insertMessage(
            ChatMessage(
                text = context.getString(R.string.chat_greeting, prefManager.getString(PrefManager.Key.NAME)),
                participant = Participant.MODEL
            )
        )
    }
}