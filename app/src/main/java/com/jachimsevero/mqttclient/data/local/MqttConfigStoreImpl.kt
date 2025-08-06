package com.jachimsevero.mqttclient.data.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import com.jachimsevero.mqttclient.MqttConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

class MqttConfigStoreImpl(@ApplicationContext context: Context) : MqttConfigStore {

  private val mqttConfigDataStore =
      DataStoreFactory.create(
          serializer = MqttConfigSerializer,
          produceFile = { File(context.filesDir, "mqtt_config.pb") },
      )

  override fun get() = mqttConfigDataStore.data

  override suspend fun save(config: MqttConfig) {
    mqttConfigDataStore.updateData {
      it.toBuilder()
          .setHost(config.host)
          .setPort(config.port)
          .setUsername(config.username)
          .setPassword(config.password)
          .setTopic(config.topic)
          .setWebsocketPath(config.websocketPath)
          .build()
    }
  }
}
