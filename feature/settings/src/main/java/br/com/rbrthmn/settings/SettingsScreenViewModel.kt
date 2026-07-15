package br.com.rbrthmn.settings

import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.data.auth.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val authRepository: AuthRepository
) : SettingsScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(SettingsScreenContract.UiState())

    private val effectsChannel = Channel<SettingsScreenContract.Effect>(Channel.BUFFERED)
    override val effects = effectsChannel.receiveAsFlow()

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
                onSelectDarkModeOption(intent.option)
            SettingsScreenContract.Intent.OnLogoutClick ->
                uiState.update { it.copy(showLogoutDialog = true) }
            SettingsScreenContract.Intent.OnDismissLogoutDialog ->
                uiState.update { it.copy(showLogoutDialog = false) }
            SettingsScreenContract.Intent.OnConfirmLogout -> onConfirmLogout()
        }
    }

    private fun onSelectDarkModeOption(option: String) {
        uiState.update {
            it.copy(
                selectedDarkModeOption = option,
                showDarkModeDialog = false
            )
        }
        sendEffect(SettingsScreenContract.Effect.ShowThemeUpdated)
    }

    private fun onConfirmLogout() {
        authRepository.signOut()
        uiState.update { it.copy(showLogoutDialog = false) }
        sendEffect(SettingsScreenContract.Effect.NavigateToSignIn)
    }

    private fun sendEffect(effect: SettingsScreenContract.Effect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }
}
