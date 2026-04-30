package br.com.rbrthmn.data.entity.wealth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reserves")
data class ReserveEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val institution: String,
    val currentTotal: Double,
    val monthlyYield: Double,
    val totalInflow: Double,
    val totalOutflow: Double
)
