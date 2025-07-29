package com.jachimsevero.mqttclient.presentation.configscreen

import com.jachimsevero.mqttclient.presentation.BaseMVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor() :
    BaseMVIViewModel<ConfigContract.State, ConfigContract.Event, ConfigContract.Effect>(
        ConfigContract.State()
    ) {

  override suspend fun handleEvent(event: ConfigContract.Event) {
    when (event) {
      is ConfigContract.Event.OnServerHostChanged ->
          setState { copy(serverHost = event.serverHost) }
      is ConfigContract.Event.OnServerPortChanged ->
          setState { copy(serverPort = event.serverPort) }
      is ConfigContract.Event.OnWebsocketPathChanged ->
          setState { copy(websocketPath = event.websocketPath) }
      is ConfigContract.Event.OnUsernameChanged -> setState { copy(username = event.username) }
      is ConfigContract.Event.OnPasswordChanged -> setState { copy(password = event.password) }
      is ConfigContract.Event.OnTopicChanged -> setState { copy(topic = event.topic) }
    }
  }
}
