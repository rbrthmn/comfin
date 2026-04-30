package br.com.rbrthmn.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investments")
data class InvestmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val institution: String,
    val assetName: String,
    val purchaseDate: Long?,
    val expiryDate: Long?,
    val quantity: Double,
    val proventos: Double,
    val investedValue: Double,
    val currentValue: Double
)
