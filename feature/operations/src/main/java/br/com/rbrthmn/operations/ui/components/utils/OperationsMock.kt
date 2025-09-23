package br.com.rbrthmn.operations.ui.components.utils

import br.com.rbrthmn.operations.ui.Operation
import br.com.rbrthmn.operations.ui.OperationType
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

fun getOperationsMock(): List<Operation> {
    fun Double.format(digits: Int) = "%.${digits}f".format(this)
    val operations = mutableListOf<Operation>()
    val fixedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val fixedMonth = Calendar.getInstance().get(Calendar.MONTH)
    val fixedYear = Calendar.getInstance().get(Calendar.YEAR)

    repeat(12) { index ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, fixedYear)
        calendar.set(Calendar.MONTH, fixedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, Random.nextInt(1,fixedDay))

        val types = OperationType.entries.map { it.getLocalizedName() }
        val extras = listOf("Conta A", "Conta B", "Cartão X", "Investimento Y", null)
        val value = Random.nextDouble(100.0, 5000.0).format(2)

        operations.add(
            Operation(
                description = "Teste ${index + 1}",
                value = value,
                date = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                type = types.random(),
                extras = extras.randomOrNull()
            )
        )
    }

    return operations
}

fun OperationType.getLocalizedName(locale: Locale = Locale.getDefault()): String {
    return when (this) {
        OperationType.TRANSFER -> when (locale.language) {
            "pt" -> "Transferência"
            else -> "Transfer"
        }

        OperationType.DEBIT_PURCHASE -> when (locale.language) {
            "pt" -> "Compra no Débito"
            else -> "Debit Purchase"
        }

        OperationType.BILL_PAYMENT -> when (locale.language) {
            "pt" -> "Pagamento de Boleto"
            else -> "Bill Payment"
        }

        OperationType.WITHDRAWAL -> when (locale.language) {
            "pt" -> "Saque"
            else -> "Withdrawal"
        }

        OperationType.DEPOSIT -> when (locale.language) {
            "pt" -> "Depósito"
            else -> "Deposit"
        }

        OperationType.RESERVE_ALLOCATION -> when (locale.language) {
            "pt" -> "Alocação de Reserva"
            else -> "Reserve Allocation"
        }

        OperationType.RESERVE_WITHDRAWAL -> when (locale.language) {
            "pt" -> "Retirada de Reserva"
            else -> "Reserve Withdrawal"
        }

        OperationType.INCOME -> when (locale.language) {
            "pt" -> "Receita"
            else -> "Income"
        }

        OperationType.OTHER -> when (locale.language) {
            "pt" -> "Outro"
            else -> "Other"
        }
    }
}