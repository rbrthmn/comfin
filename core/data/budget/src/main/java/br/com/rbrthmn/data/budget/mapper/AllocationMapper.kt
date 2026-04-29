package br.com.rbrthmn.data.budget.mapper

import br.com.rbrthmn.data.entity.AllocationEntity
import br.com.rbrthmn.data.budget.model.Allocation

fun AllocationEntity.toDomain() = Allocation(
    id = id,
    name = name,
    plannedPercentage = plannedPercentage,
    targetValue = targetValue,
    actualAllocated = actualAllocated
)

fun Allocation.toEntity() = AllocationEntity(
    id = id,
    name = name,
    plannedPercentage = plannedPercentage,
    targetValue = targetValue,
    actualAllocated = actualAllocated
)
