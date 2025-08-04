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

package br.com.rbrthmn.operations.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.operations.OperationsScreenContract
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import br.com.rbrthmn.ui.R as uiR

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    uiState: OperationsScreenContract.UiState,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = uiR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = uiR.dimen.padding_medium))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(id = uiR.dimen.padding_small))
            ) {
                Text(
                    text = valueWithCurrencyString(
                        currencyStringId = uiR.string.brl_currency,
                        value = uiState.totalBalance
                    ),
                    fontSize = dimensionResource(id = uiR.dimen.font_size_large).value.sp,
                )
                Text(
                    text = stringResource(id = R.string.balance_title),
                    fontSize = dimensionResource(id = uiR.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = uiR.dimen.padding_small))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = valueWithCurrencyString(
                            currencyStringId = uiR.string.brl_currency,
                            value = uiState.totalIncome
                        ),
                        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.incomes_title),
                        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = valueWithCurrencyString(
                            currencyStringId = uiR.string.brl_currency,
                            value = uiState.totalOutcome
                        ),
                        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.outflow_title),
                        fontSize = dimensionResource(id = uiR.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TotalBalanceCardPreview() {
    TotalBalanceCard(
        uiState = OperationsScreenContract.UiState(
            totalBalance = "100,00",
            totalOutcome = "50,00",
            totalIncome = "50,00"
        )
    )
}
