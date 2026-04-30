package br.com.rbrthmn.data.entity.finance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val issuerName: String,
    val currentStatementValue: Double,
    val availableLimit: Double,
    val dueDay: Int
)
