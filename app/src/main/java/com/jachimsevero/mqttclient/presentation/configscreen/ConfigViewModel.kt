package com.jachimsevero.mqttclient.presentation.configscreen

import androidx.lifecycle.viewModelScope
import com.jachimsevero.mqttclient.MqttConfig
import com.jachimsevero.mqttclient.data.local.MqttConfigStore
import com.jachimsevero.mqttclient.presentation.BaseMVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(private val mqttConfigStore: MqttConfigStore) :
    BaseMVIViewModel<ConfigContract.State, ConfigContract.Event, ConfigContract.Effect>(
        ConfigContract.State()
    ) {

  init {
    viewModelScope.launch(Dispatchers.IO) {
      val mqttConfig = mqttConfigStore.get().first()
      setState {
        copy(
            serverHost = mqttConfig.host,
            serverPort = mqttConfig.port.toString(),
            websocketPath = mqttConfig.websocketPath,
            username = mqttConfig.username,
            password = mqttConfig.password,
        )
      }
    }
  }

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
      is ConfigContract.Event.OnSaveClicked -> {
        saveConfig()
      }
    }
  }

  private fun saveConfig() =
      viewModelScope.launch(Dispatchers.IO) {
        val mqttConfig =
            MqttConfig.newBuilder()
                .setHost(state.value.serverHost)
                .setPort(state.value.serverPort.toInt())
                .setWebsocketPath(state.value.websocketPath)
                .setUsername(state.value.username)
                .setPassword(state.value.password)
                .build()
        mqttConfigStore.save(mqttConfig)
      }
}
