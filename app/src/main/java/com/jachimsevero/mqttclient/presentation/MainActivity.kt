package com.jachimsevero.mqttclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jachimsevero.mqttclient.presentation.configscreen.ConfigScreen
import com.jachimsevero.mqttclient.presentation.mainscreen.MainScreen
import com.jachimsevero.mqttclient.presentation.mainscreen.MainViewModel
import com.jachimsevero.mqttclient.presentation.theme.MqttClientTheme
import com.jachimsevero.mqttclient.service.MqttService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject lateinit var mqttService: MqttService

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MqttClientTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = NavRoutes.MainScreen) {
          composable<NavRoutes.MainScreen> { backStackEntry ->
            val sharedViewModel: MainViewModel = hiltViewModel(backStackEntry)
            MainScreen(sharedViewModel) { navController.navigate(NavRoutes.ConfigScreen) }
          }

          composable<NavRoutes.ConfigScreen> {
            val entry = navController.getBackStackEntrySafe(NavRoutes.MainScreen)
            val sharedViewModel: MainViewModel =
                if (entry != null) {
                  hiltViewModel(entry)
                } else {
                  hiltViewModel()
                }

            ConfigScreen(sharedViewModel) { navController.popBackStack() }
          }
        }
      }
    }

    lifecycleScope.launch {
      try {
        mqttService.start()
      } catch (_: Exception) {}
    }
  }
}

fun NavController.getBackStackEntrySafe(route: NavRoutes): NavBackStackEntry? {
  return try {
    getBackStackEntry(route)
  } catch (_: Exception) {
    null
  }
}
