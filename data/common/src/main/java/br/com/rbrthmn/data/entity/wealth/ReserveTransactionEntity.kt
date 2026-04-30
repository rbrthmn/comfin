package br.com.rbrthmn.data.entity.wealth

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.rbrthmn.data.entity.wealth.ReserveEntity

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
