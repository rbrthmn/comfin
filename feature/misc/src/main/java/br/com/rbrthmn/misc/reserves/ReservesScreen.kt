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

package br.com.rbrthmn.misc.reserves

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.misc.R
import br.com.rbrthmn.misc.reserves.components.ReserveItem
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object ReservesDestination : NavigationDestination {
    override val route = "reserves"
}

const val NEW_RESERVE_DIALOG_TAG = "new_reserve_dialog"

@Composable
fun ReservesScreen(
    modifier: Modifier = Modifier,
    viewModel: ReservesContract.ViewModel = koinViewModel<ReservesContract.ViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()

    ReservesScreen(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun ReservesScreen(
    modifier: Modifier = Modifier,
    uiState: ReservesContract.UIState,
    onIntent: (ReservesContract.Intent) -> Unit
) {
    val totalReservesValue = uiState.reserves.sumOf { it.value.toDouble() }.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ReservesCard(
            reservesTotalValue = totalReservesValue,
            reserves = uiState.reserves,
            expandedReserveId = uiState.expandedReserveId,
            onReserveItemClick = { reserveId ->
                onIntent(ReservesContract.Intent.OnReserveItemClick(reserveId))
            },
            onAddReserveClick = {
                onIntent(ReservesContract.Intent.OnAddReserveButtonClick)
            }
        )
    }

    if (uiState.showNewReserveDialog) {
        NewReserveDialog(
            reserveName = uiState.newReserveName,
            reserveValue = uiState.newReserveValue,
            isReserveNameValid = uiState.isNewReserveNameValid,
            isReserveValueValid = uiState.isNewReserveValueValid,
            onReserveNameChange = { name ->
                onIntent(ReservesContract.Intent.OnNewReserveNameChange(name))
            },
            onReserveValueChange = { value ->
                onIntent(ReservesContract.Intent.OnNewReserveValueChange(value))
            },
            onSaveButtonClick = {
                onIntent(ReservesContract.Intent.OnSaveNewReserve)
            },
            onCancelButtonClick = {
                onIntent(ReservesContract.Intent.OnCancelNewReserve)
            }
        )
    }
}

@Composable
private fun ReservesCard(
    modifier: Modifier = Modifier,
    reservesTotalValue: String,
    reserves: List<Reserve>,
    expandedReserveId: String?,
    onReserveItemClick: (String) -> Unit,
    onAddReserveClick: () -> Unit
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
            modifier = modifier.padding(
                vertical = dimensionResource(id = commonR.dimen.padding_medium),
                horizontal = dimensionResource(id = commonR.dimen.padding_medium)
            )
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.reserve_total_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = commonR.dimen.font_size_large).value.sp
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = valueWithCurrencyString(
                        currencyStringId = commonR.string.brl_currency,
                        value = reservesTotalValue
                    ),
                    fontSize = dimensionResource(id = commonR.dimen.font_size_large).value.sp
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
                    ReserveItem(
                        reserve = reserve,
                        isExpanded = expandedReserveId == reserve.name,
                        onClick = { onReserveItemClick(reserve.name) }
                    )
                }
            }
            AddReserveButton(onAddReserveClick)
        }
    }
}

@Composable
private fun AddReserveButton(
    onAddReserveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAddReserveClick,
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
                text = stringResource(id = R.string.add_reserve_button),
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
            )
        }
    }
}

@Composable
private fun NewReserveDialog(
    reserveName: String,
    reserveValue: String,
    isReserveNameValid: Boolean,
    isReserveValueValid: Boolean,
    onReserveNameChange: (String) -> Unit,
    onReserveValueChange: (String) -> Unit,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = commonR.dimen.padding_medium))
                .testTag(NEW_RESERVE_DIALOG_TAG),
            shape = RoundedCornerShape(dimensionResource(id = commonR.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = commonR.dimen.padding_medium)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.new_reserve_name_hint)) },
                    value = reserveName,
                    onValueChange = onReserveNameChange,
                    isError = !isReserveNameValid,
                    singleLine = true
                )
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.new_reserve_value_hint)) },
                    value = reserveValue,
                    onValueChange = onReserveValueChange,
                    isError = !isReserveValueValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
private fun ReservesScreenPreview() {
    ReservesScreen(
        uiState = ReservesContract.UIState(
            reserves = listOf(
                Reserve(
                    name = "Emergency Fund",
                    value = "1000.00",
                    operations = listOf(
                        ReserveOperation(
                            date = "2023-11-15",
                            value = "200.00",
                            isWithdrawal = false
                        ),
                        ReserveOperation(
                            date = "2023-11-22",
                            value = "100.00",
                            isWithdrawal = true
                        ),
                        ReserveOperation(
                            date = "2023-12-01",
                            value = "300.00",
                            isWithdrawal = false
                        )
                    )
                ),
                Reserve(
                    name = "Birthday money",
                    value = "100.00",
                    operations = listOf()
                )
            )
        ),
        onIntent = {}
    )
}

@Preview
@Composable
private fun NewReserveDialogPreview() {
    NewReserveDialog(
        reserveName = "Reserva",
        reserveValue = "",
        onReserveNameChange = {},
        onReserveValueChange = {},
        onSaveButtonClick = {},
        onCancelButtonClick = {},
        isReserveNameValid = true,
        isReserveValueValid = true
    )
}
