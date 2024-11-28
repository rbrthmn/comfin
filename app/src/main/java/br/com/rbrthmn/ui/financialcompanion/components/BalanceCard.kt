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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.DecimalInputField
import br.com.rbrthmn.ui.financialcompanion.viewmodels.BalanceCardViewModel
import br.com.rbrthmn.ui.financialcompanion.viewmodels.BalanceCardViewModelImpl
import org.koin.androidx.compose.koinViewModel

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    viewModel: BalanceCardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddBalanceDialog = rememberSaveable { mutableStateOf(false) }

    if (showAddBalanceDialog.value)
        AddBankAccountDialog(
            viewModel = viewModel,
            onCancelButtonClick = {
                viewModel.cleanNewAccount()
                showAddBalanceDialog.value = false
            },
            onSaveButtonClick = { viewModel.onSaveClick(showAddBalanceDialog) },
        )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        ValuesList(
            totalValue = uiState.totalBalance,
            totalValueTitle = stringResource(id = R.string.total_balance_title),
            values = uiState.accounts,
            onAddItemButtonClick = { showAddBalanceDialog.value = true },
            addItemButtonText = stringResource(id = R.string.add_account_button)
        )
    }
}

@Composable
private fun AddBankAccountDialog(
    modifier: Modifier = Modifier,
    viewModel: BalanceCardViewModel,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                )
            ) {
                DecimalInputField(
                    onValueChange = viewModel::onInitialBalanceChange,
                    value = viewModel.newAccountBalance,
                    label = stringResource(id = R.string.balance_input_hint),
                    prefix = stringResource(id = R.string.brl_currency),
                    isError = !viewModel.isNewAccountBalanceValid
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.balance_description_input_hint)) },
                    maxLines = 100,
                    value = viewModel.newAccountDescription,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    isError = !viewModel.isNewAccountDescriptionValid
                )
                BanksDropdownMenu(
                    onBankSelected = viewModel::onBankChange,
                    isValid = viewModel.isNewAccountBankValid,
                    modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
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

@Preview
@Composable
fun BalanceCardPreview(modifier: Modifier = Modifier) {
    BalanceCard(
        viewModel = BalanceCardViewModelImpl(),
        modifier = modifier
    )
}

@Preview
@Composable
fun AddBankAccountDialogPreview(modifier: Modifier = Modifier) {
    AddBankAccountDialog(
        viewModel = BalanceCardViewModelImpl(),
        onSaveButtonClick = { },
        onCancelButtonClick = { },
        modifier = modifier,

        )
}
