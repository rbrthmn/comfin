package br.com.rbrthmn.settings

import br.com.rbrthmn.ui.BaseViewModel

interface SettingsScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>()

    data class UiState(
        val showDarkModeDialog: Boolean = false,
        val selectedDarkModeOption: String = DarkModeOption.SYSTEM.label
    )

    sealed class Intent {
        object OnOpenDarkModeDialog : Intent()
        object OnDismissDarkModeDialog : Intent()
        data class OnSelectDarkModeOption(val option: String) : Intent()
    }
}

enum class DarkModeOption(val label: String) {
    SYSTEM("Padrão do sistema"),
    LIGHT("Claro"),
    DARK("Noturno")
}
