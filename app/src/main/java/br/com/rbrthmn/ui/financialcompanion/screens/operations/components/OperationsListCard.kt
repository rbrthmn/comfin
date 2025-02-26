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

package br.com.rbrthmn.ui.financialcompanion.screens.operations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.screens.operations.Operation
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenContract
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreenViewModel
import br.com.rbrthmn.ui.financialcompanion.utils.ResourceStringProvider
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun OperationsListCard(viewModel: OperationsScreenContract.OperationsScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddOperationDialog = rememberSaveable { mutableStateOf(false) }

    if (showAddOperationDialog.value)
        AddOperationDialog(
            viewModel = viewModel,
            onSaveButtonClick = { viewModel.onSaveButtonClick(showAddOperationDialog) },
            onCancelButtonClick = {
                viewModel.resetDialogFields()
                showAddOperationDialog.value = false
            }
        )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_medium),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            TextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            HorizontalDivider()
            TextButton(
                onClick = { showAddOperationDialog.value = true },
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_icon_description),
                        tint = Color.Gray,
                        modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_extra_small))
                    )
                    Text(
                        text = stringResource(id = R.string.add_operation_button),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                }
            }
            if (uiState.operations.isNotEmpty()) {
                HorizontalDivider()
                OperationsList(operations = uiState.operations)
            }
        }
    }
}

@Composable
fun AddOperationDialog(
    modifier: Modifier = Modifier,
    viewModel: OperationsScreenContract.OperationsScreenViewModel,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    availableOperationTypes: List<OperationType> = OperationType.entries
) {
    val uiState by viewModel.uiState.collectAsState()
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
                    label = { Text(text = stringResource(id = R.string.operation_description_hint)) },
                    value = uiState.newOperationDescription,
                    onValueChange = viewModel::onDescriptionChange,
                    isError = !uiState.isNewOperationDescriptionValid,
                    singleLine = true
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.operation_value_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = uiState.newOperationValue,
                    onValueChange = viewModel::onValueChange,
                    isError = !uiState.isNewOperationValueValid,
                    singleLine = true
                )
                OperationTypeDropdownMenu(
                    onTypeClicked = { viewModel.onOperationTypeChange(it) },
                    operationTypes = availableOperationTypes,
                    isError = !uiState.isNewOperationTypeValid
                )
                uiState.dialogFields.forEach { composableFunction ->
                    composableFunction()
                }
                DatePickerField(
                    onDateSelected = { viewModel.onOperationDateChange(it) },
                    isError = !uiState.isNewOperationDateValid
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
private fun OperationTypeDropdownMenu(
    modifier: Modifier = Modifier,
    onTypeClicked: (type: OperationType) -> Unit,
    operationTypes: List<OperationType>,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText ?: stringResource(id = R.string.blank),
            label = { Text(text = stringResource(id = R.string.operation_type_hint)) },
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
            operationTypes.forEach { selectedOption ->
                val optionString = stringResource(id = selectedOption.stringId)
                DropdownMenuItem(
                    onClick = {
                        onTypeClicked(selectedOption)
                        selectedOptionText = optionString
                        expanded = false
                    },
                    text = { Text(text = optionString) },
                )
            }
        }
    }
}


@Composable
private fun OperationsList(operations: List<Operation>) {
    val groupedOperations = operations.groupBy { it.date }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        groupedOperations.forEach { (_, operationsForDate) ->
            DayOfWeekAndMonthText(date = operationsForDate[0].date)

            operationsForDate.forEach { operation ->
                OperationItem(
                    description = operation.description,
                    value = operation.value,
                    type = operation.type,
                    extras = operation.extras
                )
            }
        }
    }
}

@Composable
private fun DayOfWeekAndMonthText(date: LocalDate) {
    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE, dd"))
    Text(
        text = formattedDate, fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun OperationItem(
    description: String,
    value: String,
    type: String,
    extras: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = description,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                lineHeight = dimensionResource(id = R.dimen.font_size_medium).value.sp,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = type,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp,
                    lineHeight = dimensionResource(id = R.dimen.font_size_small).value.sp,
                )
                extras?.let {
                    VerticalDivider(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.padding_small))
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                    )
                    Text(
                        text = it,
                        fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp,
                        lineHeight = dimensionResource(id = R.dimen.font_size_small).value.sp,
                    )
                }
            }
        }
        Text(
            text = valueWithCurrencyString(currencyStringId = R.string.brl_currency, value = value),
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
            lineHeight = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        )
    }
}

@Preview
@Composable
fun OperationsListCardPreview() {
    val context = LocalContext.current
    val stringProvider = ResourceStringProvider(context)

    OperationsListCard(viewModel = OperationsScreenViewModel(stringProvider))
}

@Preview
@Composable
fun AddOperationDialogPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val stringProvider = ResourceStringProvider(context)

    AddOperationDialog(
        viewModel = OperationsScreenViewModel(stringProvider),
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}
