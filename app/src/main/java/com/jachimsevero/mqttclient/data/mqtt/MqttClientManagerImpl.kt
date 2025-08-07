package com.jachimsevero.mqttclient.data.mqtt

import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedListener
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.jachimsevero.mqttclient.MqttConfig
import com.jachimsevero.mqttclient.domain.model.MqttConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MqttClientManagerImpl() : MqttClientManager {

  private lateinit var mqttConfig: MqttConfig
  private var onStatusChanged: ((MqttConnectionStatus) -> Unit)? = null
  private var client: Mqtt5AsyncClient? = null
  private var shouldReconnect: Boolean = true

  override fun setConfig(config: MqttConfig) {
    mqttConfig = config
    createClient()
  }

  override fun setOnStatusChangedListener(listener: (MqttConnectionStatus) -> Unit) {
    onStatusChanged = listener
  }

  private fun createClient() {
    client =
        MqttClient.builder()
            .useMqttVersion5()
            .identifier(UUID.randomUUID().toString())
            .serverHost(mqttConfig.host)
            .serverPort(mqttConfig.port)
            .sslWithDefaultConfig()
            .webSocketConfig()
            .serverPath(mqttConfig.websocketPath)
            .applyWebSocketConfig()
            .addDisconnectedListener(disconnectListener())
            .addConnectedListener { onStatusChanged?.invoke(MqttConnectionStatus.Connected) }
            .buildAsync()
  }

  override suspend fun connect() {
    withContext(Dispatchers.IO) {
      try {
        onStatusChanged?.invoke(MqttConnectionStatus.Connecting)
        shouldReconnect = true

        client?.let {
          it.connectWith()
              .simpleAuth()
              .username(mqttConfig.username)
              .password(mqttConfig.password.toByteArray())
              .applySimpleAuth()
              .send()
              .await()
          onStatusChanged?.invoke(MqttConnectionStatus.Connected)
        } ?: throw IllegalStateException("Client not initialized")
      } catch (e: Exception) {
        if (e.message?.contains("NOT_AUTHORIZED") == true) {
          shouldReconnect = false
        }

        onStatusChanged?.invoke(MqttConnectionStatus.Disconnected)
        throw e
      }
    }
  }

  override suspend fun disconnect() {
    withContext(Dispatchers.IO) {
      shouldReconnect = false
      onStatusChanged?.invoke(MqttConnectionStatus.Disconnected)
      client?.disconnect()?.await()
    }
  }

  override suspend fun subscribe(topic: String, onMessageReceived: (String) -> Unit) {
    withContext(Dispatchers.IO) {
      client
          ?.subscribeWith()
          ?.topicFilter(topic)
          ?.qos(MqttQos.AT_LEAST_ONCE)
          ?.callback { message ->
            val payload =
                message.payload
                    .map { buffer ->
                      val bytes = ByteArray(buffer.remaining())
                      buffer.get(bytes)
                      String(bytes)
                    }
                    .orElse("")
            onMessageReceived(payload)
          }
          ?.send()
          ?.await()
    }
  }

  override suspend fun publish(topic: String, payload: String) {
    withContext(Dispatchers.IO) {
      client
          ?.publishWith()
          ?.topic(topic)
          ?.payload(payload.toByteArray())
          ?.qos(MqttQos.AT_LEAST_ONCE)
          ?.send()
          ?.await()
    }
  }

  private fun disconnectListener(): MqttClientDisconnectedListener =
      MqttClientDisconnectedListener { context ->
        onStatusChanged?.invoke(MqttConnectionStatus.Disconnected)

        if (!shouldReconnect) {
          return@MqttClientDisconnectedListener
        }

        CoroutineScope(Dispatchers.IO).launch {
          delay(1_000)
          try {
            connect()
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }
      }
}
