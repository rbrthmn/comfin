package br.com.rbrthmn.settings.ui

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : SettingsContract.ViewModel() {
    override val uiState: StateFlow<SettingsContract.UiState> =
        MutableStateFlow(SettingsContract.UiState())

    private val mutableState get() = uiState as MutableStateFlow

    override fun doOnInit(): SettingsViewModel {
        return this
    }

    override fun onIntent(intent: SettingsContract.Intent) {
        when (intent) {
            is SettingsContract.Intent.OnDarkModeClick -> onDarkModeClick()
            is SettingsContract.Intent.OnDismissDarkModeDialog -> onDismissDarkModeDialog()
            is SettingsContract.Intent.OnThemeSelected -> onThemeSelected(intent.option)
        }
    }

    private fun onDarkModeClick() {
        mutableState.update { it.copy(showDarkModeDialog = true) }
        Log.d("DarkMode", "onDarkModeClick: ${uiState.value.selectedTheme}")
    }

    private fun onDismissDarkModeDialog() {
        Log.d("DarkMode", "onDismissDarkModeDialog: ${uiState.value.selectedTheme}")
        mutableState.update { it.copy(showDarkModeDialog = false) }
    }

    private fun onThemeSelected(option: SettingsContract.ThemeOption) {
        Log.d("DarkMode", "onThemeSelected: $option")
        mutableState.update { it.copy(selectedTheme = option, showDarkModeDialog = false) }
    }
}
