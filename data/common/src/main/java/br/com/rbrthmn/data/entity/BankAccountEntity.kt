package br.com.rbrthmn.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_accounts")
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val bankName: String,
    val balanceValue: Double,
    val isMainAccount: Boolean
)
