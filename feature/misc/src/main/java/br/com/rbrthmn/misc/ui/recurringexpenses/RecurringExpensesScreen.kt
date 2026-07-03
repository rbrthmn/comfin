package br.com.rbrthmn.misc.recurringexpenses

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
fun RecurringExpenses(
    modifier: Modifier = Modifier,
    viewModel: RecurringExpensesScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        RecurringExpensesCard(
            expenses = uiState.recurringExpenses,
            totalExpensesValue = uiState.totalExpensesValue,
            showAddExpenseDialog = uiState.showAddExpenseDialog,
            newExpenseDescription = uiState.newExpenseDescription,
            newExpenseValue = uiState.newExpenseValue,
            newExpenseBillingDay = uiState.newExpenseBillingDay,
            onOpenAddExpenseDialog = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnOpenAddExpenseDialog) },
            onDismissAddExpenseDialog = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnDismissAddExpenseDialog) },
            onDescriptionChange = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnNewExpenseDescriptionChange(it)) },
            onValueChange = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnNewExpenseValueChange(it)) },
            onBillingDayChange = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnNewExpenseBillingDayChange(it)) },
            onSaveNewExpense = { viewModel.onIntent(RecurringExpensesScreenContract.Intent.OnSaveNewExpense) }
        )
    }
}

@Composable
private fun RecurringExpensesCard(
    modifier: Modifier = Modifier,
    expenses: List<RecurringExpense>,
    totalExpensesValue: String,
    showAddExpenseDialog: Boolean,
    newExpenseDescription: String,
    newExpenseValue: String,
    newExpenseBillingDay: String,
    onOpenAddExpenseDialog: () -> Unit,
    onDismissAddExpenseDialog: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onBillingDayChange: (String) -> Unit,
    onSaveNewExpense: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
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
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = valueWithCurrencyString(
                        currencyStringId = commonR.string.brl_currency,
                        value = totalExpensesValue
                    ),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                    ),
            ) {
                for (expense in expenses) {
                    ExpenseItem(
                        expense = expense,
                        modifier = modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_extra_small))
                    )
                }
            }
            AddExpenseButton(
                showDialog = showAddExpenseDialog,
                newExpenseDescription = newExpenseDescription,
                newExpenseValue = newExpenseValue,
                newExpenseBillingDay = newExpenseBillingDay,
                onOpenDialog = onOpenAddExpenseDialog,
                onDismissDialog = onDismissAddExpenseDialog,
                onDescriptionChange = onDescriptionChange,
                onValueChange = onValueChange,
                onBillingDayChange = onBillingDayChange,
                onSave = onSaveNewExpense
            )
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
                style = MaterialTheme.typography.bodyLarge,
            )
            VerticalDivider(
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                    .height(dimensionResource(id = commonR.dimen.padding_medium))
            )
            Text(
                text = expense.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
            )
        }
        Text(
            text = valueWithCurrencyString(
                currencyStringId = commonR.string.brl_currency,
                value = expense.value
            ),
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun AddExpenseButton(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    newExpenseDescription: String,
    newExpenseValue: String,
    newExpenseBillingDay: String,
    onOpenDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onBillingDayChange: (String) -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        NewRecurringExpenseDialog(
            description = newExpenseDescription,
            value = newExpenseValue,
            selectedBillingDay = newExpenseBillingDay,
            onDescriptionChange = onDescriptionChange,
            onValueChange = onValueChange,
            onBillingDayChange = onBillingDayChange,
            onSaveButtonClick = onSave,
            onCancelButtonClick = onDismissDialog
        )
    }

    Button(
        onClick = onOpenDialog,
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
            )
            Text(
                text = stringResource(id = R.string.recurring_expense_add_button),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun NewRecurringExpenseDialog(
    description: String,
    value: String,
    selectedBillingDay: String,
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
                    value = description,
                    onValueChange = onDescriptionChange
                )
                ExpenseBillingDayDropdownMenu(
                    selectedBillingDay = selectedBillingDay,
                    onBillingDaySelected = onBillingDayChange
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.recurring_expense_value_hint)) },
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true
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
private fun ExpenseBillingDayDropdownMenu(
    selectedBillingDay: String,
    onBillingDaySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = List(31) { (it + 1).toString() }

    OutlinedTextField(
        value = selectedBillingDay.ifEmpty { stringResource(id = commonR.string.blank) },
        label = { Text(text = stringResource(id = R.string.card_bill_close_day_hint)) },
        onValueChange = { },
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
                    onBillingDaySelected(selectionOption)
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
    RecurringExpensesCard(
        expenses = listOf(
            RecurringExpense(description = "Aluguel", value = "1500.00", billingDay = "05"),
            RecurringExpense(description = "Internet", value = "100.00", billingDay = "10")
        ),
        totalExpensesValue = "1600.00",
        showAddExpenseDialog = false,
        newExpenseDescription = "",
        newExpenseValue = "",
        newExpenseBillingDay = "",
        onOpenAddExpenseDialog = {},
        onDismissAddExpenseDialog = {},
        onDescriptionChange = {},
        onValueChange = {},
        onBillingDayChange = {},
        onSaveNewExpense = {}
    )
}

@Preview
@Composable
private fun NewRecurringExpenseDialogPreview() {
    NewRecurringExpenseDialog(
        description = "",
        value = "",
        selectedBillingDay = "",
        onDescriptionChange = {},
        onValueChange = {},
        onBillingDayChange = {},
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}
