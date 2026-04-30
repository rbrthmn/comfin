package br.com.rbrthmn.data.dao.wealth

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.wealth.InvestmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(investment: InvestmentEntity): Long

    @Update
    suspend fun update(investment: InvestmentEntity): Int

    @Delete
    suspend fun delete(investment: InvestmentEntity): Int

    @Query("SELECT * FROM investments ORDER BY type ASC, assetName ASC")
    fun getAll(): Flow<List<InvestmentEntity>>

    @Query("SELECT * FROM investments WHERE type = :type ORDER BY assetName ASC")
    fun getByType(type: String): Flow<List<InvestmentEntity>>

    @Query("SELECT * FROM investments WHERE id = :id")
    suspend fun getById(id: Long): InvestmentEntity?
}
