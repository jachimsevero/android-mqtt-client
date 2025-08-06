package com.jachimsevero.mqttclient.data.local

import com.jachimsevero.mqttclient.MqttConfig
import kotlinx.coroutines.flow.Flow

interface MqttConfigStore {
  fun get(): Flow<MqttConfig>

  suspend fun save(config: MqttConfig)
}
