package com.jachimsevero.mqttclient.domain.model

sealed class MqttConnectionStatus {
  data object Connecting : MqttConnectionStatus()

  data object Connected : MqttConnectionStatus()

  data object Disconnected : MqttConnectionStatus()
}
