/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import br.com.rbrthmn.ui.financialcompanion.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.financialcompanion.utils.DecimalInputVisualTransformation
import org.koin.compose.koinInject

@Composable
fun DecimalInputField(
    modifier: Modifier = Modifier,
    decimalFormatter: DecimalInputFieldFormatter = koinInject(),
    onValueChange: (String) -> Unit,
    value: String,
    prefix: String = "",
    label: String = "",
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            val newValue = decimalFormatter.cleanup(it)
            onValueChange(newValue)
        },
        prefix = { Text(text = prefix)},
        label = { Text(text = label)},
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        visualTransformation = DecimalInputVisualTransformation(decimalFormatter)
    )
}
