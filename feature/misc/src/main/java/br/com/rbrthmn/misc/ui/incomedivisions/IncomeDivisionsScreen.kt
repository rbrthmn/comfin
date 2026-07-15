package br.com.rbrthmn.misc.incomedivisions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.misc.R
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.ui.components.MonthSelectionTopBar
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import br.com.rbrthmn.ui.R as commonR

const val RECURRING_EXPENSES_DIVISION_TAG = "recurring_expenses_division"

object IncomeDivisionsDestination : NavigationDestination {
    override val route = "income_divisions"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeDivisionsScreen(
    modifier: Modifier = Modifier,
    onRecurringExpensesDivisionClick: () -> Unit,
    viewModel: IncomeDivisionsScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        MonthSelectionTopBar(
            initialDate = uiState.selectedDate,
            onDateSelected = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnDateSelected(it)) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        IncomeDivisionsScreenContent(
            innerPaddingValues = innerPadding,
            incomeDivisions = uiState.incomeDivisions,
            showAddDivisionDialog = uiState.showAddDivisionDialog,
            newDivisionName = uiState.newDivisionName,
            newDivisionValue = uiState.newDivisionValue,
            newDivisionPercentage = uiState.newDivisionPercentage,
            onRecurringExpensesDivisionClick = onRecurringExpensesDivisionClick,
            onOpenAddDivisionDialog = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnOpenAddDivisionDialog) },
            onDismissAddDivisionDialog = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnDismissAddDivisionDialog) },
            onNewDivisionNameChange = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnNewDivisionNameChange(it)) },
            onNewDivisionValueChange = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnNewDivisionValueChange(it)) },
            onNewDivisionPercentageChange = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnNewDivisionPercentageChange(it)) },
            onSaveNewDivision = { viewModel.onIntent(IncomeDivisionsScreenContract.Intent.OnSaveNewDivision) },
            modifier = modifier
        )
    }
}

@Composable
private fun IncomeDivisionsScreenContent(
    modifier: Modifier = Modifier,
    innerPaddingValues: PaddingValues,
    incomeDivisions: List<IncomeDivision>,
    showAddDivisionDialog: Boolean,
    newDivisionName: String,
    newDivisionValue: String,
    newDivisionPercentage: String,
    onRecurringExpensesDivisionClick: () -> Unit,
    onOpenAddDivisionDialog: () -> Unit,
    onDismissAddDivisionDialog: () -> Unit,
    onNewDivisionNameChange: (String) -> Unit,
    onNewDivisionValueChange: (String) -> Unit,
    onNewDivisionPercentageChange: (String) -> Unit,
    onSaveNewDivision: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            modifier = modifier
                .padding(vertical = dimensionResource(id = commonR.dimen.padding_medium))
                .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(dimensionResource(id = commonR.dimen.padding_medium))
            ) {
                incomeDivisions.forEachIndexed { index, division ->
                    IncomeDivisionItem(
                        data = division,
                        onRecurringExpensesClick = onRecurringExpensesDivisionClick
                    )
                    if (index == incomeDivisions.lastIndex - 1) {
                        HorizontalDivider(modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_small)))
                        AddDivisionButton(
                            showDialog = showAddDivisionDialog,
                            newDivisionName = newDivisionName,
                            newDivisionValue = newDivisionValue,
                            newDivisionPercentage = newDivisionPercentage,
                            onOpenDialog = onOpenAddDivisionDialog,
                            onDismissDialog = onDismissAddDivisionDialog,
                            onNameChange = onNewDivisionNameChange,
                            onValueChange = onNewDivisionValueChange,
                            onPercentageChange = onNewDivisionPercentageChange,
                            onSave = onSaveNewDivision,
                            modifier = modifier
                        )
                    }
                    if (index != incomeDivisions.lastIndex) {
                        HorizontalDivider(modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_small)))
                    }
                }
            }
        }
    }
}

@Composable
private fun IncomeDivisionItem(
    modifier: Modifier = Modifier,
    data: IncomeDivision,
    onRecurringExpensesClick: () -> Unit
) {
    var value = data.value
    var percentage = data.percentage

    Row(
        modifier = if (data.isRecurringExpenses) {
            modifier
                .testTag(RECURRING_EXPENSES_DIVISION_TAG)
                .fillMaxWidth()
                .clickable { onRecurringExpensesClick() }
        } else modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = data.name.plus(stringResource(id = commonR.string.colon)),
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.weight(0.4F)
        )
        Row(
            modifier = modifier.weight(0.6F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                prefix = { Text(text = "R$") },
                value = value,
                onValueChange = { value = it },
                readOnly = data.canEditValue,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
            VerticalDivider()
            TextField(
                suffix = { Text(text = "%") },
                value = percentage,
                onValueChange = { percentage = it },
                readOnly = data.canEditPercentage,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
        }
    }
}

@Composable
private fun AddDivisionButton(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    newDivisionName: String,
    newDivisionValue: String,
    newDivisionPercentage: String,
    onOpenDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onPercentageChange: (String) -> Unit,
    onSave: () -> Unit
) {
    if (showDialog) {
        NewDivisionDialog(
            name = newDivisionName,
            value = newDivisionValue,
            percentage = newDivisionPercentage,
            onNameChange = onNameChange,
            onValueChange = onValueChange,
            onPercentageChange = onPercentageChange,
            onSaveButtonClick = onSave,
            onCancelButtonClick = onDismissDialog
        )
    }

    TextButton(
        onClick = onOpenDialog,
        contentPadding = PaddingValues(dimensionResource(id = commonR.dimen.zero_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = commonR.string.add_icon_description),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                )
                Text(
                    text = stringResource(id = R.string.add_division),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun NewDivisionDialog(
    name: String,
    value: String,
    percentage: String,
    onNameChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onPercentageChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = commonR.dimen.padding_medium)),
            shape = RoundedCornerShape(dimensionResource(id = commonR.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = commonR.dimen.padding_medium)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.division_name_hint)) },
                    value = name,
                    onValueChange = onNameChange
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.division_value_hint)) },
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true
                )
                OutlinedTextField(
                    suffix = { Text(text = stringResource(id = R.string.division_percentage)) },
                    label = { Text(text = stringResource(id = R.string.division_percentage_hint)) },
                    value = percentage,
                    onValueChange = onPercentageChange
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

@Preview
@Composable
fun IncomeDivisionsScreenPreview() {
    IncomeDivisionsScreenContent(
        innerPaddingValues = PaddingValues(),
        incomeDivisions = listOf(
            IncomeDivision(name = "Renda Total", value = "1000", percentage = "100", canEditPercentage = false),
            IncomeDivision(name = "Gastos Recorrentes", value = "100", percentage = "10", canEditValue = false, isRecurringExpenses = true),
            IncomeDivision(name = "Restante do mês", value = "100", canEditValue = false, percentage = "10", canEditPercentage = false)
        ),
        showAddDivisionDialog = false,
        newDivisionName = "",
        newDivisionValue = "",
        newDivisionPercentage = "",
        onRecurringExpensesDivisionClick = {},
        onOpenAddDivisionDialog = {},
        onDismissAddDivisionDialog = {},
        onNewDivisionNameChange = {},
        onNewDivisionValueChange = {},
        onNewDivisionPercentageChange = {},
        onSaveNewDivision = {}
    )
}

@Preview
@Composable
fun NewDivisionDialogPreview() {
    NewDivisionDialog(
        name = "",
        value = "",
        percentage = "",
        onNameChange = {},
        onValueChange = {},
        onPercentageChange = {},
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}
