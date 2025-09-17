package br.com.rbrthmn.settings.ui

import br.com.rbrthmn.ui.BaseViewModel

interface SettingsContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val showDarkModeDialog: Boolean = false,
        val selectedTheme: ThemeOption = ThemeOption.System
    )

    enum class ThemeOption { System, Light, Dark }

    sealed class Intent {
        data object OnDarkModeClick : Intent()
        data object OnDismissDarkModeDialog : Intent()
        data class OnThemeSelected(val option: ThemeOption) : Intent()
    }
}
