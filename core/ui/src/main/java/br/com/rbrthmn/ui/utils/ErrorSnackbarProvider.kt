package br.com.rbrthmn.ui.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

interface SnackBarProvider {
    val hostState: SnackbarHostState
    @Composable
    fun ShowSnackBar(message: String, duration: SnackbarDuration?)
}

class SnackBarProviderImpl : SnackBarProvider {
    override val hostState: SnackbarHostState = SnackbarHostState()

    @Composable
    override fun ShowSnackBar(message: String, duration: SnackbarDuration?) {
        val scope = rememberCoroutineScope()

        LaunchedEffect(LAUNCHED_EFFECT_KEY) {
            scope.launch {
                hostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = duration ?: SnackbarDuration.Short
                )
            }
        }
    }

    companion object {
        const val LAUNCHED_EFFECT_KEY = "showSnackBar"
    }
}