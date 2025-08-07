package com.jachimsevero.mqttclient.presentation.mainscreen

import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus
import com.jachimsevero.mqttclient.presentation.MviEffect
import com.jachimsevero.mqttclient.presentation.MviEvent
import com.jachimsevero.mqttclient.presentation.MviState

interface MainContract {
  data class State(
      val connectionStatus: MqttConnectionStatus = MqttConnectionStatus.Disconnected,
      val messages: List<String> = emptyList(),
      val isSaving: Boolean = false,
  ) : MviState

  sealed class Event : MviEvent {
    data object OnReconnectClicked : Event()
  }

  sealed class Effect : MviEffect {
    data class ShowError(val message: String) : Effect()
  }
}
