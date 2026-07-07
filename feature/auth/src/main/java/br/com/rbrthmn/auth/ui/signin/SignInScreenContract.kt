package br.com.rbrthmn.auth.signin

import br.com.rbrthmn.ui.BaseViewModel
import kotlinx.coroutines.flow.Flow

interface SignInScreenContract {
    abstract class ViewModel : BaseViewModel<UiState, Intent>() {
        abstract val effects: Flow<Effect>
    }

    data class UiState(
        val email: String = "",
        val password: String = "",
        val isEmailValid: Boolean = true,
        val isPasswordValid: Boolean = true,
        val isLoading: Boolean = false,
        val showInvalidCredentialsError: Boolean = false
    )

    sealed class Intent {
        data class OnEmailChange(val value: String) : Intent()
        data class OnPasswordChange(val value: String) : Intent()
        object OnSignInClick : Intent()
        object OnGoogleSignInClick : Intent()
        object OnSignUpClick : Intent()
        object OnPasswordRecoveryClick : Intent()
    }

    sealed class Effect {
        object NavigateToHome : Effect()
        object NavigateToSignUp : Effect()
        object NavigateToPasswordRecovery : Effect()
        object ShowGoogleSignInUnavailable : Effect()
    }
}
