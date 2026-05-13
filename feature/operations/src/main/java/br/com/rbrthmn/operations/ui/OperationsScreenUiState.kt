package br.com.rbrthmn.operations.ui

import androidx.compose.runtime.Composable
import java.time.LocalDate

data class OperationsScreenUiState(
    val operations: List<Operation> = listOf(),
    val dialogFields: MutableList<@Composable () -> Unit> = mutableListOf(),
    val totalBalance: String = DEFAULT_STRING_VALUE,
    val totalIncome: String = DEFAULT_STRING_VALUE,
    val totalOutcome: String = DEFAULT_STRING_VALUE,
    val newOperationDescription: String = DEFAULT_STRING_VALUE,
    val isNewOperationDescriptionValid: Boolean = true,
    val newOperationValue: String = DEFAULT_STRING_VALUE,
    val isNewOperationValueValid: Boolean = true,
    val newOperationType: OperationType? = null,
    val isNewOperationTypeValid: Boolean = true,
    val newOperationOriginAccount: String = DEFAULT_STRING_VALUE,
    val isNewOperationOriginAccountValid: Boolean = true,
    val newOperationDestinationAccount: String = DEFAULT_STRING_VALUE,
    val isNewOperationDestinationAccountValid: Boolean = true,
    val newOperationDate: LocalDate = LocalDate.now(),
    var isNewOperationDateValid: Boolean = true,
    val newOperationReserve: String = DEFAULT_STRING_VALUE,
    val isNewOperationReserveValid: Boolean = true,
    val searchQuery: String = DEFAULT_STRING_VALUE,
    val currentDateFilter: LocalDate = LocalDate.now()
) {
    companion object {
        const val DEFAULT_STRING_VALUE = ""
    }
}
