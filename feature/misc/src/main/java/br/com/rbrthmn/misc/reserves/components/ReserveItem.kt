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

package br.com.rbrthmn.misc.reserves.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.misc.reserves.Reserve
import br.com.rbrthmn.misc.reserves.ReserveOperation
import br.com.rbrthmn.operations.ui.OperationType
import br.com.rbrthmn.operations.ui.OperationsScreenContract
import br.com.rbrthmn.operations.ui.OperationsScreenViewModel
import br.com.rbrthmn.operations.ui.components.AddOperationDialog
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import br.com.rbrthmn.operations.R as operationR
import br.com.rbrthmn.ui.R as commonR

@Composable
fun ReserveItem(
    modifier: Modifier = Modifier,
    reserve: Reserve,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val operationsScreenViewModel: OperationsScreenContract.ViewModel =
        koinViewModel()
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = ""
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = commonR.dimen.corner_shape_round)))
            .clickable { onClick() }
            .padding(end = dimensionResource(id = commonR.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(id = commonR.string.drop_down_arrow_icon_description),
                modifier = modifier
                    .alpha(0.2f)
                    .rotate(rotationState)
                    .padding(dimensionResource(id = commonR.dimen.padding_extra_small)),
            )
            Text(
                text = reserve.name,
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = valueWithCurrencyString(
                currencyStringId = commonR.string.brl_currency, value = reserve.value
            ),
            maxLines = 1,
            fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
            fontWeight = FontWeight.Bold,
        )
    }

    if (isExpanded) {
        ReserveOperationsList(reserve, operationsScreenViewModel, modifier)
    }
}

@Composable
private fun ReserveOperationsList(
    reserve: Reserve,
    operationsScreenViewModel: OperationsScreenContract.ViewModel,
    modifier: Modifier = Modifier
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AddOperationDialog(
            uiState = operationsScreenViewModel.uiState.collectAsState().value,
            onSaveButtonClick = {
                operationsScreenViewModel.onIntent(
                    OperationsScreenContract.Intent.OnSaveButtonClick(
                        showDialog
                    )
                )
            },
            onCancelButtonClick = {
                showDialog.value = false
                operationsScreenViewModel.onIntent(OperationsScreenContract.Intent.OnResetDialogFields)
            },
            onDescriptionChange = {
                operationsScreenViewModel.onIntent(
                    OperationsScreenContract.Intent.OnDescriptionChange(
                        it
                    )
                )
            },
            onTypeChange = {
                operationsScreenViewModel.onIntent(
                    OperationsScreenContract.Intent.OnOperationTypeChange(
                        it
                    )
                )
            },
            onValueChange = {
                operationsScreenViewModel.onIntent(OperationsScreenContract.Intent.OnValueChange(it))
            },
            onDateChange = {
                operationsScreenViewModel.onIntent(
                    OperationsScreenContract.Intent.OnOperationDateChange(
                        it
                    )
                )
            },
            availableOperationTypes = listOf(
                OperationType.RESERVE_ALLOCATION,
                OperationType.RESERVE_WITHDRAWAL
            )
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_extra_small))
    ) {
        for (operation in reserve.operations) {
            val operationValueColor = if (operation.isWithdrawal) Color.Red else Color.Green
            val operationValueSymbol = if (operation.isWithdrawal) "-" else "+"

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
                    .padding(vertical = dimensionResource(id = commonR.dimen.padding_extra_small)),
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
                    fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
                )
                Text(
                    text = operationValueSymbol + valueWithCurrencyString(
                        currencyStringId = commonR.string.brl_currency,
                        value = operation.value
                    ),
                    color = operationValueColor,
                    fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
                )
            }
        }
        TextButton(
            onClick = { showDialog.value = true },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
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
                )
                Text(
                    text = stringResource(id = operationR.string.add_operation_button),
                    fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun ReserveOperationsListPreview() {
    val context = LocalContext.current
    val stringProvider = ResourceStringProvider(context)
    ReserveOperationsList(
        reserve = Reserve(
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
        operationsScreenViewModel = OperationsScreenViewModel(stringProvider)
    )
}