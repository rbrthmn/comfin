package br.com.rbrthmn.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reserve_transactions",
    foreignKeys = [
        ForeignKey(
            entity = ReserveEntity::class,
            parentColumns = ["id"],
            childColumns = ["reserveId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("reserveId")]
)
data class ReserveTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reserveId: Long,
    val date: Long,
    val inflow: Double,
    val outflow: Double,
    val yield: Double,
    val value: Double
)
