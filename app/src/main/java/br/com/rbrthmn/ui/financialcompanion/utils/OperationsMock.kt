package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.rbrthmn.ui.financialcompanion.screens.Operation
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