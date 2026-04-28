package br.com.rbrthmn.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SettingsScreenViewModel : SettingsScreenContract.ViewModel() {
    override var uiState = MutableStateFlow(SettingsScreenContract.UiState())

    override fun doOnInit(): SettingsScreenViewModel {
        return this
    }

    override fun onIntent(intent: SettingsScreenContract.Intent) {
        when (intent) {
            SettingsScreenContract.Intent.OnOpenDarkModeDialog ->
                uiState.update { it.copy(showDarkModeDialog = true) }
            SettingsScreenContract.Intent.OnDismissDarkModeDialog ->
                uiState.update { it.copy(showDarkModeDialog = false) }
            is SettingsScreenContract.Intent.OnSelectDarkModeOption ->
                uiState.update {
                    it.copy(
                        selectedDarkModeOption = intent.option,
                        showDarkModeDialog = false
                    )
                }
        }
    }
}
