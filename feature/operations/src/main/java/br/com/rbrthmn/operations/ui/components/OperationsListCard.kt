/*
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
 */

package br.com.rbrthmn.operations.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.data.operations.model.OperationItem
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.operations.ui.Operation
import br.com.rbrthmn.operations.ui.OperationType
import br.com.rbrthmn.operations.ui.OperationsScreenContract
import br.com.rbrthmn.operations.ui.OperationsScreenViewModel
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import br.com.rbrthmn.ui.R as uiR

const val NEW_OPERATION_DIALOG_TAG = "new_operation_dialog"
const val OPERATION_TYPES_DROPDOWN_MENU_TAG = "operation_types_dropdown_menu"

@Composable
fun OperationsListCard(
    modifier: Modifier = Modifier,
    viewModel: OperationsScreenContract.ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddOperationDialog = rememberSaveable { mutableStateOf(false) }

    if (showAddOperationDialog.value)
        AddOperationDialog(
            viewModel = viewModel,
            onSaveButtonClick = {
                viewModel.onIntent(
                    OperationsScreenContract.Intent.OnSaveButtonClick(
                        showAddOperationDialog
                    )
                )
            },
            onCancelButtonClick = {
                viewModel.onIntent(OperationsScreenContract.Intent.OnResetDialogFields)
                showAddOperationDialog.value = false
            }
        )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = dimensionResource(id = uiR.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = uiR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                vertical = dimensionResource(id = uiR.dimen.padding_medium),
                horizontal = dimensionResource(id = uiR.dimen.padding_medium)
            )
        ) {
            TextField(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.onIntent(
                        OperationsScreenContract.Intent.OnSearchQueryChange(
                            it
                        )
                    )
                },
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            HorizontalDivider()
            TextButton(
                onClick = { showAddOperationDialog.value = true },
                contentPadding = PaddingValues(dimensionResource(id = uiR.dimen.zero_padding))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = uiR.string.add_icon_description),
                        tint = Color.Gray,
                        modifier = Modifier.padding(end = dimensionResource(id = uiR.dimen.padding_extra_small))
                    )
                    Text(
                        text = stringResource(id = R.string.add_operation_button),
                        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp
                    )
                }
            }
            if (uiState.operations.isNotEmpty()) {
                HorizontalDivider()
                OperationsList(
                    operations = uiState.operations,
                    onEdit = { operation ->
                        viewModel.onIntent(OperationsScreenContract.Intent.OnEditOperation(operation))
                        showAddOperationDialog.value = true
                    },
                    onDelete = { id ->
                        viewModel.onIntent(OperationsScreenContract.Intent.OnDeleteOperation(id))
                    }
                )
            }
        }
    }
}

@Composable
fun AddOperationDialog(
    modifier: Modifier = Modifier,
    viewModel: OperationsScreenContract.ViewModel,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    availableOperationTypes: List<OperationType> = OperationType.entries
) {
    val uiState by viewModel.uiState.collectAsState()
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag(NEW_OPERATION_DIALOG_TAG)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = uiR.dimen.padding_large),
                    vertical = dimensionResource(id = uiR.dimen.padding_medium)
                )
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.operation_description_hint)) },
                    value = uiState.newOperationDescription,
                    onValueChange = {
                        viewModel.onIntent(
                            OperationsScreenContract.Intent.OnDescriptionChange(
                                it
                            )
                        )
                    },
                    isError = !uiState.isNewOperationDescriptionValid,
                    singleLine = true
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = uiR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.operation_value_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = uiState.newOperationValue,
                    onValueChange = {
                        viewModel.onIntent(
                            OperationsScreenContract.Intent.OnValueChange(
                                it
                            )
                        )
                    },
                    isError = !uiState.isNewOperationValueValid,
                    singleLine = true
                )
                OperationTypeDropdownMenu(
                    onTypeClicked = {
                        viewModel.onIntent(
                            OperationsScreenContract.Intent.OnOperationTypeChange(
                                it
                            )
                        )
                    },
                    operationTypes = availableOperationTypes,
                    isError = !uiState.isNewOperationTypeValid
                )
                uiState.dialogFields.forEach { composableFunction ->
                    composableFunction()
                }
                DatePickerField(
                    onDateSelected = {
                        viewModel.onIntent(
                            OperationsScreenContract.Intent.OnOperationDateChange(
                                it
                            )
                        )
                    },
                    isError = !uiState.isNewOperationDateValid
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = uiR.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(text = stringResource(id = uiR.string.cancel_button))
                    }
                    Button(onClick = onSaveButtonClick) {
                        Text(text = stringResource(id = uiR.string.save_button))
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
            value = selectedOptionText ?: stringResource(id = uiR.string.blank),
            label = { Text(text = stringResource(id = R.string.operation_type_hint)) },
            onValueChange = { selectedOptionText = it },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        stringResource(uiR.string.drop_down_arrow_icon_description)
                    )
                }
            },
            singleLine = true,
            isError = isError
        )
        DropdownMenu(
            modifier = Modifier.testTag(OPERATION_TYPES_DROPDOWN_MENU_TAG),
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
private fun OperationsList(
    operations: List<Operation>,
    onEdit: (Operation) -> Unit,
    onDelete: (Long) -> Unit
) {
    val groupedOperations = operations.groupBy { it.date }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = uiR.dimen.padding_small)),
        modifier = Modifier.padding(top = dimensionResource(id = uiR.dimen.padding_small))
    ) {
        groupedOperations.forEach { (date, operationsForDate) ->
            key(date) {
                DayOfWeekAndMonthText(date = operationsForDate[0].date)

                operationsForDate.forEach { operation ->
                    key(operation.id) {
                        OperationItem(
                            description = operation.description,
                            value = operation.value,
                            type = operation.type,
                            extras = operation.extras,
                            onEdit = { onEdit(operation) },
                            onDelete = { onDelete(operation.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayOfWeekAndMonthText(date: LocalDate) {
    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE, dd"))
    Text(
        text = formattedDate,
        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun OperationItem(
    description: String,
    value: String,
    type: String,
    extras: String? = null,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.EndToStart -> { showDeleteConfirm = true; true }
                SwipeToDismissBoxValue.StartToEnd -> { onEdit(); false }
                else -> false
            }
        }
    )

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirm = false
                coroutineScope.launch { dismissState.reset() }
            },
            title = { Text(stringResource(R.string.delete_operation_title)) },
            text = { Text(stringResource(R.string.delete_operation_message)) },
            confirmButton = {
                Button(onClick = {
                    showDeleteConfirm = false
                    onDelete()
                }) { Text(stringResource(R.string.delete_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    coroutineScope.launch { dismissState.reset() }
                }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val (bgColor, icon, alignment) = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.EndToStart ->
                    Triple(Color.Red, Icons.Default.Delete, Alignment.CenterEnd)
                SwipeToDismissBoxValue.StartToEnd ->
                    Triple(Color(0xFF4CAF50), Icons.Default.Edit, Alignment.CenterStart)
                else -> Triple(Color.Transparent, null, Alignment.Center)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(horizontal = dimensionResource(id = uiR.dimen.padding_medium)),
                contentAlignment = alignment
            ) {
                icon?.let { Icon(it, contentDescription = null, tint = Color.White) }
            }
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { onEdit() }
                .padding(start = dimensionResource(id = uiR.dimen.padding_small))
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = description,
                    fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
                    lineHeight = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = type,
                        fontSize = dimensionResource(id = uiR.dimen.font_size_small).value.sp,
                        lineHeight = dimensionResource(id = uiR.dimen.font_size_small).value.sp,
                    )
                    extras?.let {
                        VerticalDivider(
                            modifier = Modifier
                                .height(dimensionResource(id = uiR.dimen.padding_small))
                                .padding(horizontal = dimensionResource(id = uiR.dimen.padding_extra_small))
                        )
                        Text(
                            text = it,
                            fontSize = dimensionResource(id = uiR.dimen.font_size_small).value.sp,
                            lineHeight = dimensionResource(id = uiR.dimen.font_size_small).value.sp,
                        )
                    }
                }
            }
            Text(
                text = valueWithCurrencyString(
                    currencyStringId = uiR.string.brl_currency,
                    value = value
                ),
                fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
                lineHeight = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun OperationsListCardPreview() {
    val context = LocalContext.current
    OperationsListCard(viewModel = previewOperationsViewModel(context))
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun AddOperationDialogPreview() {
    val context = LocalContext.current
    AddOperationDialog(
        viewModel = previewOperationsViewModel(context),
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}

private fun previewOperationsViewModel(context: android.content.Context): OperationsScreenViewModel {
    val mockData = flowOf(
        OperationsData(
            operations = listOf(
                OperationItem(
                    id = 1,
                    date = LocalDate.now(),
                    counterparty = "Salary",
                    notes = null,
                    amount = 5000.0,
                    type = "INCOME",
                    category = "INCOME",
                    accountName = "Conta Principal"
                ),
                OperationItem(
                    id = 2,
                    date = LocalDate.now(),
                    counterparty = "Supermarket",
                    notes = null,
                    amount = 150.0,
                    type = "DEBIT_PURCHASE",
                    category = "DEBIT_PURCHASE",
                    accountName = "Conta Principal"
                ),
                OperationItem(
                    id = 3,
                    date = LocalDate.now().minusDays(1),
                    counterparty = "Transfer",
                    notes = null,
                    amount = 200.0,
                    type = "PIX",
                    category = "PIX",
                    accountName = "Conta B"
                ),
            ),
            totalIncome = 5000.0,
            totalOutcome = 350.0,
            totalBalance = 4650.0
        )
    )
    return OperationsScreenViewModel(
        stringProvider = ResourceStringProvider(context),
        operationsRepository = object : OperationsRepository {
            override fun getOperationsForMonth(year: Int, month: Int) = mockData
            override fun getAvailableAccounts() =
                flowOf(emptyList<br.com.rbrthmn.data.operations.model.AccountItem>())
            override fun getAvailableReserves() =
                flowOf(emptyList<br.com.rbrthmn.data.operations.model.ReserveItem>())
            override suspend fun addOperation(data: br.com.rbrthmn.data.operations.model.NewOperationData) =
                Result.success(Unit)
            override suspend fun deleteOperation(id: Long) = Result.success(Unit)
            override suspend fun updateOperation(id: Long, data: br.com.rbrthmn.data.operations.model.NewOperationData) =
                Result.success(Unit)
        }
    ).doOnInit()
}
