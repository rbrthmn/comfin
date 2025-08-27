package br.com.rbrthmn.misc.reserves

data class Reserve(
    val name: String,
    val value: String,
    val operations: List<ReserveOperation> = listOf()
)
