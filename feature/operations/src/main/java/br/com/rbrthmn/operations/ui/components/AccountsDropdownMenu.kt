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

package br.com.rbrthmn.operations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.ui.R as uiR

const val ACCOUNTS_DROPDOWN_MENU_TAG = "accounts_dropdown_menu"
const val ACCOUNTS_DROPDOWN_ICON_TAG = "accounts_dropdown_icon"
const val ACCOUNTS_DROPDOWN_MENU_ITEM_TAG = "accounts_dropdown_menu_item"
const val ADD_SIMPLE_ACCOUNT_DIALOG_TAG = "add_simple_account_dialog"

@Composable
fun AccountsDropdownMenu(
    modifier: Modifier = Modifier,
    operationAccountType: OperationAccountType,
    onAccountSelected: (String) -> Unit,
    isError: Boolean,
    accounts: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var showAddAccountDialog by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val newAccountsList = accounts.plus(stringResource(R.string.add_account_option))

    if (showAddAccountDialog) {
        AddSimpleAccountDialog(
            onCancelButtonClick = { showAddAccountDialog = false },
            onSaveButtonClick = { showAddAccountDialog = false })
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText ?: stringResource(id = uiR.string.blank),
            label = { Text(text = stringResource(id = operationAccountType.stringId)) },
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = true },
                    modifier = modifier.testTag(ACCOUNTS_DROPDOWN_ICON_TAG) // ✅ Corrigido: adicionado '= modifier'
                ) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(uiR.string.drop_down_arrow_icon_description),
                    )
                }
            },
            singleLine = true,
            isError = isError
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.testTag(ACCOUNTS_DROPDOWN_MENU_TAG)
        ) {
            newAccountsList.forEach { selectedOption ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(ACCOUNTS_DROPDOWN_MENU_ITEM_TAG),
                    onClick = {
                        selectedOptionText = selectedOption
                        expanded = false
                        onAccountSelected(selectedOption)
                        if (selectedOptionText == newAccountsList.last()) showAddAccountDialog =
                            true
                    },
                    text = { Text(text = selectedOption) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = uiR.drawable.bank_icon),
                            contentDescription = stringResource(R.string.operation_account_icon_description)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AddSimpleAccountDialog(
    modifier: Modifier = Modifier,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    var accountHolder by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag(ADD_SIMPLE_ACCOUNT_DIALOG_TAG)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = uiR.dimen.padding_large),
                    vertical = dimensionResource(id = uiR.dimen.padding_medium)
                )
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.account_holder_hint)) },
                    value = accountHolder,
                    onValueChange = { accountHolder = it },
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = uiR.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Button(onClick = onCancelButtonClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = uiR.string.close_icon_description)
                        )
                    }
                    Button(onClick = onSaveButtonClick) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(id = uiR.string.check_icon_description)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountsDropdownMenuPreview() {
    AccountsDropdownMenu(
        operationAccountType = OperationAimedAccount,
        onAccountSelected = {},
        isError = false,
        accounts = listOf("Conta A", "Conta B", "Conta C", "Conta D", "Conta E")
    )
}

@Preview
@Composable
fun AddSimpleAccountDialogPreview() {
    AddSimpleAccountDialog(onSaveButtonClick = {}, onCancelButtonClick = {})
}