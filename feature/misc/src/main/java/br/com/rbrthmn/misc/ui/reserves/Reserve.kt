package br.com.rbrthmn.misc.ui.reserves

data class Reserve(
    val name: String,
    val value: String,
    val operations: List<ReserveOperation> = listOf()
)
