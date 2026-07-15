package br.com.rbrthmn.auth.passwordrecovery

import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.auth.common.isValidEmail
import br.com.rbrthmn.auth.common.isValidPassword
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordRecoveryScreenViewModel(
    private val authRepository: AuthRepository
) : PasswordRecoveryScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(PasswordRecoveryScreenContract.UiState())

    private val effectsChannel = Channel<PasswordRecoveryScreenContract.Effect>(Channel.BUFFERED)
    override val effects = effectsChannel.receiveAsFlow()

    override fun doOnInit(): PasswordRecoveryScreenViewModel = this

    override fun onIntent(intent: PasswordRecoveryScreenContract.Intent) {
        when (intent) {
            is PasswordRecoveryScreenContract.Intent.OnEmailChange -> onEmailChange(intent.value)
            PasswordRecoveryScreenContract.Intent.OnVerifyEmailClick -> onVerifyEmailClick()
            is PasswordRecoveryScreenContract.Intent.OnNewPasswordChange ->
                onNewPasswordChange(intent.value)
            is PasswordRecoveryScreenContract.Intent.OnNewPasswordConfirmationChange ->
                onNewPasswordConfirmationChange(intent.value)
            PasswordRecoveryScreenContract.Intent.OnResetPasswordClick -> onResetPasswordClick()
            PasswordRecoveryScreenContract.Intent.OnBackToSignInClick ->
                sendEffect(PasswordRecoveryScreenContract.Effect.NavigateToSignIn)
        }
    }

    private fun onEmailChange(value: String) {
        uiState.update {
            it.copy(
                email = value,
                isEmailValid = isValidEmail(value),
                showAccountNotFoundError = false
            )
        }
    }

    private fun onVerifyEmailClick() {
        val state = uiState.value
        if (!isValidEmail(state.email)) {
            uiState.update { it.copy(isEmailValid = false) }
            return
        }
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true, showAccountNotFoundError = false) }
            when (authRepository.verifyAccountEmail(email = state.email)) {
                is AuthResult.Success -> uiState.update {
                    it.copy(
                        isLoading = false,
                        step = PasswordRecoveryScreenContract.RecoveryStep.NEW_PASSWORD
                    )
                }

                else -> uiState.update {
                    it.copy(isLoading = false, showAccountNotFoundError = true)
                }
            }
        }
    }

    private fun onNewPasswordChange(value: String) {
        uiState.update {
            it.copy(
                newPassword = value,
                isNewPasswordValid = isValidPassword(value),
                isNewPasswordConfirmationValid =
                    it.newPasswordConfirmation.isEmpty() || it.newPasswordConfirmation == value
            )
        }
    }

    private fun onNewPasswordConfirmationChange(value: String) {
        uiState.update {
            it.copy(
                newPasswordConfirmation = value,
                isNewPasswordConfirmationValid = value == it.newPassword
            )
        }
    }

    private fun onResetPasswordClick() {
        val state = uiState.value
        val isPasswordValid = isValidPassword(state.newPassword)
        val isConfirmationValid = state.newPasswordConfirmation == state.newPassword
        if (!isPasswordValid || !isConfirmationValid) {
            uiState.update {
                it.copy(
                    isNewPasswordValid = isPasswordValid,
                    isNewPasswordConfirmationValid = isConfirmationValid
                )
            }
            return
        }
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true) }
            when (
                authRepository.resetPassword(email = state.email, newPassword = state.newPassword)
            ) {
                is AuthResult.Success -> {
                    uiState.update { it.copy(isLoading = false) }
                    effectsChannel.send(
                        PasswordRecoveryScreenContract.Effect.ShowPasswordResetSuccess
                    )
                    effectsChannel.send(PasswordRecoveryScreenContract.Effect.NavigateToSignIn)
                }

                else -> uiState.update {
                    it.copy(isLoading = false, showAccountNotFoundError = true)
                }
            }
        }
    }

    private fun sendEffect(effect: PasswordRecoveryScreenContract.Effect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }
}
