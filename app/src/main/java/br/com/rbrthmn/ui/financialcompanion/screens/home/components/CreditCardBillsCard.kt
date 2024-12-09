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

package br.com.rbrthmn.ui.financialcompanion.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.commoncomponents.DecimalInputField
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreditCardBillsCard(
    modifier: Modifier = Modifier,
    viewModel: CreditCardBillsCardContract.CreditCardsBillCardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddCardDialog = rememberSaveable { mutableStateOf(false) }

    if (showAddCardDialog.value)
        AddCreditCardDialog(
            viewModel = viewModel,
            onCancelButtonClick = {
                viewModel.cleanInputs()
                showAddCardDialog.value = false
            },
            onSaveButtonClick = { viewModel.onSaveClick(showAddCardDialog) })

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        CreditCardBillsList(
            totalBill = uiState.totalBill,
            creditCards = uiState.bills,
            onAddItemButtonClick = { showAddCardDialog.value = true },
        )
    }
}

@Composable
private fun CreditCardBillsList(
    modifier: Modifier = Modifier,
    totalBill: String,
    creditCards: List<CreditCardBillUiState>,
    onAddItemButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TotalValueText(
            totalValueTitle = stringResource(id = R.string.total_bills_title),
            totalValue = totalBill,
            modifier = modifier
        )
        HorizontalDivider()
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
        ) {
            for (card in creditCards) {
                CreditCardItem(
                    itemName = card.name,
                    itemValue = card.value,
                    canEditValue = card.canValueBeEdited,
                    dueDay = card.dueDay.toString()
                )
            }
            AddItemButton(
                buttonText = stringResource(id = R.string.add_card_button),
                onButtonClick = onAddItemButtonClick
            )
        }
    }
}

@Composable
private fun CreditCardItem(
    modifier: Modifier = Modifier,
    itemName: String,
    itemValue: String,
    canEditValue: Boolean,
    dueDay: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.6f)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(id = R.string.bank_icon_description),
                tint = Color.Gray,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = itemName,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
                Text(
                    text = stringResource(id = R.string.credit_card_due_day_label) + dueDay,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
            }
        }
        TextField(
            prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
            value = itemValue,
            onValueChange = { },
            readOnly = !canEditValue,
            singleLine = true,
            modifier = modifier.weight(0.4f)
        )
    }
}


@Composable
private fun AddCreditCardDialog(
    modifier: Modifier = Modifier,
    viewModel: CreditCardBillsCardContract.CreditCardsBillCardViewModel,
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
                    label = { Text(text = stringResource(id = R.string.balance_name_input_hint)) },
                    maxLines = 100,
                    value = viewModel.newCreditCardName,
                    onValueChange = viewModel::onNewCreditCardNameChange,
                    isError = !viewModel.isNewCreditCardNameValid,
                )
                DecimalInputField(
                    onValueChange = viewModel::onNewCreditCardBillChange,
                    value = viewModel.newCreditCardBill,
                    label = stringResource(id = R.string.card_bill_input_hint),
                    prefix = stringResource(id = R.string.brl_currency),
                    isError = !viewModel.isNewCreditCardBillValid
                )
                BanksDropdownMenu(
                    onBankSelected = viewModel::onBankChange,
                    isValid = viewModel.isNewCreditCardBankNameValid,
                    modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))
                )
                CardBillCloseDayDropdownMenu(
                    onDayClicked = viewModel::onNewCreditCardBillDueDayChange,
                    isError = !viewModel.isNewCreditCardBillDueDayValid
                )
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
private fun CardBillCloseDayDropdownMenu(onDayClicked: (day: Int) -> Unit, isError: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val options = List(31) { (it + 1) }

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
        singleLine = true,
        isError = isError
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { selectionOption ->
            DropdownMenuItem(
                onClick = {
                    selectedOptionText = selectionOption.toString()
                    onDayClicked(selectionOption)
                    expanded = false
                },
                text = { Text(text = selectionOption.toString()) }
            )
        }
    }
}

@Preview
@Composable
fun CreditCardsBillCardPreview(modifier: Modifier = Modifier) {
    CreditCardBillsCard(
        viewModel = CreditCardBillsCardViewModel(),
        modifier = modifier
    )
}

@Preview
@Composable
fun AddCardBillDialogPreview(modifier: Modifier = Modifier) {
    AddCreditCardDialog(
        onSaveButtonClick = { },
        onCancelButtonClick = {},
        viewModel = CreditCardBillsCardViewModel()
    )
}
