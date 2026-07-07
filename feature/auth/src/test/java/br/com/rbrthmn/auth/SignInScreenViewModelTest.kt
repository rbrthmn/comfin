package br.com.rbrthmn.auth

import br.com.rbrthmn.auth.signin.SignInScreenContract
import br.com.rbrthmn.auth.signin.SignInScreenViewModel
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
import br.com.rbrthmn.data.auth.model.SignInMethod
import br.com.rbrthmn.data.auth.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInScreenViewModelTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: SignInScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SignInScreenViewModel(authRepository = authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should keep default state`() {
        viewModel.doOnInit()

        assertEquals(SignInScreenContract.UiState(), viewModel.uiState.value)
    }

    @Test
    fun `onEmailChange with invalid email should mark email invalid`() {
        viewModel.onIntent(SignInScreenContract.Intent.OnEmailChange(INVALID_EMAIL))

        assertEquals(INVALID_EMAIL, viewModel.uiState.value.email)
        assertFalse(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onEmailChange with valid email should mark email valid`() {
        viewModel.onIntent(SignInScreenContract.Intent.OnEmailChange(VALID_EMAIL))

        assertEquals(VALID_EMAIL, viewModel.uiState.value.email)
        assertTrue(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onPasswordChange with blank value should mark password invalid`() {
        viewModel.onIntent(SignInScreenContract.Intent.OnPasswordChange(BLANK_STRING))

        assertEquals(BLANK_STRING, viewModel.uiState.value.password)
        assertFalse(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onPasswordChange with value should mark password valid`() {
        viewModel.onIntent(SignInScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        assertEquals(VALID_PASSWORD, viewModel.uiState.value.password)
        assertTrue(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onSignInClick with invalid fields should not call repository`() {
        viewModel.onIntent(SignInScreenContract.Intent.OnSignInClick)

        coVerify(exactly = 0) { authRepository.signIn(any(), any()) }
        assertFalse(viewModel.uiState.value.isEmailValid)
        assertFalse(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onSignInClick with valid credentials should emit NavigateToHome`() = runTest {
        coEvery {
            authRepository.signIn(email = VALID_EMAIL, password = VALID_PASSWORD)
        } returns AuthResult.Success(USER)
        viewModel.onIntent(SignInScreenContract.Intent.OnEmailChange(VALID_EMAIL))
        viewModel.onIntent(SignInScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        viewModel.onIntent(SignInScreenContract.Intent.OnSignInClick)

        assertEquals(SignInScreenContract.Effect.NavigateToHome, viewModel.effects.first())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onSignInClick with wrong credentials should show invalid credentials error`() = runTest {
        coEvery {
            authRepository.signIn(email = VALID_EMAIL, password = VALID_PASSWORD)
        } returns AuthResult.Error.InvalidCredentials
        viewModel.onIntent(SignInScreenContract.Intent.OnEmailChange(VALID_EMAIL))
        viewModel.onIntent(SignInScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        viewModel.onIntent(SignInScreenContract.Intent.OnSignInClick)

        assertTrue(viewModel.uiState.value.showInvalidCredentialsError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onGoogleSignInClick with unavailable provider should emit ShowGoogleSignInUnavailable`() =
        runTest {
            coEvery {
                authRepository.signInWith(SignInMethod.GOOGLE)
            } returns AuthResult.Error.ProviderUnavailable

            viewModel.onIntent(SignInScreenContract.Intent.OnGoogleSignInClick)

            assertEquals(
                SignInScreenContract.Effect.ShowGoogleSignInUnavailable,
                viewModel.effects.first()
            )
        }

    @Test
    fun `onSignUpClick should emit NavigateToSignUp`() = runTest {
        viewModel.onIntent(SignInScreenContract.Intent.OnSignUpClick)

        assertEquals(SignInScreenContract.Effect.NavigateToSignUp, viewModel.effects.first())
    }

    @Test
    fun `onPasswordRecoveryClick should emit NavigateToPasswordRecovery`() = runTest {
        viewModel.onIntent(SignInScreenContract.Intent.OnPasswordRecoveryClick)

        assertEquals(
            SignInScreenContract.Effect.NavigateToPasswordRecovery,
            viewModel.effects.first()
        )
    }

    private companion object {
        const val VALID_EMAIL = "user@email.com"
        const val INVALID_EMAIL = "invalid-email"
        const val VALID_PASSWORD = "password123"
        const val BLANK_STRING = " "
        val USER = AuthUser(id = "id", name = "User", email = VALID_EMAIL)
    }
}
