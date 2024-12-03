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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString


@Composable
fun LastMonthDifferenceCard(differenceValue: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.padding_medium),
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
        ) {
            Text(
                text = stringResource(id = R.string.difference_from_last_month_title),
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.weight(0.6f)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(0.4f)) {
                Text(
                    text = valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency,
                        value = differenceValue
                    ),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun LastMonthDifferenceCardPreview(modifier: Modifier = Modifier) {
    LastMonthDifferenceCard(differenceValue = "-4.00", modifier = modifier)
}
