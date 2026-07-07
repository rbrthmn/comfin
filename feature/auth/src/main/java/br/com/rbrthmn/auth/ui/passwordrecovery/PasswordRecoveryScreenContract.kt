package br.com.rbrthmn.auth.passwordrecovery

import br.com.rbrthmn.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow

interface PasswordRecoveryScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>() {
        abstract val effects: Flow<Effect>
    }

    data class UiState(
        val step: RecoveryStep = RecoveryStep.EMAIL,
        val email: String = "",
        val newPassword: String = "",
        val newPasswordConfirmation: String = "",
        val isEmailValid: Boolean = true,
        val isNewPasswordValid: Boolean = true,
        val isNewPasswordConfirmationValid: Boolean = true,
        val isLoading: Boolean = false,
        val showAccountNotFoundError: Boolean = false
    )

    enum class RecoveryStep {
        EMAIL,
        NEW_PASSWORD
    }

    sealed class Intent {
        data class OnEmailChange(val value: String) : Intent()
        object OnVerifyEmailClick : Intent()
        data class OnNewPasswordChange(val value: String) : Intent()
        data class OnNewPasswordConfirmationChange(val value: String) : Intent()
        object OnResetPasswordClick : Intent()
        object OnBackToSignInClick : Intent()
    }

    sealed class Effect {
        object NavigateToSignIn : Effect()
        object ShowPasswordResetSuccess : Effect()
    }
}
