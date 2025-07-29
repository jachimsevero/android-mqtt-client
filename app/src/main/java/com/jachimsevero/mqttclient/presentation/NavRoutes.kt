package com.jachimsevero.mqttclient.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoutes {
  @Serializable data object ConfigScreen : NavRoutes()
}
