package br.com.rbrthmn.auth.signup

import br.com.rbrthmn.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow

interface SignUpScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>() {
        abstract val effects: Flow<Effect>
    }

    data class UiState(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val passwordConfirmation: String = "",
        val isNameValid: Boolean = true,
        val isEmailValid: Boolean = true,
        val isPasswordValid: Boolean = true,
        val isPasswordConfirmationValid: Boolean = true,
        val isLoading: Boolean = false,
        val showEmailAlreadyRegisteredError: Boolean = false
    )

    sealed class Intent {
        data class OnNameChange(val value: String) : Intent()
        data class OnEmailChange(val value: String) : Intent()
        data class OnPasswordChange(val value: String) : Intent()
        data class OnPasswordConfirmationChange(val value: String) : Intent()
        object OnSignUpClick : Intent()
        object OnGoogleSignInClick : Intent()
        object OnSignInClick : Intent()
    }

    sealed class Effect {
        object NavigateToHome : Effect()
        object NavigateToSignIn : Effect()
        object ShowGoogleSignInUnavailable : Effect()
    }
}
