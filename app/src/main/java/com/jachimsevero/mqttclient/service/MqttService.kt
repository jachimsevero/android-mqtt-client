package com.jachimsevero.mqttclient.service

import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MqttService {
  val connectionStatus: StateFlow<MqttConnectionStatus>
  val receivedMessages: SharedFlow<String>

  suspend fun start()

  suspend fun stop()

  suspend fun publish(topic: String, payload: String)
}
