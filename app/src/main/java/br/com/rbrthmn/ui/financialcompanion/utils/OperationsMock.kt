package br.com.rbrthmn.ui.financialcompanion.utils

import br.com.rbrthmn.ui.financialcompanion.screens.Operation
import java.util.Calendar
import kotlin.random.Random

fun getOperationsMock(): List<Operation> {
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
    val operations = mutableListOf<Operation>()
    repeat(12) { index ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2023)

        if (index > 0 && Random.nextBoolean()) {
            calendar.time = operations[index - 1].date
        } else {
            calendar.set(Calendar.MONTH, Random.nextInt(12))
            calendar.set(Calendar.DAY_OF_MONTH, Random.nextInt(1, 29))
        }

        val types = listOf("Transferência", "Pagamento", "Depósito", "Saque")
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