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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CreditCardsBillCard(
    totalCreditCardsBill: String,
    cardBills: List<Account>,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardBillDialog(
    modifier: Modifier = Modifier,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""


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
                BanksDropdownMenu(modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
                CardBillCloseDayDropdownMenu()
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    CardBillCloseDayDropdownMenu()
//                    OutlinedTextField(
//                        value = selectedDate,
//                        onValueChange = { },
//                        label = { Text("DOB") },
//                        readOnly = true,
//                        trailingIcon = {
//                            IconButton(onClick = {  }) {
//                                Icon(
//                                    imageVector = Icons.Default.DateRange,
//                                    contentDescription = "Select date"
//                                )
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(64.dp)
//                    )
//                    var selectedDay by remember { mutableStateOf(1) } // Inicializa com 0 ou um dia padrão
//
//                    DayOfMonthInput(
//                        selectedDay = selectedDay,
//                        onDaySelected = { selectedDay = it }
//                    )
//                }
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
fun CardBillCloseDayDropdownMenu(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "1", "2"
    )
    var selectedOptionText by remember { mutableStateOf(options.first()) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText,
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
}

@Preview
@Composable
fun CreditCardsBillCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}