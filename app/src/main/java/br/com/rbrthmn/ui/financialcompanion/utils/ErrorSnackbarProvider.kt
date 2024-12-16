/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.rbrthmn.ui.financialcompanion.utils

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