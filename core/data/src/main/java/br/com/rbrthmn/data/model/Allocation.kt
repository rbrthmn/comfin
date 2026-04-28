package br.com.rbrthmn.data.model

data class Allocation(
    val id: Long = 0,
    val name: String,
    val plannedPercentage: Double,
    val targetValue: Double,
    val actualAllocated: Double
)
