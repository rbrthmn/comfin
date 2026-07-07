package br.com.rbrthmn.auth.signup

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
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.auth.R
import br.com.rbrthmn.auth.common.AuthErrorText
import br.com.rbrthmn.auth.common.AuthTextField
import br.com.rbrthmn.auth.common.GoogleSignInButton
import br.com.rbrthmn.auth.common.OrDivider
import br.com.rbrthmn.navigation.NavigationDestination
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object SignUpDestination : NavigationDestination {
    override val route: String = "sign_up"
}

const val SIGN_UP_SCREEN_TAG = "sign_up_screen"

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val googleUnavailableMessage = stringResource(id = R.string.auth_google_unavailable)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SignUpScreenContract.Effect.NavigateToHome -> onNavigateToHome()
                SignUpScreenContract.Effect.NavigateToSignIn -> onNavigateToSignIn()
                SignUpScreenContract.Effect.ShowGoogleSignInUnavailable ->
                    snackbarHostState.showSnackbar(message = googleUnavailableMessage)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SignUpContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.testTag(SIGN_UP_SCREEN_TAG)
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SignUpContent(
    uiState: SignUpScreenContract.UiState,
    onIntent: (SignUpScreenContract.Intent) -> Unit,
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
            text = stringResource(id = R.string.sign_up_title),
            style = MaterialTheme.typography.headlineMedium
        )
        AuthTextField(
            value = uiState.name,
            onValueChange = { onIntent(SignUpScreenContract.Intent.OnNameChange(it)) },
            label = stringResource(id = R.string.sign_up_name_hint),
            isError = !uiState.isNameValid,
            errorText = stringResource(id = R.string.sign_up_blank_name_error)
        )
        AuthTextField(
            value = uiState.email,
            onValueChange = { onIntent(SignUpScreenContract.Intent.OnEmailChange(it)) },
            label = stringResource(id = R.string.auth_email_hint),
            isError = !uiState.isEmailValid,
            errorText = stringResource(id = R.string.auth_invalid_email_error),
            keyboardType = KeyboardType.Email
        )
        AuthTextField(
            value = uiState.password,
            onValueChange = { onIntent(SignUpScreenContract.Intent.OnPasswordChange(it)) },
            label = stringResource(id = R.string.auth_password_hint),
            isError = !uiState.isPasswordValid,
            errorText = stringResource(id = R.string.auth_password_min_length_error),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        AuthTextField(
            value = uiState.passwordConfirmation,
            onValueChange = {
                onIntent(SignUpScreenContract.Intent.OnPasswordConfirmationChange(it))
            },
            label = stringResource(id = R.string.sign_up_password_confirmation_hint),
            isError = !uiState.isPasswordConfirmationValid,
            errorText = stringResource(id = R.string.auth_password_mismatch_error),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        if (uiState.showEmailAlreadyRegisteredError) {
            AuthErrorText(
                text = stringResource(id = R.string.sign_up_email_already_registered_error)
            )
        }
        Button(
            onClick = { onIntent(SignUpScreenContract.Intent.OnSignUpClick) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.sign_up_button))
        }
        OrDivider()
        GoogleSignInButton(
            onClick = { onIntent(SignUpScreenContract.Intent.OnGoogleSignInClick) }
        )
        TextButton(
            onClick = { onIntent(SignUpScreenContract.Intent.OnSignInClick) }
        ) {
            Text(text = stringResource(id = R.string.sign_up_has_account_link))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpContent(
        uiState = SignUpScreenContract.UiState(),
        onIntent = {}
    )
}
