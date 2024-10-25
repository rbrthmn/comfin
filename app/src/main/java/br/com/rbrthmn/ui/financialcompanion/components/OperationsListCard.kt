package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.screens.Operation
import br.com.rbrthmn.ui.financialcompanion.utils.OperationType
import br.com.rbrthmn.ui.financialcompanion.utils.getOperationsMock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun OperationsListCard(operations: List<Operation>) {
    val showAddOperationDialog = remember { mutableStateOf(false) }
    if (showAddOperationDialog.value)
        AddOperationDialog(
            onCancelButtonClick = { showAddOperationDialog.value = false },
            onSaveButtonClick = { showAddOperationDialog.value = false })

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
                value = "",
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
            TextButton(
                onClick = { showAddOperationDialog.value = true },
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
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
            }
            if (operations.isNotEmpty()) {
                HorizontalDivider()
                OperationsList(operations = operations)
            }
        }
    }
}

@Composable
private fun AddOperationDialog(
    modifier: Modifier = Modifier,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    var operationType: OperationType? by remember { mutableStateOf(null) }

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
                    label = { Text(text = stringResource(id = R.string.operation_value_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = "",
                    onValueChange = { },
                    singleLine = true
                )
                OperationTypeDropdownMenu(onTypeClicked = { operationType = it })
                when (operationType) {
                    OperationType.TRANSFER -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.origin_account_hint))
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.aimed_account_hint))
                    }

                    OperationType.DEBIT_PURCHASE,
                    OperationType.BILL_PAYMENT,
                    OperationType.WITHDRAWAL -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.origin_account_hint))
                    }

                    OperationType.DEPOSIT,
                    OperationType.INCOME -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.aimed_account_hint))
                    }

                    OperationType.RESERVE_ALLOCATION -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.origin_account_hint))
                        ReservesDropdownMenu()
                    }

                    OperationType.RESERVE_WITHDRAWAL -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.aimed_account_hint))
                        ReservesDropdownMenu()
                    }

                    OperationType.OTHER, null -> {
                        AccountsDropdownMenu(accountType = stringResource(id = R.string.origin_account_hint))
                    }
                }
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.balance_description_input_hint)) },
                    maxLines = 100,
                    value = "",
                    onValueChange = { }
                )
                DatePickerDocked()
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
    onTypeClicked: (type: OperationType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val options = OperationType.entries

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
            singleLine = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectedOption ->
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
private fun AccountsDropdownMenu(modifier: Modifier = Modifier, accountType: String) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "Conta A",
        "Conta B",
        "Conta C",
        "Conta D",
        "Conta E",
        "Conta F",
        "Conta G",

        )
    var selectedOptionText: String? by remember { mutableStateOf(null) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText ?: stringResource(id = R.string.blank),
            label = { Text(text = accountType) },
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
                    text = { Text(text = selectionOption) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.help),
                            contentDescription = "Balance Icon"
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: convertMillisToDate(System.currentTimeMillis())

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Data da operação") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
private fun OperationsList(operations: List<Operation>) {
    val groupedOperations = operations.groupBy {
        SimpleDateFormat("EEE, dd", Locale.getDefault()).format(it.date)
    }

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
private fun DayOfWeekAndMonthText(date: Date) {
    val formatter = SimpleDateFormat("EEE, dd", Locale.getDefault())
    val formattedDate = formatter.format(date)
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
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Text(
                text = description,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = type,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
                extras?.let {
                    VerticalDivider(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small)))
                    Text(
                        text = it,
                        fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                    )
                }
            }
        }
        Text(text = value, fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp)
    }
}

@Preview
@Composable
fun OperationsListCardPreview(modifier: Modifier = Modifier) {
    OperationsListCard(operations = getOperationsMock())
}

@Preview
@Composable
fun AddOperationDialogPreview(modifier: Modifier = Modifier) {
    AddOperationDialog(onSaveButtonClick = {}, onCancelButtonClick = {})
}
