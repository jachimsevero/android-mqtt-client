package com.jachimsevero.mqttclient.di

import android.content.Context
import com.jachimsevero.mqttclient.data.local.MqttConfigStore
import com.jachimsevero.mqttclient.data.local.MqttConfigStoreImpl
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
}
