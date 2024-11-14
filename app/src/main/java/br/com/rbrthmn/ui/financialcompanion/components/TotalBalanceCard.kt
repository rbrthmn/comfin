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

package br.com.rbrthmn.ui.financialcompanion.components

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
import br.com.rbrthmn.R


@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    totalBalance: String,
    incomes: String,
    outflow: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = totalBalance,
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                )
                Text(
                    text = stringResource(id = R.string.balance_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_small))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = incomes,
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.incomes_title),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = outflow,
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                    Text(
                        text = stringResource(id = R.string.outflow_title),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TotalBalanceCardPreview(modifier: Modifier = Modifier) {
    TotalBalanceCard(
        totalBalance = "R$ 1.000,00",
        incomes = "R$ 2.000,00",
        outflow = "R$ 1.000"
    )
}
