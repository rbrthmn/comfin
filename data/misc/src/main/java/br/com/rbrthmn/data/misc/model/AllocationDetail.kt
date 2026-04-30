package br.com.rbrthmn.data.misc.model

data class AllocationDetail(
    val id: Long,
    val name: String,
    val plannedPercentage: Double,
    val targetValue: Double,
    val actualAllocated: Double
)
