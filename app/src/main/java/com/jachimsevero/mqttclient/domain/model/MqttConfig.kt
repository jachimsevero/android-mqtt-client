package com.jachimsevero.mqttclient.domain.model

data class MqttConfig(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val topic: String,
    val websocketPath: String,
)
