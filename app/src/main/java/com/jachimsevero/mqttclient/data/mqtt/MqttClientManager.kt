package com.jachimsevero.mqttclient.data.mqtt

import com.jachimsevero.mqttclient.MqttConfig
import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus

interface MqttClientManager {
  fun setConfig(config: MqttConfig)

  fun setOnStatusChangedListener(listener: (MqttConnectionStatus) -> Unit)

  suspend fun connect()

  suspend fun disconnect()

  suspend fun subscribe(topic: String, onMessageReceived: (String) -> Unit)

  suspend fun publish(topic: String, payload: String)
}
