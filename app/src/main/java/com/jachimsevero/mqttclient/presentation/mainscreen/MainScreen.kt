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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jachimsevero.mqttclient.R
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme

@Composable
internal fun MainScreen(onSettingsClicked: () -> Unit) {
  MainScreenContent(onSettingsClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(onSettingsClicked: () -> Unit) {
  val messages = listOf("message1", "message2")

  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopAppBar(
            title = {
              Column {
                Text(stringResource(R.string.app_name))
                Text(text = "Status: connecting", style = MaterialTheme.typography.labelSmall)
              }
            },
            actions = {
              IconButton(onClick = {}) {
                Icon(Icons.Default.Refresh, contentDescription = "Reconnect")
              }
              IconButton(onClick = { onSettingsClicked() }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
              }
            },
        )
      },
  ) { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
      Text(
          stringResource(R.string.received_messages),
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 8.dp),
      )
      LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(messages) { msg ->
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
  MqttClientTheme { MainScreenContent {} }
}
