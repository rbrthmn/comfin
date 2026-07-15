package br.com.rbrthmn.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.navigation.NavigationDestination
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object SettingsDestination : NavigationDestination {
    override val route: String = "settings"
}

const val SETTINGS_LIST_TAG = "settings_list"
const val SETTINGS_LOGOUT_TAG = "settings_logout"

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit = {},
    viewModel: SettingsScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val themeUpdatedMessage = stringResource(id = R.string.settings_theme_updated)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SettingsScreenContract.Effect.NavigateToSignIn -> onNavigateToSignIn()
                SettingsScreenContract.Effect.ShowThemeUpdated ->
                    snackbarHostState.showSnackbar(message = themeUpdatedMessage)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsContent(
                showDarkModeDialog = uiState.showDarkModeDialog,
                selectedDarkModeOption = uiState.selectedDarkModeOption,
                showLogoutDialog = uiState.showLogoutDialog,
                onDarkModeSettingClick = { viewModel.onIntent(SettingsScreenContract.Intent.OnOpenDarkModeDialog) },
                onDarkModeDialogDismiss = { viewModel.onIntent(SettingsScreenContract.Intent.OnDismissDarkModeDialog) },
                onDarkModeOptionSelected = { viewModel.onIntent(SettingsScreenContract.Intent.OnSelectDarkModeOption(it)) },
                onLogoutClick = { viewModel.onIntent(SettingsScreenContract.Intent.OnLogoutClick) },
                onLogoutDialogDismiss = { viewModel.onIntent(SettingsScreenContract.Intent.OnDismissLogoutDialog) },
                onLogoutConfirm = { viewModel.onIntent(SettingsScreenContract.Intent.OnConfirmLogout) }
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    showDarkModeDialog: Boolean,
    selectedDarkModeOption: String,
    showLogoutDialog: Boolean,
    onDarkModeSettingClick: () -> Unit,
    onDarkModeDialogDismiss: () -> Unit,
    onDarkModeOptionSelected: (String) -> Unit,
    onLogoutClick: () -> Unit,
    onLogoutDialogDismiss: () -> Unit,
    onLogoutConfirm: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = commonR.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
            modifier = modifier
                .padding(
                    vertical = dimensionResource(id = commonR.dimen.padding_medium),
                    horizontal = dimensionResource(id = commonR.dimen.padding_medium),
                )
                .testTag(SETTINGS_LIST_TAG)
        ) {
            DarkModeSetting(
                showDialog = showDarkModeDialog,
                selectedOption = selectedDarkModeOption,
                onSettingClick = onDarkModeSettingClick,
                onDialogDismiss = onDarkModeDialogDismiss,
                onOptionSelected = onDarkModeOptionSelected
            )
            LogoutSetting(
                showDialog = showLogoutDialog,
                onSettingClick = onLogoutClick,
                onDialogDismiss = onLogoutDialogDismiss,
                onConfirm = onLogoutConfirm
            )
        }
    }
}

@Composable
private fun DarkModeSetting(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    selectedOption: String,
    onSettingClick: () -> Unit,
    onDialogDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDialogDismiss) {
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
                        selectedOption = selectedOption,
                        onOptionSelected = onOptionSelected
                    )
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSettingClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.settings_dark_mode),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = selectedOption,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LogoutSetting(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onSettingClick: () -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDialogDismiss,
            title = { Text(text = stringResource(id = R.string.settings_logout_dialog_title)) },
            text = { Text(text = stringResource(id = R.string.settings_logout_dialog_message)) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = stringResource(id = R.string.settings_logout_confirm_button),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDialogDismiss) {
                    Text(text = stringResource(id = commonR.string.cancel_button))
                }
            }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSettingClick() }
            .testTag(SETTINGS_LOGOUT_TAG),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.settings_logout),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun DarkModeRadioOptions(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val radioOptions = DarkModeOption.entries.map { it.label }
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
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
    SettingsContent(
        showDarkModeDialog = false,
        selectedDarkModeOption = DarkModeOption.SYSTEM.label,
        showLogoutDialog = false,
        onDarkModeSettingClick = {},
        onDarkModeDialogDismiss = {},
        onDarkModeOptionSelected = {},
        onLogoutClick = {},
        onLogoutDialogDismiss = {},
        onLogoutConfirm = {}
    )
}
