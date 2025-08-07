package com.jachimsevero.mqttclient.di

import android.content.Context
import com.jachimsevero.mqttclient.data.local.MqttConfigStore
import com.jachimsevero.mqttclient.data.local.MqttConfigStoreImpl
import com.jachimsevero.mqttclient.data.mqtt.MqttClientManager
import com.jachimsevero.mqttclient.data.mqtt.MqttClientManagerImpl
import com.jachimsevero.mqttclient.service.MqttService
import com.jachimsevero.mqttclient.service.MqttServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideMqttConfigStore(@ApplicationContext context: Context): MqttConfigStore =
      MqttConfigStoreImpl(context)

  @Provides @Singleton fun provideMqttClientManager(): MqttClientManager = MqttClientManagerImpl()

  @Provides
  @Singleton
  fun provideMqttService(
      mqttClientManager: MqttClientManager,
      mqttConfigStore: MqttConfigStore,
  ): MqttService = MqttServiceImpl(mqttClientManager, mqttConfigStore)
}
