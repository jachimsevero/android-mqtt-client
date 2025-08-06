package com.jachimsevero.mqttclient.presentation.configscreen.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.jachimsevero.mqttclient.presentation.configscreen.ConfigUiField

@Composable
internal fun PasswordInput(
    @StringRes label: Int,
    field: ConfigUiField,
    onValueChange: (String) -> Unit,
) {
  OutlinedTextField(
      value = field.value,
      onValueChange = onValueChange,
      label = { Text(stringResource(label)) },
      isError = field.error != null,
      supportingText = { field.error?.let { Text(it) } },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth(),
  )
}
