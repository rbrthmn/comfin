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

package br.com.rbrthmn.misc.ui.recurringexpenses

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.misc.R
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object RecurringExpensesDestination : NavigationDestination {
    override val route = "recurring_expenses"
}

const val NEW_RECURRING_EXPENSE_DIALOG_TAG = "new_recurring_expense_dialog"

@Composable
fun RecurringExpensesScreen(
    modifier: Modifier = Modifier,
    viewModel: RecurringExpensesContract.ViewModel = koinViewModel<RecurringExpensesContract.ViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()

    RecurringExpensesScreen(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun RecurringExpensesScreen(
    modifier: Modifier = Modifier,
    uiState: RecurringExpensesContract.UIState,
    onIntent: (RecurringExpensesContract.Intent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecurringExpensesCard(
            expenses = uiState.expenses,
            totalExpensesValue = uiState.totalExpensesValue
        ) { onIntent(RecurringExpensesContract.Intent.OnAddExpenseButtonClick) }
    }

    if (uiState.showNewExpenseDialog) {
        NewRecurringExpenseDialog(
            uiState = uiState,
            onDescriptionChange = {
                onIntent(
                    RecurringExpensesContract.Intent.OnNewExpenseDescriptionChange(
                        it
                    )
                )
            },
            onValueChange = { onIntent(RecurringExpensesContract.Intent.OnNewExpenseValueChange(it)) },
            onBillingDayChange = {
                onIntent(
                    RecurringExpensesContract.Intent.OnNewExpenseBillingDayChange(
                        it
                    )
                )
            },
            onSaveButtonClick = { onIntent(RecurringExpensesContract.Intent.OnSaveNewExpense) },
            onCancelButtonClick = { onIntent(RecurringExpensesContract.Intent.OnCancelNewExpense) }
        )
    }
}

@Composable
private fun RecurringExpensesCard(
    modifier: Modifier = Modifier,
    expenses: List<RecurringExpense>,
    totalExpensesValue: String,
    onAddExpenseClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = commonR.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = commonR.dimen.padding_medium))
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.recurring_total_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = commonR.dimen.font_size_large).value.sp
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = valueWithCurrencyString(
                        currencyStringId = commonR.string.brl_currency, value = totalExpensesValue
                    ),
                    fontSize = dimensionResource(id = commonR.dimen.font_size_large).value.sp
                )
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300, easing = LinearOutSlowInEasing
                        )
                    ),
            ) {
                for (expense in expenses) {
                    ExpenseItem(
                        expense = expense,
                        modifier = modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_extra_small))
                    )
                }
            }
            AddExpenseButton(onAddExpenseClick = onAddExpenseClick)
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
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
            )
            VerticalDivider(
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                    .height(dimensionResource(id = commonR.dimen.padding_medium))
            )
            Text(
                text = expense.description,
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
            )
        }
        Text(
            text = valueWithCurrencyString(
                currencyStringId = commonR.string.brl_currency, value = expense.value
            ),
            maxLines = 1,
            fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
        )
    }
}

@Composable
private fun AddExpenseButton(modifier: Modifier = Modifier, onAddExpenseClick: () -> Unit) {
    Button(
        onClick = onAddExpenseClick,
        contentPadding = PaddingValues(dimensionResource(id = commonR.dimen.zero_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = commonR.string.add_icon_description),
                tint = Color.Gray,
                modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
            )
            Text(
                text = stringResource(id = R.string.recurring_expense_add_button),
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
            )
        }
    }
}

@Composable
private fun NewRecurringExpenseDialog(
    uiState: RecurringExpensesContract.UIState,
    onDescriptionChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onBillingDayChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = commonR.dimen.padding_medium))
                .testTag(NEW_RECURRING_EXPENSE_DIALOG_TAG),
            shape = RoundedCornerShape(dimensionResource(id = commonR.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = commonR.dimen.padding_medium)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.recurring_expense_description_hint)) },
                    value = uiState.newExpenseDescription,
                    onValueChange = onDescriptionChange,
                    isError = !uiState.isNewExpenseDescriptionValid
                )
                ExpenseBillingDayDropdownMenu(
                    selectedDay = uiState.newExpenseBillingDay,
                    onDaySelected = onBillingDayChange
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.recurring_expense_value_hint)) },
                    value = uiState.newExpenseValue,
                    onValueChange = onValueChange,
                    singleLine = true,
                    isError = !uiState.isNewExpenseValueValid
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = commonR.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(text = stringResource(id = commonR.string.cancel_button))
                    }
                    Button(onClick = onSaveButtonClick) {
                        Text(text = stringResource(id = commonR.string.save_button))
                    }
                }
            }
        }
    }
}
@Composable
private fun ExpenseBillingDayDropdownMenu(selectedDay: String, onDaySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = List(31) { (it + 1).toString() }

    OutlinedTextField(
        value = selectedDay,
        label = { Text(text = stringResource(id = R.string.card_bill_close_day_hint)) },
        onValueChange = onDaySelected,
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
                    onDaySelected(selectionOption)
                    expanded = false
                },
                text = { Text(text = selectionOption) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecurringExpensesScreenPreview() {
    val expenses = listOf(
        RecurringExpense(
            description = "Aluguel",
            value = "1500.00",
            billingDay = "05"
        ), RecurringExpense(
            description = "Internet",
            value = "100.00",
            billingDay = "10"
        ), RecurringExpense(
            description = "Energia",
            value = "80.00",
            billingDay = "13"
        )
    )
    RecurringExpensesScreen(
        uiState = RecurringExpensesContract.UIState(
            expenses = expenses,
            totalExpensesValue = expenses.sumOf { it.value.toDouble() }.toString()
        ),
        onIntent = {}
    )
}

@Preview
@Composable
private fun NewRecurringExpenseDialogPreview() {
    NewRecurringExpenseDialog(
        uiState = RecurringExpensesContract.UIState(),
        onDescriptionChange = {},
        onValueChange = {},
        onBillingDayChange = {},
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}
