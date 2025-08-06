package com.jachimsevero.mqttclient.presentation.configscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jachimsevero.mqttclient.R
import com.jachimsevero.mqttclient.presentation.configscreen.components.NumberInput
import com.jachimsevero.mqttclient.presentation.configscreen.components.PasswordInput
import com.jachimsevero.mqttclient.presentation.configscreen.components.TextInput
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun ConfigScreen(onSaved: () -> Unit) {
  val viewModel: ConfigViewModel = hiltViewModel()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.effect.collectLatest { effect ->
      when (effect) {
        ConfigContract.Effect.Saved -> onSaved()
      }
    }
  }

  Surface { ConfigScreenContent(state) { event -> viewModel.setEvent(event) } }
}

@Composable
private fun ConfigScreenContent(
    state: ConfigContract.State,
    action: (event: ConfigContract.Event) -> Unit,
) {
  LazyColumn(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      TextInput(label = R.string.server_host, field = state.serverHost) {
        action(ConfigContract.Event.OnServerHostChanged(it))
      }
    }

    item {
      NumberInput(label = R.string.server_port, field = state.serverPort) {
        action(ConfigContract.Event.OnServerPortChanged(it))
      }
    }

    item {
      TextInput(label = R.string.websocket_path, field = state.websocketPath) {
        action(ConfigContract.Event.OnWebsocketPathChanged(it))
      }
    }

    item {
      TextInput(label = R.string.username, field = state.username) {
        action(ConfigContract.Event.OnUsernameChanged(it))
      }
    }

    item {
      PasswordInput(label = R.string.password, field = state.password) {
        action(ConfigContract.Event.OnPasswordChanged(it))
      }
    }

    item {
      Button(
          enabled = state.isFormValid && !state.isSaving,
          onClick = { action(ConfigContract.Event.OnSaveClicked) },
          modifier = Modifier.fillMaxWidth(),
      ) {
        Text(stringResource(R.string.save_amp_close))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenContentPreview() {
  MqttClientTheme {
    ConfigScreenContent(
        ConfigContract.State(
            serverHost = ConfigUiField(value = "mqqt.circletransit.com"),
            serverPort = ConfigUiField(value = "443"),
            websocketPath = ConfigUiField(value = "/mqtt/"),
            username = ConfigUiField(value = "abc"),
            password = ConfigUiField(value = "12345678"),
            isFormValid = true,
        )
    ) {}
  }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenContentPreview_InputValidationError() {
  MqttClientTheme {
    ConfigScreenContent(
        ConfigContract.State(
            serverHost = ConfigUiField(error = "Host required"),
            serverPort = ConfigUiField(error = "Port required"),
            websocketPath = ConfigUiField(error = "WebSocket path required"),
            username = ConfigUiField(error = "Username required"),
            password = ConfigUiField(error = "Password required"),
        )
    ) {}
  }
}
