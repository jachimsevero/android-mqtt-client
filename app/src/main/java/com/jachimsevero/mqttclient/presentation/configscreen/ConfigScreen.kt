package com.jachimsevero.mqttclient.presentation.configscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
      verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = state.serverHost,
            onValueChange = { action(ConfigContract.Event.OnServerHostChanged(it)) },
            label = { Text(stringResource(R.string.server_host)) },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = state.serverPort,
            onValueChange = { action(ConfigContract.Event.OnServerPortChanged(it)) },
            label = { Text(stringResource(R.string.server_port)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(
            value = state.websocketPath,
            onValueChange = { action(ConfigContract.Event.OnWebsocketPathChanged(it)) },
            label = { Text(stringResource(R.string.websocket_path)) },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = state.username,
            onValueChange = { action(ConfigContract.Event.OnUsernameChanged(it)) },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = state.password,
            onValueChange = { action(ConfigContract.Event.OnPasswordChanged(it)) },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = state.topic,
            onValueChange = { action(ConfigContract.Event.OnTopicChanged(it)) },
            label = { Text(stringResource(R.string.topic)) },
            modifier = Modifier.fillMaxWidth())

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
          Text(stringResource(R.string.connect))
        }

        Text(
            text = stringResource(R.string.received_messages),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp))

        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .height(150.dp)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())) {
              Text(text = "receivedMessages")
            }
      }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenContentPreview() {
  MqttClientTheme { ConfigScreenContent(ConfigContract.State()) {} }
}
