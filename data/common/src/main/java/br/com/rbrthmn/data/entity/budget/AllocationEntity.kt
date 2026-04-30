package br.com.rbrthmn.data.entity.budget

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allocations")
data class AllocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val plannedPercentage: Double,
    val targetValue: Double,
    val actualAllocated: Double
)
