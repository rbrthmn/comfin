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

package br.com.rbrthmn.settings.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.settings.R
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object SettingsDestination : NavigationDestination {
    override val route: String = "settings"
}

const val SETTINGS_LIST_TAG = "settings_list"

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsContract.ViewModel = koinViewModel()
) {
    val uiState = (viewModel.uiState).collectAsState().value

    SettingsScreen(
        uiState = uiState,
        onDismissRequest = { viewModel.onIntent(SettingsContract.Intent.OnDismissDarkModeDialog) },
        onOptionSelected = { option ->
            viewModel.onIntent(
                SettingsContract.Intent.OnThemeSelected(
                    option
                )
            )
        },
        onDarkModeClick = {
            Log.d("SettingsScreen", "Dark Mode Clicked")
            viewModel.onIntent(SettingsContract.Intent.OnDarkModeClick)
        },
        modifier = modifier
    )
}

@Composable
private fun SettingsScreen(
    uiState: SettingsContract.UiState,
    onDismissRequest: () -> Unit,
    onOptionSelected: (SettingsContract.ThemeOption) -> Unit,
    onDarkModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SettingsContent(
            uiState = uiState,
            onDismissRequest = onDismissRequest,
            onOptionSelected = onOptionSelected,
            onDarkModeClick = onDarkModeClick,
            modifier = modifier
        )
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsContract.UiState,
    onDismissRequest: () -> Unit,
    onOptionSelected: (SettingsContract.ThemeOption) -> Unit,
    onDarkModeClick: () -> Unit,
    modifier: Modifier = Modifier,
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
            modifier = modifier
                .padding(
                    vertical = dimensionResource(id = commonR.dimen.padding_medium),
                    horizontal = dimensionResource(id = commonR.dimen.padding_medium),
                )
                .testTag(SETTINGS_LIST_TAG)
        ) {
            DarkModeSetting(
                uiState = uiState,
                onDismissRequest = onDismissRequest,
                onOptionSelected = onOptionSelected,
                onDarkModeClick = onDarkModeClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DarkModeSetting(
    uiState: SettingsContract.UiState,
    onDismissRequest: () -> Unit,
    onOptionSelected: (SettingsContract.ThemeOption) -> Unit,
    onDarkModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showDarkModeDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
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
                    DarkModeRadioOptions(
                        selected = uiState.selectedTheme,
                        onOptionSelected = { option ->
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDarkModeClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.settings_dark_mode),
            fontSize = dimensionResource(id = commonR.dimen.font_size_large).value.sp
        )
        Text(
            text = when (uiState.selectedTheme) {
                SettingsContract.ThemeOption.System -> stringResource(id = R.string.theme_option_system)
                SettingsContract.ThemeOption.Light -> stringResource(id = R.string.theme_option_light)
                SettingsContract.ThemeOption.Dark -> stringResource(id = R.string.theme_option_dark)
            },
            fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
        )
    }
}

@Composable
private fun DarkModeRadioOptions(
    selected: SettingsContract.ThemeOption,
    onOptionSelected: (SettingsContract.ThemeOption) -> Unit
) {
    val radioOptions = listOf(
        SettingsContract.ThemeOption.System to stringResource(id = R.string.theme_option_system),
        SettingsContract.ThemeOption.Light to stringResource(id = R.string.theme_option_light),
        SettingsContract.ThemeOption.Dark to stringResource(id = R.string.theme_option_dark)
    )
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { (option, text) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (option == selected),
                        onClick = { onOptionSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selected),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        uiState = SettingsContract.UiState(),
        onDismissRequest = {},
        onOptionSelected = {},
        onDarkModeClick = {}
    )
}