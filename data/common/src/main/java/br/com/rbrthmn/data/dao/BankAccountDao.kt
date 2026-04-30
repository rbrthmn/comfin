package br.com.rbrthmn.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.BankAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BankAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: BankAccountEntity): Long

    @Update
    suspend fun update(account: BankAccountEntity): Int

    @Delete
    suspend fun delete(account: BankAccountEntity): Int

    @Query("SELECT * FROM bank_accounts ORDER BY isMainAccount DESC, name ASC")
    fun getAll(): Flow<List<BankAccountEntity>>

    @Query("SELECT * FROM bank_accounts WHERE id = :id")
    suspend fun getById(id: Long): BankAccountEntity?
}
