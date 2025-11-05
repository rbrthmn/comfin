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

package br.com.rbrthmn.misc.ui.incomedivisions

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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

@Composable
fun IncomeDivisionsScreen(
    modifier: Modifier = Modifier,
    viewModel: IncomeDivisionsContract.ViewModel = koinViewModel<IncomeDivisionsContract.ViewModel>(),
    onRecurringExpensesDivisionClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    IncomeDivisionsScreen(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onRecurringExpensesDivisionClick = onRecurringExpensesDivisionClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeDivisionsScreen(
    modifier: Modifier = Modifier,
    uiState: IncomeDivisionsContract.UIState,
    onIntent: (IncomeDivisionsContract.Intent) -> Unit,
    onRecurringExpensesDivisionClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        MonthSelectionTopBar(
            initialDate = LocalDate.now(),
            onDateSelected = {},
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = modifier
                    .padding(vertical = dimensionResource(id = commonR.dimen.padding_medium))
                    .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(dimensionResource(id = commonR.dimen.padding_medium))
                ) {
                    uiState.incomeDivisions.forEachIndexed { index, division ->
                        IncomeDivision(
                            index = index,
                            data = division,
                            onIntent = onIntent,
                            onRecurringExpensesDivisionClick = onRecurringExpensesDivisionClick
                        )
                        if (index == uiState.incomeDivisions.lastIndex - 1) {
                            HorizontalDivider(modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_small)))
                            AddDivisionButton(
                                modifier = modifier,
                                onClick = { onIntent(IncomeDivisionsContract.Intent.OnAddDivisionButtonClick) }
                            )
                        }
                        if (index != uiState.incomeDivisions.lastIndex) {
                            HorizontalDivider(modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_small)))
                        }
                    }
                }
            }
            if (uiState.showNewDivisionDialog) {
                NewDivisionDialog(
                    uiState = uiState,
                    onNameChange = {
                        onIntent(
                            IncomeDivisionsContract.Intent.OnNewDivisionNameChange(
                                it
                            )
                        )
                    },
                    onValueChange = {
                        onIntent(
                            IncomeDivisionsContract.Intent.OnNewDivisionValueChange(
                                it
                            )
                        )
                    },
                    onPercentageChange = {
                        onIntent(
                            IncomeDivisionsContract.Intent.OnNewDivisionPercentageChange(
                                it
                            )
                        )
                    },
                    onSaveButtonClick = { onIntent(IncomeDivisionsContract.Intent.OnSaveNewDivision) },
                    onCancelButtonClick = { onIntent(IncomeDivisionsContract.Intent.OnCancelNewDivision) }
                )
            }
        }
    }
}

@Composable
private fun IncomeDivision(
    modifier: Modifier = Modifier,
    index: Int,
    data: IncomeDivision,
    onIntent: (IncomeDivisionsContract.Intent) -> Unit,
    onRecurringExpensesDivisionClick: () -> Unit
) {
    Row(
        modifier = if (data.isRecurringExpenses) {
            modifier
                .testTag(RECURRING_EXPENSES_DIVISION_TAG)
                .fillMaxWidth()
                .clickable { onRecurringExpensesDivisionClick() }
        } else modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = data.name.plus(stringResource(id = commonR.string.colon)),
            fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
            modifier = modifier.weight(0.4F)
        )
        Row(
            modifier = modifier.weight(0.6F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                prefix = { Text(text = "R$") },
                value = data.value,
                onValueChange = {
                    onIntent(
                        IncomeDivisionsContract.Intent.OnDivisionValueChanged(
                            index,
                            it
                        )
                    )
                },
                readOnly = !data.canEditValue,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
            VerticalDivider()
            TextField(
                suffix = { Text(text = "%") },
                value = data.percentage,
                onValueChange = {
                    onIntent(
                        IncomeDivisionsContract.Intent.OnDivisionPercentageChanged(
                            index,
                            it
                        )
                    )
                },
                readOnly = !data.canEditPercentage,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
        }
    }
}

@Composable
private fun AddDivisionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
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
                    tint = Color.Gray,
                    modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
                )
                Text(
                    text = stringResource(id = R.string.add_division),
                    fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Composable
private fun NewDivisionDialog(
    uiState: IncomeDivisionsContract.UIState,
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
                    value = uiState.newDivisionName,
                    onValueChange = onNameChange,
                    isError = !uiState.isNewDivisionNameValid
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.division_value_hint)) },
                    value = uiState.newDivisionValue,
                    onValueChange = onValueChange,
                    isError = !uiState.isNewDivisionValueValid,
                    singleLine = true
                )
                OutlinedTextField(
                    suffix = { Text(text = stringResource(id = R.string.division_percentage)) },
                    label = { Text(text = stringResource(id = R.string.division_percentage_hint)) },
                    value = uiState.newDivisionPercentage,
                    onValueChange = onPercentageChange,
                    isError = !uiState.isNewDivisionPercentageValid
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

@Preview(showBackground = true)
@Composable
private fun IncomeDivisionsScreenPreview() {
    IncomeDivisionsScreen(
        uiState = IncomeDivisionsContract.UIState(
            incomeDivisions = listOf(
                IncomeDivision(
                    name = "Total income",
                    value = "1000",
                    percentage = "100",
                    canEditValue = true,
                    canEditPercentage = false,
                ),
                IncomeDivision(
                    name = "Recurring expenses",
                    value = "100",
                    percentage = "10",
                    canEditValue = false,
                    isRecurringExpenses = true,
                ),
                IncomeDivision(
                    name = "Remaining for the month",
                    value = "100",
                    canEditValue = false,
                    percentage = "10",
                    canEditPercentage = false,
                ),
            )
        ),
        onIntent = {},
        onRecurringExpensesDivisionClick = {}
    )
}

@Preview
@Composable
fun NewDivisionDialogPreview() {
    NewDivisionDialog(
        uiState = IncomeDivisionsContract.UIState(),
        onNameChange = {},
        onValueChange = {},
        onPercentageChange = {},
        onSaveButtonClick = {},
        onCancelButtonClick = {}
    )
}
