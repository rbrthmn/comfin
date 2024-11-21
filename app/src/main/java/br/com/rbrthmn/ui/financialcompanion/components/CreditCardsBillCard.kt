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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.viewmodels.FinancialOverviewUiState


@Composable
fun CreditCardsBillCard(
    totalCreditCardsBill: String,
    cardBills: List<FinancialOverviewUiState>,
    modifier: Modifier = Modifier
) {
    val showAddCardBillDialog = remember { mutableStateOf(false) }

    if (showAddCardBillDialog.value)
        AddCardBillDialog(
            onCancelButtonClick = { showAddCardBillDialog.value = false },
            onSaveButtonClick = { showAddCardBillDialog.value = false })

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        ValuesList(
            totalValue = totalCreditCardsBill,
            totalValueTitle = stringResource(id = R.string.total_bills_title),
            values = cardBills,
            onAddItemButtonClick = { showAddCardBillDialog.value = true },
            addItemButtonText = stringResource(id = R.string.add_card_button)
        )
    }
}

@Composable
fun AddCardBillDialog(
    modifier: Modifier = Modifier,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                )
            ) {
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.card_bill_input_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = "",
                    onValueChange = { },
                    singleLine = true
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.balance_description_input_hint)) },
                    maxLines = 100,
                    value = "",
                    onValueChange = { }
                )
                BanksDropdownMenu(modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))
                CardBillCloseDayDropdownMenu()
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(text = stringResource(id = R.string.cancel_button))
                    }
                    Button(onClick = onSaveButtonClick) {
                        Text(text = stringResource(id = R.string.save_button))
                    }
                }
            }
        }
    }
}

@Composable
private fun CardBillCloseDayDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val options = List(31) { (it + 1).toString() }

    OutlinedTextField(
        value = selectedOptionText ?: stringResource(id = R.string.blank),
        label = { Text(text = stringResource(id = R.string.card_bill_close_day_hint)) },
        onValueChange = { selectedOptionText = it },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.ArrowDropDown, "contentDescription")
            }
        },
        singleLine = true
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { selectionOption ->
            DropdownMenuItem(
                onClick = {
                    selectedOptionText = selectionOption
                    expanded = false
                },
                text = { Text(text = selectionOption) }
            )
        }
    }
}

@Preview
@Composable
fun CreditCardsBillCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(FinancialOverviewUiState( "Banco A", "500,00", "R$"), FinancialOverviewUiState("Banco B", "1.000,00", "R$"))
    val totalCreditCardsBill = "12234.00"
    CreditCardsBillCard(
        totalCreditCardsBill = totalCreditCardsBill,
        cardBills = accounts,
        modifier = modifier
    )
}

@Preview
@Composable
fun AddCardBillDialogPreview(modifier: Modifier = Modifier) {
    AddCardBillDialog(onSaveButtonClick = { }, onCancelButtonClick = { })
}
