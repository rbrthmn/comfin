package br.com.rbrthmn.settings

import br.com.rbrthmn.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow

interface SettingsScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>() {
        abstract val effects: Flow<Effect>
    }

    data class UiState(
        val showDarkModeDialog: Boolean = false,
        val selectedDarkModeOption: String = DarkModeOption.SYSTEM.label,
        val showLogoutDialog: Boolean = false
    )

    sealed class Intent {
        object OnOpenDarkModeDialog : Intent()
        object OnDismissDarkModeDialog : Intent()
        data class OnSelectDarkModeOption(val option: String) : Intent()
        object OnLogoutClick : Intent()
        object OnDismissLogoutDialog : Intent()
        object OnConfirmLogout : Intent()
    }

    sealed class Effect {
        object NavigateToSignIn : Effect()
        object ShowThemeUpdated : Effect()
    }
}

enum class DarkModeOption(val label: String) {
    SYSTEM("Padrão do sistema"),
    LIGHT("Claro"),
    DARK("Noturno")
}
