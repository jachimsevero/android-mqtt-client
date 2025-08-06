package com.jachimsevero.mqttclient.presentation.configscreen.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.jachimsevero.mqttclient.presentation.configscreen.ConfigUiField

@Composable
internal fun NumberInput(
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
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      modifier = Modifier.fillMaxWidth(),
  )
}
