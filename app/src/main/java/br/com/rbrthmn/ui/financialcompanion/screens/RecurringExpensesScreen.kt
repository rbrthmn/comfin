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

package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString

data class RecurringExpense(val description: String, val value: String, val billingDay: String)

object RecurringExpensesDestination : NavigationDestination {
    override val route = "recurring_expenses"
}

@Composable
fun RecurringExpenses(modifier: Modifier = Modifier) {
    val recurringExpenses = listOf(
        RecurringExpense(
            description = "Aluguel",
            value = "1500.00",
            billingDay = "05" // Day of the month (e.g., 5th)
        ), RecurringExpense(
            description = "Internet",
            value = "100.00",
            billingDay = "10" // Day of the month (e.g., 20th)
        ), RecurringExpense(
            description = "Energia",
            value = "80.00",
            billingDay = "13"  // Day of the month (e.g., 10th)
        )
    )
    val totalExpensesValue = recurringExpenses.sumOf { it.value.toDouble() }.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecurringExpensesCard(expenses = recurringExpenses, totalExpensesValue = totalExpensesValue)
    }
}

@Composable
private fun RecurringExpensesCard(
    modifier: Modifier = Modifier, expenses: List<RecurringExpense>, totalExpensesValue: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.recurring_total_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency, value = totalExpensesValue
                    ),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp
                )
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300, easing = LinearOutSlowInEasing
                        )
                    ),
            ) {
                for (expense in expenses) {
                    ExpenseItem(
                        expense = expense,
                        modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_extra_small))
                    )
                }
            }
            AddExpenseButton()
        }
    }
}

@Composable
private fun ExpenseItem(expense: RecurringExpense, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.7f)
        ) {
            Text(
                text = "${stringResource(id = R.string.recurring_expense_billing_day)} ${expense.billingDay}",
                maxLines = 1,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
            )
            VerticalDivider(
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                    .height(dimensionResource(id = R.dimen.padding_medium))
            )
            Text(
                text = expense.description,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
            )
        }
        Text(
            text = valueWithCurrencyString(
                currencyStringId = R.string.brl_currency, value = expense.value
            ),
            maxLines = 1,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
        )
    }
}

@Composable
private fun AddExpenseButton(modifier: Modifier = Modifier) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value)
        NewRecurringExpenseDialog(
            onSaveButtonClick = { showDialog.value = false },
            onCancelButtonClick = { showDialog.value = false })

    Button(
        onClick = { showDialog.value = true },
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_icon_description),
                tint = Color.Gray,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
            )
            Text(
                text = stringResource(id = R.string.recurring_expense_add_button),
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
            )
        }
    }
}

@Composable
private fun NewRecurringExpenseDialog(onSaveButtonClick: () -> Unit, onCancelButtonClick: () -> Unit) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.recurring_expense_description_hint)) },
                    value = "",
                    onValueChange = { }
                )
                ExpenseBillingDayDropdownMenu()
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.recurring_expense_value_hint)) },
                    value = "",
                    onValueChange = { },
                    singleLine = true
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
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
private fun ExpenseBillingDayDropdownMenu() {
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

@Preview(showBackground = true)
@Composable
private fun RecurringExpensesScreenPreview(modifier: Modifier = Modifier) {
    RecurringExpenses()
}

@Preview
@Composable
private fun NewRecurringExpenseDialogPreview(modifier: Modifier = Modifier) {
    NewRecurringExpenseDialog(onSaveButtonClick = {}, onCancelButtonClick = {})
}
