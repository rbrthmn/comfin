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

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.rbrthmn.R


@Composable
fun BanksDropdownMenu(
    onBankSelected: (Int, String) -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val bankList: List<Pair<Int, String>> = listOf(
        Pair(0, "Banco Inter"),
        Pair(1, "Banco Interpanamericano latino das americas"),
        Pair(2, "Banco Santander"),
        Pair(3, "Banco Bradesco"),
        Pair(4, "Banco do Brasil"),
        Pair(5, "Banco Itaú"),
        Pair(6, "Banco BMG"),
        Pair(7, "Banco do Nordeste"),
        Pair(8, "Banco do ddasdsad"),
        Pair(9, "Banco Inter"),
        Pair(10, "Banco Interpanamericano latino das americas"),
        Pair(11, "Banco Santander"),
        Pair(12, "Banco Bradesco"),
        Pair(13, "Banco do Brasil"),
        Pair(14, "Banco Itaú"),
        Pair(15, "Banco BMG"),
        Pair(16, "Banco do Nordeste"),
        Pair(17, "Banco do ddasdsad")
    )

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText ?: stringResource(id = R.string.blank),
            label = { Text(text = stringResource(id = R.string.bank_hint)) },
            onValueChange = { selectedOptionText = it },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, "contentDescription")
                }
            },
            singleLine = true,
            isError = !isValid
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bankList.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption.second
                        onBankSelected(selectionOption.first, selectionOption.second)
                        expanded = false
                    },
                    text = { Text(text = selectionOption.second) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.account_balance),
                            contentDescription = "Balance Icon",
                            modifier = Modifier.clip(shape = Shape)
                        )
                    }
                )
            }
        }
    }
}
