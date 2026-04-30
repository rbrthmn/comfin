package br.com.rbrthmn.data.entity.budget

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_expenses")
data class RecurringExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val amount: Double,
    val paymentDay: Int,
    val validUntil: Long?,
    val isPaid: Boolean
)
