package com.jachimsevero.mqttclient.presentation.configscreen.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
internal fun PasswordInput(@StringRes label: Int, value: String, onValueChange: (String) -> Unit) {
  OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      label = { Text(stringResource(label)) },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth(),
  )
}
