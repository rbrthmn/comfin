package br.com.rbrthmn.auth.signin

import android.content.Context
import androidx.lifecycle.viewModelScope
import br.com.rbrthmn.auth.common.isValidEmail
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.SignInMethod
import br.com.rbrthmn.data.auth.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInScreenViewModel(
    private val authRepository: AuthRepository
) : SignInScreenContract.ViewModel() {

    override var uiState = MutableStateFlow(SignInScreenContract.UiState())

    private val effectsChannel = Channel<SignInScreenContract.Effect>(Channel.BUFFERED)
    override val effects = effectsChannel.receiveAsFlow()

    override fun doOnInit(): SignInScreenViewModel = this

    override fun onIntent(intent: SignInScreenContract.Intent) {
        when (intent) {
            is SignInScreenContract.Intent.OnEmailChange -> onEmailChange(intent.value)
            is SignInScreenContract.Intent.OnPasswordChange -> onPasswordChange(intent.value)
            SignInScreenContract.Intent.OnSignInClick -> onSignInClick()
            is SignInScreenContract.Intent.OnGoogleSignInClick ->
                onGoogleSignInClick(intent.activityContext)
            SignInScreenContract.Intent.OnSignUpClick -> sendEffect(SignInScreenContract.Effect.NavigateToSignUp)
            SignInScreenContract.Intent.OnPasswordRecoveryClick ->
                sendEffect(SignInScreenContract.Effect.NavigateToPasswordRecovery)
        }
    }

    private fun onEmailChange(value: String) {
        uiState.update {
            it.copy(
                email = value,
                isEmailValid = isValidEmail(value),
                showInvalidCredentialsError = false
            )
        }
    }

    private fun onPasswordChange(value: String) {
        uiState.update {
            it.copy(
                password = value,
                isPasswordValid = value.isNotBlank(),
                showInvalidCredentialsError = false
            )
        }
    }

    private fun onSignInClick() {
        val state = uiState.value
        val isEmailValid = isValidEmail(state.email)
        val isPasswordValid = state.password.isNotBlank()
        if (!isEmailValid || !isPasswordValid) {
            uiState.update {
                it.copy(isEmailValid = isEmailValid, isPasswordValid = isPasswordValid)
            }
            return
        }
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true, showInvalidCredentialsError = false) }
            when (authRepository.signIn(email = state.email, password = state.password)) {
                is AuthResult.Success -> {
                    uiState.update { it.copy(isLoading = false) }
                    effectsChannel.send(SignInScreenContract.Effect.NavigateToHome)
                }

                else -> uiState.update {
                    it.copy(isLoading = false, showInvalidCredentialsError = true)
                }
            }
        }
    }

    private fun onGoogleSignInClick(activityContext: Context) {
        viewModelScope.launch {
            uiState.update { it.copy(isLoading = true, showGoogleSignInError = false) }
            when (authRepository.signInWith(SignInMethod.GOOGLE, activityContext)) {
                is AuthResult.Success -> {
                    uiState.update { it.copy(isLoading = false) }
                    effectsChannel.send(SignInScreenContract.Effect.NavigateToHome)
                }

                AuthResult.Error.Cancelled ->
                    uiState.update { it.copy(isLoading = false) }

                else -> uiState.update {
                    it.copy(isLoading = false, showGoogleSignInError = true)
                }
            }
        }
    }

    private fun sendEffect(effect: SignInScreenContract.Effect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }
}
