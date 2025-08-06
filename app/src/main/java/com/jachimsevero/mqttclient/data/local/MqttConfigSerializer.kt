package com.jachimsevero.mqttclient.data.local

import androidx.datastore.core.Serializer
import com.jachimsevero.mqttclient.MqttConfig
import java.io.InputStream
import java.io.OutputStream

object MqttConfigSerializer : Serializer<MqttConfig> {
  override val defaultValue: MqttConfig = MqttConfig.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): MqttConfig = MqttConfig.parseFrom(input)

  override suspend fun writeTo(t: MqttConfig, output: OutputStream) = t.writeTo(output)
}
