package com.jachimsevero.mqttclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jachimsevero.mqttclient.presentation.configscreen.ConfigScreen
import com.jachimsevero.mqttclient.presentation.mainscreen.MainScreen
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MqttClientTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = NavRoutes.MainScreen) {
          composable<NavRoutes.MainScreen> {
            MainScreen { navController.navigate(NavRoutes.ConfigScreen) }
          }
          composable<NavRoutes.ConfigScreen> { ConfigScreen { navController.popBackStack() } }
        }
      }
    }
  }
}
