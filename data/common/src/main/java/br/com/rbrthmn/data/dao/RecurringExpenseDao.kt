package br.com.rbrthmn.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: RecurringExpenseEntity): Long

    @Update
    suspend fun update(expense: RecurringExpenseEntity): Int

    @Delete
    suspend fun delete(expense: RecurringExpenseEntity): Int

    @Query("SELECT * FROM recurring_expenses ORDER BY paymentDay ASC")
    fun getAll(): Flow<List<RecurringExpenseEntity>>

    @Query("SELECT * FROM recurring_expenses WHERE id = :id")
    suspend fun getById(id: Long): RecurringExpenseEntity?
}
