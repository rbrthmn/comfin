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

package br.com.rbrthmn.ui.financialcompanion.screens.reserves

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import br.com.rbrthmn.ui.financialcompanion.screens.operations.components.AddOperationDialog
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.model.OperationType
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString
import java.text.SimpleDateFormat
import java.util.Locale

data class Reserve(
    val name: String,
    val value: String,
    val operations: List<ReserveOperation> = listOf()
)

data class ReserveOperation(val date: String, val value: String, val isWithdrawal: Boolean)

object ReservesDestination : NavigationDestination {
    override val route = "reserves"
}

@Composable
fun ReservesScreen(modifier: Modifier = Modifier) {
    val reserveA = Reserve(
        name = "Emergency Fund",
        value = "1000.00",
        operations = listOf(
            ReserveOperation(date = "2023-11-15", value = "200.00", isWithdrawal = false),
            ReserveOperation(date = "2023-11-22", value = "100.00", isWithdrawal = true),
            ReserveOperation(date = "2023-12-01", value = "300.00", isWithdrawal = false)
        )
    )
    val reserveB = Reserve(
        name = "Birthday money",
        value = "100.00",
        operations = listOf()
    )
    val reserves = listOf(reserveA, reserveB)
    val totalReservesValue = reserves.sumOf { it.value.toDouble() }.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ReservesCard(reservesTotalValue = totalReservesValue, reserves = reserves)
    }
}

@Composable
private fun ReservesCard(
    modifier: Modifier = Modifier,
    reservesTotalValue: String,
    reserves: List<Reserve>
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
            modifier = modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_medium),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.reserve_total_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency,
                        value = reservesTotalValue
                    ),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp
                )
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
            ) {
                for (reserve in reserves) {
                    ReserveItem(reserve = reserve)
                }
            }
            AddReserveButton()
        }
    }
}

@Composable
private fun AddReserveButton(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog)
        NewReserveDialog(
            onSaveButtonClick = { showDialog = false },
            onCancelButtonClick = { showDialog = false })

    Button(
        onClick = { showDialog = true },
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
                text = stringResource(id = R.string.add_reserve_button),
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
            )
        }
    }
}

@Composable
private fun NewReserveDialog(onSaveButtonClick: () -> Unit, onCancelButtonClick: () -> Unit) {
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
                    label = { Text(text = stringResource(id = R.string.new_reserve_name_hint)) },
                    value = "",
                    onValueChange = { }
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.new_reserve_value_hint)) },
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
private fun ReserveItem(modifier: Modifier = Modifier, reserve: Reserve) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = ""
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_shape_round)))
            .clickable { isExpanded = !isExpanded }
            .padding(end = dimensionResource(id = R.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow_icon_description),
                modifier = modifier
                    .alpha(0.2f)
                    .rotate(rotationState)
                    .padding(dimensionResource(id = R.dimen.padding_extra_small)),
            )
            Text(
                text = reserve.name,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = valueWithCurrencyString(
                currencyStringId = R.string.brl_currency, value = reserve.value
            ),
            maxLines = 1,
            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    if (isExpanded) {
        ReserveOperationsList(reserve, modifier)
    }
}

@Composable
private fun ReserveOperationsList(
    reserve: Reserve,
    modifier: Modifier = Modifier
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AddOperationDialog(
            onSaveButtonClick = { showDialog.value = false },
            onCancelButtonClick = { showDialog.value = false },
            availableOperationTypes = listOf(
                OperationType.RESERVE_ALLOCATION,
                OperationType.RESERVE_WITHDRAWAL
            )
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_extra_small))
    ) {
        for (operation in reserve.operations) {
            val operationValueColor = if (operation.isWithdrawal) Color.Red else Color.Green
            val operationValueSymbol = if (operation.isWithdrawal) "-" else "+"

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                    .padding(vertical = dimensionResource(id = R.dimen.padding_extra_small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(operation.date)!!
                    ),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
                Text(
                    text = operationValueSymbol + valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency,
                        value = operation.value
                    ),
                    color = operationValueColor,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
        TextButton(
            onClick = { showDialog.value = true },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
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
                )
                Text(
                    text = stringResource(id = R.string.add_operation_button),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReservesScreenPreview(modifier: Modifier = Modifier) {
    ReservesScreen()
}

@Preview
@Composable
private fun NewReserveDialogPreview(modifier: Modifier = Modifier) {
    NewReserveDialog(onSaveButtonClick = {}, onCancelButtonClick = {})
}
