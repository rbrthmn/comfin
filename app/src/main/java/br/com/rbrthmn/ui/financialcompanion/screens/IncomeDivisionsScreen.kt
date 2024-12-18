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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.MonthSelectionTopBar
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear

object IncomeDivisionsDestination : NavigationDestination {
    override val route = "income_divisions"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeDivisionsScreen(
    modifier: Modifier = Modifier,
    onRecurringExpensesDivisionClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        MonthSelectionTopBar(
            currentMonth = MonthsOfTheYear.JANUARY,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        IncomeDivisionsScreenContent(
            innerPaddingValues = innerPadding,
            modifier = modifier,
            onRecurringExpensesDivisionClick = onRecurringExpensesDivisionClick
        )
    }
}

@Composable
private fun IncomeDivisionsScreenContent(
    modifier: Modifier = Modifier,
    innerPaddingValues: PaddingValues,
    onRecurringExpensesDivisionClick: () -> Unit
) {
    val incomeDivisions = listOf(
        IncomeDivision(
            name = stringResource(id = R.string.total_income),
            value = "1000",
            percentage = "100",
            canEditPercentage = false,
        ),
        IncomeDivision(
            name = stringResource(id = R.string.recurring_expenses),
            value = "100",
            percentage = "10",
            canEditValue = false,
            isRecurringExpenses = true
        ),
        IncomeDivision(
            name = stringResource(id = R.string.remaining_month),
            value = "100",
            canEditValue = false,
            percentage = "10",
            canEditPercentage = false,
        ),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                for (division in incomeDivisions) {
                    with(division) {
                        IncomeDivision(
                            divisionName = name,
                            divisionValue = value,
                            divisionPercentage = percentage,
                            canEditValue = canEditValue,
                            canEditPercentage = canEditPercentage,
                            isRecurringExpenses = isRecurringExpenses,
                            onRecurringExpensesClick = onRecurringExpensesDivisionClick,
                            modifier = modifier
                        )
                    }
                    if (incomeDivisions.indexOf(division) == incomeDivisions.lastIndex - 1) {
                        HorizontalDivider(modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))
                        AddDivisionButton(modifier = modifier)
                    }
                    if (division != incomeDivisions.last()) {
                        HorizontalDivider(modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))
                    }
                }
            }
        }
    }
}

@Composable
private fun IncomeDivision(
    modifier: Modifier = Modifier,
    divisionName: String,
    divisionValue: String,
    divisionPercentage: String,
    canEditValue: Boolean,
    canEditPercentage: Boolean,
    isRecurringExpenses: Boolean = false,
    onRecurringExpensesClick: () -> Unit = { }
) {
    var value = divisionValue
    var percentage = divisionPercentage

    Row(
        modifier = if (isRecurringExpenses.not()) {
            modifier.fillMaxWidth()
        } else modifier
            .fillMaxWidth()
            .clickable {
                onRecurringExpensesClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = divisionName.plus(stringResource(id = R.string.colon)),
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
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
                readOnly = canEditValue,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
            VerticalDivider()
            TextField(
                suffix = { Text(text = "%") },
                value = percentage,
                onValueChange = { percentage = it },
                readOnly = canEditPercentage,
                maxLines = 1,
                modifier = modifier.weight(0.5f)
            )
        }
    }
}

@Composable
private fun AddDivisionButton(modifier: Modifier = Modifier) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        NewDivisionDialog(
            onSaveButtonClick = { showDialog.value = false },
            onCancelButtonClick = { showDialog.value = false })
    }

    TextButton(
        onClick = { showDialog.value = true },
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
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
                    contentDescription = stringResource(id = R.string.add_icon_description),
                    tint = Color.Gray,
                    modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                )
                Text(
                    text = stringResource(id = R.string.add_division),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Composable
private fun NewDivisionDialog(onSaveButtonClick: () -> Unit, onCancelButtonClick: () -> Unit) {
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
                    label = { Text(text = stringResource(id = R.string.division_name_hint)) },
                    value = "",
                    onValueChange = { }
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.division_value_hint)) },
                    value = "",
                    onValueChange = { },
                    singleLine = true
                )
                OutlinedTextField(
                    suffix = { Text(text = stringResource(id = R.string.division_percentage)) },
                    label = { Text(text = stringResource(id = R.string.division_percentage_hint)) },
                    value = "",
                    onValueChange = { }
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

@Preview
@Composable
fun IncomeDivisionsScreenPreview(modifier: Modifier = Modifier) {
    IncomeDivisionsScreen(modifier = Modifier, onRecurringExpensesDivisionClick = {})
}

@Preview
@Composable
fun NewDivisionDialogPreview(modifier: Modifier = Modifier) {
    NewDivisionDialog(onSaveButtonClick = { }, onCancelButtonClick = { })
}

class IncomeDivision(
    val name: String,
    val value: String,
    val canEditValue: Boolean = true,
    val percentage: String,
    val canEditPercentage: Boolean = true,
    val isRecurringExpenses: Boolean = false
)