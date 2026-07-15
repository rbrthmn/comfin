package br.com.rbrthmn.auth

import br.com.rbrthmn.auth.signup.SignUpScreenContract
import br.com.rbrthmn.auth.signup.SignUpScreenViewModel
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
class SignUpScreenViewModelTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: SignUpScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SignUpScreenViewModel(authRepository = authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should keep default state`() {
        viewModel.doOnInit()

        assertEquals(SignUpScreenContract.UiState(), viewModel.uiState.value)
    }

    @Test
    fun `onNameChange with blank value should mark name invalid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnNameChange(BLANK_STRING))

        assertEquals(BLANK_STRING, viewModel.uiState.value.name)
        assertFalse(viewModel.uiState.value.isNameValid)
    }

    @Test
    fun `onNameChange with value should mark name valid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnNameChange(VALID_NAME))

        assertEquals(VALID_NAME, viewModel.uiState.value.name)
        assertTrue(viewModel.uiState.value.isNameValid)
    }

    @Test
    fun `onEmailChange with invalid email should mark email invalid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnEmailChange(INVALID_EMAIL))

        assertEquals(INVALID_EMAIL, viewModel.uiState.value.email)
        assertFalse(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onEmailChange with valid email should mark email valid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnEmailChange(VALID_EMAIL))

        assertEquals(VALID_EMAIL, viewModel.uiState.value.email)
        assertTrue(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onPasswordChange with short password should mark password invalid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordChange(SHORT_PASSWORD))

        assertEquals(SHORT_PASSWORD, viewModel.uiState.value.password)
        assertFalse(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onPasswordChange with valid password should mark password valid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        assertEquals(VALID_PASSWORD, viewModel.uiState.value.password)
        assertTrue(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onPasswordConfirmationChange with mismatch should mark confirmation invalid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        viewModel.onIntent(
            SignUpScreenContract.Intent.OnPasswordConfirmationChange(OTHER_PASSWORD)
        )

        assertFalse(viewModel.uiState.value.isPasswordConfirmationValid)
    }

    @Test
    fun `onPasswordConfirmationChange with match should mark confirmation valid`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))

        viewModel.onIntent(
            SignUpScreenContract.Intent.OnPasswordConfirmationChange(VALID_PASSWORD)
        )

        assertTrue(viewModel.uiState.value.isPasswordConfirmationValid)
    }

    @Test
    fun `onSignUpClick with invalid fields should not call repository`() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnSignUpClick)

        coVerify(exactly = 0) { authRepository.signUp(any(), any(), any()) }
        assertFalse(viewModel.uiState.value.isNameValid)
        assertFalse(viewModel.uiState.value.isEmailValid)
        assertFalse(viewModel.uiState.value.isPasswordValid)
    }

    @Test
    fun `onSignUpClick with valid fields should emit NavigateToHome`() = runTest {
        coEvery {
            authRepository.signUp(name = VALID_NAME, email = VALID_EMAIL, password = VALID_PASSWORD)
        } returns AuthResult.Success(USER)
        assignValidInputs()

        viewModel.onIntent(SignUpScreenContract.Intent.OnSignUpClick)

        assertEquals(SignUpScreenContract.Effect.NavigateToHome, viewModel.effects.first())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onSignUpClick with registered email should show email already registered error`() =
        runTest {
            coEvery {
                authRepository.signUp(
                    name = VALID_NAME,
                    email = VALID_EMAIL,
                    password = VALID_PASSWORD
                )
            } returns AuthResult.Error.EmailAlreadyRegistered
            assignValidInputs()

            viewModel.onIntent(SignUpScreenContract.Intent.OnSignUpClick)

            assertTrue(viewModel.uiState.value.showEmailAlreadyRegisteredError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun `onGoogleSignInClick with unavailable provider should emit ShowGoogleSignInUnavailable`() =
        runTest {
            coEvery {
                authRepository.signInWith(SignInMethod.GOOGLE)
            } returns AuthResult.Error.ProviderUnavailable

            viewModel.onIntent(SignUpScreenContract.Intent.OnGoogleSignInClick)

            assertEquals(
                SignUpScreenContract.Effect.ShowGoogleSignInUnavailable,
                viewModel.effects.first()
            )
        }

    @Test
    fun `onSignInClick should emit NavigateToSignIn`() = runTest {
        viewModel.onIntent(SignUpScreenContract.Intent.OnSignInClick)

        assertEquals(SignUpScreenContract.Effect.NavigateToSignIn, viewModel.effects.first())
    }

    private fun assignValidInputs() {
        viewModel.onIntent(SignUpScreenContract.Intent.OnNameChange(VALID_NAME))
        viewModel.onIntent(SignUpScreenContract.Intent.OnEmailChange(VALID_EMAIL))
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordChange(VALID_PASSWORD))
        viewModel.onIntent(SignUpScreenContract.Intent.OnPasswordConfirmationChange(VALID_PASSWORD))
    }

    private companion object {
        const val VALID_NAME = "User"
        const val VALID_EMAIL = "user@email.com"
        const val INVALID_EMAIL = "invalid-email"
        const val VALID_PASSWORD = "password123"
        const val SHORT_PASSWORD = "short"
        const val OTHER_PASSWORD = "otherpassword"
        const val BLANK_STRING = " "
        val USER = AuthUser(id = "id", name = VALID_NAME, email = VALID_EMAIL)
    }
}
