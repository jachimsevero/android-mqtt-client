package com.jachimsevero.mqttclient.presentation.configscreen

import com.jachimsevero.mqttclient.presentation.MviEffect
import com.jachimsevero.mqttclient.presentation.MviEvent
import com.jachimsevero.mqttclient.presentation.MviState

interface ConfigContract {
  data class State(
      val serverHost: String = "",
      val serverPort: String = "1883",
      val websocketPath: String = "",
      val username: String = "",
      val password: String = "",
      val topic: String = "",
      val receivedMessages: String = ""
  ) : MviState

  sealed class Event : MviEvent {
    data class OnServerHostChanged(val serverHost: String) : Event()

    data class OnServerPortChanged(val serverPort: String) : Event()

    data class OnWebsocketPathChanged(val websocketPath: String) : Event()

    data class OnUsernameChanged(val username: String) : Event()

    data class OnPasswordChanged(val password: String) : Event()

    data class OnTopicChanged(val topic: String) : Event()
  }

  sealed class Effect : MviEffect {
    data class ShowError(val message: String) : Effect()
  }
}
