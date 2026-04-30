package br.com.rbrthmn.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReserveTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserveTransaction: ReserveTransactionEntity): Long

    @Update
    suspend fun update(reserveTransaction: ReserveTransactionEntity): Int

    @Delete
    suspend fun delete(reserveTransaction: ReserveTransactionEntity): Int

    @Query("SELECT * FROM reserve_transactions WHERE reserveId = :reserveId ORDER BY date DESC")
    fun getByReserve(reserveId: Long): Flow<List<ReserveTransactionEntity>>

    @Query("SELECT * FROM reserve_transactions ORDER BY date DESC")
    fun getAll(): Flow<List<ReserveTransactionEntity>>
}
