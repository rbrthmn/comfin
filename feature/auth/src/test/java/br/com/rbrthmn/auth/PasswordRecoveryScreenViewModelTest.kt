package br.com.rbrthmn.auth

import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryScreenContract
import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryScreenViewModel
import br.com.rbrthmn.data.auth.model.AuthResult
import br.com.rbrthmn.data.auth.model.AuthUser
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
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PasswordRecoveryScreenViewModelTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: PasswordRecoveryScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = PasswordRecoveryScreenViewModel(authRepository = authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should keep default state`() {
        viewModel.doOnInit()

        assertEquals(PasswordRecoveryScreenContract.UiState(), viewModel.uiState.value)
        assertEquals(
            PasswordRecoveryScreenContract.RecoveryStep.EMAIL,
            viewModel.uiState.value.step
        )
    }

    @Test
    fun `onEmailChange with invalid email should mark email invalid`() {
        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnEmailChange(INVALID_EMAIL))

        assertFalse(viewModel.uiState.value.isEmailValid)
    }

    @Test
    fun `onVerifyEmailClick with unknown account should show account not found error`() = runTest {
        coEvery {
            authRepository.verifyAccountEmail(email = VALID_EMAIL)
        } returns AuthResult.Error.AccountNotFound
        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnEmailChange(VALID_EMAIL))

        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnVerifyEmailClick)

        assertTrue(viewModel.uiState.value.showAccountNotFoundError)
        assertEquals(
            PasswordRecoveryScreenContract.RecoveryStep.EMAIL,
            viewModel.uiState.value.step
        )
    }

    @Test
    fun `onVerifyEmailClick with known account should advance to new password step`() = runTest {
        coEvery {
            authRepository.verifyAccountEmail(email = VALID_EMAIL)
        } returns AuthResult.Success(USER)
        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnEmailChange(VALID_EMAIL))

        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnVerifyEmailClick)

        assertEquals(
            PasswordRecoveryScreenContract.RecoveryStep.NEW_PASSWORD,
            viewModel.uiState.value.step
        )
    }

    @Test
    fun `onNewPasswordChange with short password should mark new password invalid`() {
        viewModel.onIntent(
            PasswordRecoveryScreenContract.Intent.OnNewPasswordChange(SHORT_PASSWORD)
        )

        assertFalse(viewModel.uiState.value.isNewPasswordValid)
    }

    @Test
    fun `onNewPasswordConfirmationChange with mismatch should mark confirmation invalid`() {
        viewModel.onIntent(
            PasswordRecoveryScreenContract.Intent.OnNewPasswordChange(VALID_PASSWORD)
        )

        viewModel.onIntent(
            PasswordRecoveryScreenContract.Intent.OnNewPasswordConfirmationChange(OTHER_PASSWORD)
        )

        assertFalse(viewModel.uiState.value.isNewPasswordConfirmationValid)
    }

    @Test
    fun `onResetPasswordClick with valid password should emit success and navigate to sign in`() =
        runTest {
            coEvery {
                authRepository.resetPassword(email = VALID_EMAIL, newPassword = VALID_PASSWORD)
            } returns AuthResult.Success(USER)
            viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnEmailChange(VALID_EMAIL))
            viewModel.onIntent(
                PasswordRecoveryScreenContract.Intent.OnNewPasswordChange(VALID_PASSWORD)
            )
            viewModel.onIntent(
                PasswordRecoveryScreenContract.Intent.OnNewPasswordConfirmationChange(
                    VALID_PASSWORD
                )
            )

            viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnResetPasswordClick)

            val effects = viewModel.effects.take(2).toList()
            assertEquals(
                listOf(
                    PasswordRecoveryScreenContract.Effect.ShowPasswordResetSuccess,
                    PasswordRecoveryScreenContract.Effect.NavigateToSignIn
                ),
                effects
            )
        }

    @Test
    fun `onResetPasswordClick with invalid fields should not attempt password reset`() {
        viewModel.onIntent(
            PasswordRecoveryScreenContract.Intent.OnNewPasswordChange(VALID_PASSWORD)
        )
        viewModel.onIntent(
            PasswordRecoveryScreenContract.Intent.OnNewPasswordConfirmationChange(OTHER_PASSWORD)
        )

        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnResetPasswordClick)

        coVerify(exactly = 0) { authRepository.resetPassword(any(), any()) }
        assertFalse(viewModel.uiState.value.isNewPasswordConfirmationValid)
    }

    @Test
    fun `onBackToSignInClick should emit NavigateToSignIn`() = runTest {
        viewModel.onIntent(PasswordRecoveryScreenContract.Intent.OnBackToSignInClick)

        assertEquals(
            PasswordRecoveryScreenContract.Effect.NavigateToSignIn,
            viewModel.effects.first()
        )
    }

    private companion object {
        const val VALID_EMAIL = "user@email.com"
        const val INVALID_EMAIL = "invalid-email"
        const val VALID_PASSWORD = "password123"
        const val SHORT_PASSWORD = "short"
        const val OTHER_PASSWORD = "otherpassword"
        val USER = AuthUser(id = "id", name = "User", email = VALID_EMAIL)
    }
}
