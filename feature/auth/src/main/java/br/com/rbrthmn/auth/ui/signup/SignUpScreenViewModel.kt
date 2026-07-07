package br.com.rbrthmn.auth.signup

import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.auth.common.isValidEmail
import br.com.rbrthmn.auth.common.isValidPassword
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.SignInMethod
import br.com.rbrthmn.data.auth.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    private val authRepository: AuthRepository
) : SignUpScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(SignUpScreenContract.UiState())

    private val effectsChannel = Channel<SignUpScreenContract.Effect>(Channel.BUFFERED)
    override val effects = effectsChannel.receiveAsFlow()

    override fun doOnInit(): SignUpScreenViewModel = this

    override fun onIntent(intent: SignUpScreenContract.Intent) {
        when (intent) {
            is SignUpScreenContract.Intent.OnNameChange -> onNameChange(intent.value)
            is SignUpScreenContract.Intent.OnEmailChange -> onEmailChange(intent.value)
            is SignUpScreenContract.Intent.OnPasswordChange -> onPasswordChange(intent.value)
            is SignUpScreenContract.Intent.OnPasswordConfirmationChange ->
                onPasswordConfirmationChange(intent.value)
            SignUpScreenContract.Intent.OnSignUpClick -> onSignUpClick()
            SignUpScreenContract.Intent.OnGoogleSignInClick -> onGoogleSignInClick()
            SignUpScreenContract.Intent.OnSignInClick ->
                sendEffect(SignUpScreenContract.Effect.NavigateToSignIn)
        }
    }

    private fun onNameChange(value: String) {
        uiState.update { it.copy(name = value, isNameValid = value.isNotBlank()) }
    }

    private fun onEmailChange(value: String) {
        uiState.update {
            it.copy(
                email = value,
                isEmailValid = isValidEmail(value),
                showEmailAlreadyRegisteredError = false
            )
        }
    }

    private fun onPasswordChange(value: String) {
        uiState.update {
            it.copy(
                password = value,
                isPasswordValid = isValidPassword(value),
                isPasswordConfirmationValid =
                    it.passwordConfirmation.isEmpty() || it.passwordConfirmation == value
            )
        }
    }

    private fun onPasswordConfirmationChange(value: String) {
        uiState.update {
            it.copy(
                passwordConfirmation = value,
                isPasswordConfirmationValid = value == it.password
            )
        }
    }

    private fun onSignUpClick() {
        val state = uiState.value
        val isNameValid = state.name.isNotBlank()
        val isEmailValid = isValidEmail(state.email)
        val isPasswordValid = isValidPassword(state.password)
        val isConfirmationValid = state.passwordConfirmation == state.password
        if (!isNameValid || !isEmailValid || !isPasswordValid || !isConfirmationValid) {
            uiState.update {
                it.copy(
                    isNameValid = isNameValid,
                    isEmailValid = isEmailValid,
                    isPasswordValid = isPasswordValid,
                    isPasswordConfirmationValid = isConfirmationValid
                )
            }
            return
        }
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true, showEmailAlreadyRegisteredError = false) }
            when (
                authRepository.signUp(
                    name = state.name,
                    email = state.email,
                    password = state.password
                )
            ) {
                is AuthResult.Success -> {
                    uiState.update { it.copy(isLoading = false) }
                    effectsChannel.send(SignUpScreenContract.Effect.NavigateToHome)
                }

                else -> uiState.update {
                    it.copy(isLoading = false, showEmailAlreadyRegisteredError = true)
                }
            }
        }
    }

    private fun onGoogleSignInClick() {
        viewModelScope.launch {
            when (authRepository.signInWith(SignInMethod.GOOGLE)) {
                is AuthResult.Success ->
                    effectsChannel.send(SignUpScreenContract.Effect.NavigateToHome)

                else ->
                    effectsChannel.send(SignUpScreenContract.Effect.ShowGoogleSignInUnavailable)
            }
        }
    }

    private fun sendEffect(effect: SignUpScreenContract.Effect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }
}
