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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination

data class FeatureLabel(val name: String, val route: String)

object MoreFeaturesDestination : NavigationDestination {
    override val route = "more_features"
}

@Composable
fun MoreFeaturesScreen(onFeatureClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MoreFeaturesCard(onFeatureClick)
    }
}
@Composable
private fun MoreFeaturesCard(onFeatureClick: (String) -> Unit) {
    val featuresList = listOf(
        FeatureLabel(stringResource(id = R.string.feature_label_reserves), ReservesDestination.route),
        FeatureLabel(stringResource(id = R.string.feature_label_recurring_expenses), RecurringExpensesDestination.route),
        FeatureLabel(stringResource(id = R.string.feature_label_income_distribution), IncomeDivisionsDestination.route),
        FeatureLabel(stringResource(id = R.string.feature_label_settings), SettingsDestination.route)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
        ) {
            for (index in featuresList.indices) {
                TextButton(
                    onClick = { onFeatureClick(featuresList[index].route) },
                    contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
                ) {
                    Text(text = featuresList[index].name, modifier = Modifier.fillMaxWidth())
                }
                if (index < featuresList.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreFeaturesScreenPreview(modifier: Modifier = Modifier) {
    MoreFeaturesScreen(onFeatureClick = {})
}