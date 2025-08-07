package com.jachimsevero.mqttclient.presentation.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jachimsevero.mqttclient.R
import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun MainScreen(viewModel: MainViewModel = hiltViewModel(), onSettingsClicked: () -> Unit) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(Unit) {
    viewModel.effect.collectLatest { effect ->
      when (effect) {
        is MainContract.Effect.ShowError -> {
          snackbarHostState.showSnackbar(message = effect.message, withDismissAction = true)
        }
      }
    }
  }

  MainScreenContent(
      state = state,
      action = { event -> viewModel.setEvent(event) },
      onSettingsClicked = onSettingsClicked,
      snackbarHostState = snackbarHostState,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    state: MainContract.State,
    action: (event: MainContract.Event) -> Unit,
    onSettingsClicked: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopAppBar(
            title = {
              Column {
                Text(stringResource(R.string.app_name))
                Text(
                    text = "Status: ${state.connectionStatus}",
                    style = MaterialTheme.typography.labelSmall,
                )
              }
            },
            actions = {
              if (state.connectionStatus == MqttConnectionStatus.Disconnected) {
                IconButton(onClick = { action(MainContract.Event.OnReconnectClicked) }) {
                  Icon(Icons.Default.Refresh, contentDescription = "Reconnect")
                }
              }

              IconButton(onClick = { onSettingsClicked() }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
              }
            },
        )
      },
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data -> Snackbar(snackbarData = data) }
      },
  ) { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
      Text(
          stringResource(R.string.received_messages),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 8.dp),
      )
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.messages) { msg ->
          Card(
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          ) {
            Text(
                text = msg,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreview() {
  MqttClientTheme {
    MainScreenContent(
        state =
            MainContract.State(
                connectionStatus = MqttConnectionStatus.Connected,
                messages = listOf("Message 1", "Message 2", "Message 3"),
            ),
        action = {},
        onSettingsClicked = {},
    )
  }
}
