package com.jachimsevero.mqttclient.presentation.mainscreen

import androidx.lifecycle.viewModelScope
import com.jachimsevero.mqttclient.presentation.BaseMVIViewModel
import com.jachimsevero.mqttclient.service.MqttService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mqttService: MqttService) :
    BaseMVIViewModel<MainContract.State, MainContract.Event, MainContract.Effect>(
        MainContract.State()
    ) {

  init {
    observeConnection()
    observeMessages()
  }

  override suspend fun handleEvent(event: MainContract.Event) {
    when (event) {
      is MainContract.Event.OnReconnectClicked -> reconnect()
    }
  }

  private fun observeConnection() {
    viewModelScope.launch {
      mqttService.connectionStatus.collect { status ->
        setState { copy(connectionStatus = status) }
      }
    }
  }

  private fun observeMessages() {
    viewModelScope.launch {
      mqttService.receivedMessages.collect { message ->
        setState { copy(messages = messages + message) }
      }
    }
  }

  private fun reconnect() =
      viewModelScope.launch(Dispatchers.IO) {
        try {
          mqttService.stop()
        } catch (e: Exception) {
          setEffect { MainContract.Effect.ShowError(e.message ?: "Unknown error") }
        }

        try {
          mqttService.start()
        } catch (e: Exception) {
          setEffect { MainContract.Effect.ShowError(e.message ?: "Unknown error") }
        }
      }
}
