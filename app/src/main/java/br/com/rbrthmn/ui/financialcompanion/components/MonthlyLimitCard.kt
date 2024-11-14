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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString

@Composable
fun MonthlyLimitCard(
    onCardClick: () -> Unit,
    monthLimit: String,
    monthDifference: String,
    modifier: Modifier = Modifier
) {
    val showMonthlyLimitDialog = remember { mutableStateOf(false) }
    val showMonthlyDifferenceDialog = remember { mutableStateOf(false) }

    if (showMonthlyLimitDialog.value)
        InfoDialog(
            dialogText = stringResource(id = R.string.monthly_limit_dialog_text),
            onCloseButtonClick = { showMonthlyLimitDialog.value = false }
        )

    if (showMonthlyDifferenceDialog.value)
        InfoDialog(
            dialogText = stringResource(id = R.string.monthly_difference_dialog_text),
            onCloseButtonClick = { showMonthlyDifferenceDialog.value = false }
        )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
            .clickable { onCardClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.monthly_limit_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = stringResource(id = R.string.help_icon_description),
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_extra_small))
                        .clickable { showMonthlyLimitDialog.value = true }
                )
            }
            Text(
                text = valueWithCurrencyString(
                    currencyStringId = R.string.brl_currency,
                    value = monthLimit
                ),
                fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(
                    bottom = dimensionResource(id = R.dimen.padding_medium),
                    top = dimensionResource(id = R.dimen.padding_extra_small)
                )
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(id = R.string.difference_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = stringResource(id = R.string.help_icon_description),
                    tint = Color.Gray,
                    modifier = modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_extra_small))
                        .clickable { showMonthlyDifferenceDialog.value = true }
                )
            }
            Text(
                text = valueWithCurrencyString(
                    currencyStringId = R.string.brl_currency,
                    value = monthDifference
                ),
                fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_extra_small))
            )
        }
    }
}

@Composable
private fun InfoDialog(dialogText: String, onCloseButtonClick: () -> Unit) {
    Dialog(onDismissRequest = onCloseButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = dialogText)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCloseButtonClick) {
                        Text(text = stringResource(id = R.string.understood_button))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MonthlyLimitCardPreview(modifier: Modifier = Modifier) {
    MonthlyLimitCard({}, "323", "323")
}

@Preview
@Composable
fun MonthlyLimitDialogPreview() {
    InfoDialog(stringResource(id = R.string.monthly_limit_dialog_text)) { }
}

@Preview
@Composable
fun MonthlyDifferenceDialogPreview() {
    InfoDialog(stringResource(id = R.string.monthly_difference_dialog_text)) {}
}
