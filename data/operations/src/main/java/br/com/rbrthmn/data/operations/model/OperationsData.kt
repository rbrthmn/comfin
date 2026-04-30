package br.com.rbrthmn.data.operations.model

data class OperationsData(
    val operations: List<OperationItem>,
    val totalIncome: Double,
    val totalOutcome: Double,
    val totalBalance: Double
)
