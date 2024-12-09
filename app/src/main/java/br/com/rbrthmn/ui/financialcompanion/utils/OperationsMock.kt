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

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.rbrthmn.ui.financialcompanion.screens.operations.Operation
import java.util.Calendar
import kotlin.random.Random

@Composable
fun getOperationsMock(): List<Operation> {
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
    val operations = mutableListOf<Operation>()
    val fixedMonth = Random.nextInt(12)

    repeat(12) { index ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2024)
        calendar.set(Calendar.MONTH, fixedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, Random.nextInt(1, 29))


        val types = OperationType.entries.map { stringResource(id = it.stringId) }
        val extras = listOf("Conta A", "Conta B", "Cartão X", "Investimento Y", null)
        val value = Random.nextDouble(100.0, 5000.0).format(2)

        operations.add(
            Operation(
                description = "Teste ${index + 1}",
                value = value,
                date = calendar.time,
                type = types.random(),
                extras = extras.randomOrNull()
            )
        )
    }

    return operations
}