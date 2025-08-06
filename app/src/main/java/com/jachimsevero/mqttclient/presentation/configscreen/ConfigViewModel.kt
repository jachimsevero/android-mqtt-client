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
            serverHost = ConfigUiField(mqttConfig.host),
            serverPort = ConfigUiField(mqttConfig.port.toString()),
            websocketPath = ConfigUiField(mqttConfig.websocketPath),
            username = ConfigUiField(mqttConfig.username),
            password = ConfigUiField(mqttConfig.password),
        )
      }
    }
  }

  override suspend fun handleEvent(event: ConfigContract.Event) {
    when (event) {
      is ConfigContract.Event.OnServerHostChanged ->
          updateAndValidate { setState { copy(serverHost = ConfigUiField(event.serverHost)) } }

      is ConfigContract.Event.OnServerPortChanged ->
          updateAndValidate { setState { copy(serverPort = ConfigUiField(event.serverPort)) } }

      is ConfigContract.Event.OnWebsocketPathChanged ->
          updateAndValidate {
            setState { copy(websocketPath = ConfigUiField(event.websocketPath)) }
          }

      is ConfigContract.Event.OnUsernameChanged ->
          updateAndValidate { setState { copy(username = ConfigUiField(event.username)) } }

      is ConfigContract.Event.OnPasswordChanged ->
          updateAndValidate { setState { copy(password = ConfigUiField(event.password)) } }

      is ConfigContract.Event.OnSaveClicked -> {
        saveConfig()
      }
    }
  }

  private fun updateAndValidate(transform: () -> Unit) {
    transform()
    setState { validate(state.value) }
  }

  private fun validate(state: ConfigContract.State): ConfigContract.State {
    val host = state.serverHost.value.trim()
    val port = state.serverPort.value.trim()
    val path = state.websocketPath.value.trim()
    val username = state.username.value.trim()
    val password = state.password.value.trim()

    val validatedHost = ConfigUiField(host, if (host.isBlank()) "Host required" else null)
    val validatedPort =
        ConfigUiField(
            port,
            port.toIntOrNull()?.let { if (it in 1..65535) null else "Invalid port range" },
        )

    val validatedPath = ConfigUiField(path, if (path.isBlank()) "WebSocket path required" else null)
    val validatedUsername =
        ConfigUiField(username, if (username.isBlank()) "Username required" else null)
    val validatedPassword =
        ConfigUiField(password, if (password.isBlank()) "Password required" else null)

    val isValid =
        listOf(validatedHost, validatedPort, validatedPath, validatedUsername, validatedPassword)
            .all { it.error == null }

    return state.copy(
        serverHost = validatedHost,
        serverPort = validatedPort,
        websocketPath = validatedPath,
        username = validatedUsername,
        password = validatedPassword,
        isFormValid = isValid,
    )
  }

  private fun saveConfig() =
      viewModelScope.launch(Dispatchers.IO) {
        try {
          setState { copy(isSaving = true) }
          val mqttConfig =
              MqttConfig.newBuilder()
                  .setHost(state.value.serverHost.value)
                  .setPort(state.value.serverPort.value.toInt())
                  .setWebsocketPath(state.value.websocketPath.value)
                  .setUsername(state.value.username.value)
                  .setPassword(state.value.password.value)
                  .build()
          mqttConfigStore.save(mqttConfig)
        } finally {
          setState { copy(isSaving = false) }
          setEffect { ConfigContract.Effect.Saved }
        }
      }
}
