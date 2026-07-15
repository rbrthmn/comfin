package br.com.rbrthmn.auth.passwordrecovery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.auth.R
import br.com.rbrthmn.auth.common.AuthErrorText
import br.com.rbrthmn.auth.common.AuthTextField
import br.com.rbrthmn.navigation.NavigationDestination
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object PasswordRecoveryDestination : NavigationDestination {
    override val route: String = "password_recovery"
}

const val PASSWORD_RECOVERY_SCREEN_TAG = "password_recovery_screen"

@Composable
fun PasswordRecoveryScreen(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasswordRecoveryScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val resetSuccessMessage = stringResource(id = R.string.password_recovery_success_message)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PasswordRecoveryScreenContract.Effect.NavigateToSignIn -> onNavigateToSignIn()
                PasswordRecoveryScreenContract.Effect.ShowPasswordResetSuccess ->
                    snackbarHostState.showSnackbar(message = resetSuccessMessage)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PasswordRecoveryContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.testTag(PASSWORD_RECOVERY_SCREEN_TAG)
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PasswordRecoveryContent(
    uiState: PasswordRecoveryScreenContract.UiState,
    onIntent: (PasswordRecoveryScreenContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = commonR.dimen.padding_medium),
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_large))
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.password_recovery_title),
            style = MaterialTheme.typography.headlineMedium
        )
        when (uiState.step) {
            PasswordRecoveryScreenContract.RecoveryStep.EMAIL -> EmailStep(
                uiState = uiState,
                onIntent = onIntent
            )
            PasswordRecoveryScreenContract.RecoveryStep.NEW_PASSWORD -> NewPasswordStep(
                uiState = uiState,
                onIntent = onIntent
            )
        }
        TextButton(
            onClick = { onIntent(PasswordRecoveryScreenContract.Intent.OnBackToSignInClick) }
        ) {
            Text(text = stringResource(id = R.string.password_recovery_back_to_sign_in_link))
        }
    }
}

@Composable
private fun EmailStep(
    uiState: PasswordRecoveryScreenContract.UiState,
    onIntent: (PasswordRecoveryScreenContract.Intent) -> Unit
) {
    Text(
        text = stringResource(id = R.string.password_recovery_email_instructions),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
    AuthTextField(
        value = uiState.email,
        onValueChange = { onIntent(PasswordRecoveryScreenContract.Intent.OnEmailChange(it)) },
        label = stringResource(id = R.string.auth_email_hint),
        isError = !uiState.isEmailValid,
        errorText = stringResource(id = R.string.auth_invalid_email_error),
        keyboardType = KeyboardType.Email
    )
    if (uiState.showAccountNotFoundError) {
        AuthErrorText(
            text = stringResource(id = R.string.password_recovery_account_not_found_error)
        )
    }
    Button(
        onClick = { onIntent(PasswordRecoveryScreenContract.Intent.OnVerifyEmailClick) },
        enabled = !uiState.isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.password_recovery_verify_button))
    }
}

@Composable
private fun NewPasswordStep(
    uiState: PasswordRecoveryScreenContract.UiState,
    onIntent: (PasswordRecoveryScreenContract.Intent) -> Unit
) {
    AuthTextField(
        value = uiState.newPassword,
        onValueChange = {
            onIntent(PasswordRecoveryScreenContract.Intent.OnNewPasswordChange(it))
        },
        label = stringResource(id = R.string.password_recovery_new_password_hint),
        isError = !uiState.isNewPasswordValid,
        errorText = stringResource(id = R.string.auth_password_min_length_error),
        keyboardType = KeyboardType.Password,
        isPassword = true
    )
    AuthTextField(
        value = uiState.newPasswordConfirmation,
        onValueChange = {
            onIntent(PasswordRecoveryScreenContract.Intent.OnNewPasswordConfirmationChange(it))
        },
        label = stringResource(id = R.string.password_recovery_new_password_confirmation_hint),
        isError = !uiState.isNewPasswordConfirmationValid,
        errorText = stringResource(id = R.string.auth_password_mismatch_error),
        keyboardType = KeyboardType.Password,
        isPassword = true
    )
    Button(
        onClick = { onIntent(PasswordRecoveryScreenContract.Intent.OnResetPasswordClick) },
        enabled = !uiState.isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.password_recovery_reset_button))
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordRecoveryScreenPreview() {
    PasswordRecoveryContent(
        uiState = PasswordRecoveryScreenContract.UiState(),
        onIntent = {}
    )
}
