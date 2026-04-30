package br.com.rbrthmn.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = ReserveEntity::class,
            parentColumns = ["id"],
            childColumns = ["reserveId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CreditCardEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditCardId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = BankAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["bankAccountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("reserveId"),
        Index("creditCardId"),
        Index("bankAccountId")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val counterparty: String,
    val notes: String?,
    val amount: Double,
    val type: String,
    val category: String,
    val reserveId: Long?,
    val creditCardId: Long?,
    val bankAccountId: Long?
)
