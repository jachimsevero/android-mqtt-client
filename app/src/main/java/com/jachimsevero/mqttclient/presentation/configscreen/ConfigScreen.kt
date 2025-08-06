package com.jachimsevero.mqttclient.presentation.configscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jachimsevero.mqttclient.R
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme

@Composable
internal fun ConfigScreen() {
  val viewModel: ConfigViewModel = hiltViewModel()
  val state by viewModel.state.collectAsStateWithLifecycle()

  Surface { ConfigScreenContent(state) { event -> viewModel.setEvent(event) } }
}

@Composable
private fun ConfigScreenContent(
    state: ConfigContract.State,
    action: (event: ConfigContract.Event) -> Unit,
) {
  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    OutlinedTextField(
        value = state.serverHost,
        onValueChange = { action(ConfigContract.Event.OnServerHostChanged(it)) },
        label = { Text(stringResource(R.string.server_host)) },
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = state.serverPort,
        onValueChange = { action(ConfigContract.Event.OnServerPortChanged(it)) },
        label = { Text(stringResource(R.string.server_port)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    OutlinedTextField(
        value = state.websocketPath,
        onValueChange = { action(ConfigContract.Event.OnWebsocketPathChanged(it)) },
        label = { Text(stringResource(R.string.websocket_path)) },
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = state.username,
        onValueChange = { action(ConfigContract.Event.OnUsernameChanged(it)) },
        label = { Text(stringResource(R.string.username)) },
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = state.password,
        onValueChange = { action(ConfigContract.Event.OnPasswordChanged(it)) },
        label = { Text(stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
    )

    Button(
        onClick = { action(ConfigContract.Event.OnSaveClicked) },
        modifier = Modifier.fillMaxWidth(),
    ) {
      Text(stringResource(R.string.save))
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenContentPreview() {
  MqttClientTheme { ConfigScreenContent(ConfigContract.State()) {} }
}
