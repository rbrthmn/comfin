package br.com.rbrthmn.auth.signin

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

object SignInDestination : NavigationDestination {
    override val route: String = "sign_in"
}

const val SIGN_IN_SCREEN_TAG = "sign_in_screen"

@Composable
fun SignInScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToPasswordRecovery: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInScreenContract.ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val googleUnavailableMessage = stringResource(id = R.string.auth_google_unavailable)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SignInScreenContract.Effect.NavigateToHome -> onNavigateToHome()
                SignInScreenContract.Effect.NavigateToSignUp -> onNavigateToSignUp()
                SignInScreenContract.Effect.NavigateToPasswordRecovery ->
                    onNavigateToPasswordRecovery()
                SignInScreenContract.Effect.ShowGoogleSignInUnavailable ->
                    snackbarHostState.showSnackbar(message = googleUnavailableMessage)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SignInContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.testTag(SIGN_IN_SCREEN_TAG)
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SignInContent(
    uiState: SignInScreenContract.UiState,
    onIntent: (SignInScreenContract.Intent) -> Unit,
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
            text = stringResource(id = R.string.sign_in_title),
            style = MaterialTheme.typography.headlineMedium
        )
        AuthTextField(
            value = uiState.email,
            onValueChange = { onIntent(SignInScreenContract.Intent.OnEmailChange(it)) },
            label = stringResource(id = R.string.auth_email_hint),
            isError = !uiState.isEmailValid,
            errorText = stringResource(id = R.string.auth_invalid_email_error),
            keyboardType = KeyboardType.Email
        )
        AuthTextField(
            value = uiState.password,
            onValueChange = { onIntent(SignInScreenContract.Intent.OnPasswordChange(it)) },
            label = stringResource(id = R.string.auth_password_hint),
            isError = !uiState.isPasswordValid,
            errorText = stringResource(id = R.string.sign_in_blank_password_error),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        if (uiState.showInvalidCredentialsError) {
            AuthErrorText(text = stringResource(id = R.string.sign_in_invalid_credentials_error))
        }
        Button(
            onClick = { onIntent(SignInScreenContract.Intent.OnSignInClick) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.sign_in_button))
        }
        TextButton(
            onClick = { onIntent(SignInScreenContract.Intent.OnPasswordRecoveryClick) }
        ) {
            Text(text = stringResource(id = R.string.sign_in_forgot_password_link))
        }
        OrDivider()
        GoogleSignInButton(
            onClick = { onIntent(SignInScreenContract.Intent.OnGoogleSignInClick) }
        )
        TextButton(
            onClick = { onIntent(SignInScreenContract.Intent.OnSignUpClick) }
        ) {
            Text(text = stringResource(id = R.string.sign_in_no_account_link))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInContent(
        uiState = SignInScreenContract.UiState(),
        onIntent = {}
    )
}
