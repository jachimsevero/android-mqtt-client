package com.jachimsevero.mqttclient.presentation.configscreen

import com.jachimsevero.mqttclient.presentation.MviEffect
import com.jachimsevero.mqttclient.presentation.MviEvent
import com.jachimsevero.mqttclient.presentation.MviState

interface ConfigContract {
  data class State(
      val serverHost: ConfigUiField = ConfigUiField(),
      val serverPort: ConfigUiField = ConfigUiField(),
      val websocketPath: ConfigUiField = ConfigUiField(),
      val username: ConfigUiField = ConfigUiField(),
      val password: ConfigUiField = ConfigUiField(),
      val isFormValid: Boolean = false,
      val isSaving: Boolean = false,
  ) : MviState

  sealed class Event : MviEvent {
    data class OnServerHostChanged(val serverHost: String) : Event()

    data class OnServerPortChanged(val serverPort: String) : Event()

    data class OnWebsocketPathChanged(val websocketPath: String) : Event()

    data class OnUsernameChanged(val username: String) : Event()

    data class OnPasswordChanged(val password: String) : Event()

    data object OnSaveClicked : Event()
  }

  sealed class Effect : MviEffect {
    data object Saved : Effect()
  }
}
