package com.jachimsevero.mqttclient.service

import com.jachimsevero.mqttclient.data.local.MqttConfigStore
import com.jachimsevero.mqttclient.data.mqtt.MqttClientManager
import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class MqttServiceImpl(
    private val mqttClientManager: MqttClientManager,
    private val mqttConfigStore: MqttConfigStore,
) : MqttService {

  private val _connectionStatus =
      MutableStateFlow<MqttConnectionStatus>(MqttConnectionStatus.Disconnected)
  override val connectionStatus = _connectionStatus.asStateFlow()

  private val _receivedMessages =
      MutableSharedFlow<String>(
          replay = 10,
          extraBufferCapacity = 64,
          onBufferOverflow = BufferOverflow.DROP_OLDEST,
      )
  override val receivedMessages = _receivedMessages.asSharedFlow()

  private var isStarted = AtomicBoolean(false)

  override suspend fun start() {
    if (isStarted.compareAndSet(expectedValue = false, newValue = true)) {
      mqttClientManager.setOnStatusChangedListener { _connectionStatus.value = it }

      val config = mqttConfigStore.get().first()
      mqttClientManager.setConfig(config)
      mqttClientManager.connect()

      mqttClientManager.subscribe("qrph/updates/MERC23/M000101") { message ->
        _receivedMessages.tryEmit(message)
      }
    }
  }

  override suspend fun stop() {
    if (isStarted.compareAndSet(expectedValue = true, newValue = false)) {
      mqttClientManager.disconnect()
    }
  }

  override suspend fun publish(topic: String, payload: String) {
    mqttClientManager.publish(topic, payload)
  }
}
