package br.com.rbrthmn.data.dao.wealth

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.wealth.ReserveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReserveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserve: ReserveEntity): Long

    @Update
    suspend fun update(reserve: ReserveEntity): Int

    @Delete
    suspend fun delete(reserve: ReserveEntity): Int

    @Query("SELECT * FROM reserves ORDER BY name ASC")
    fun getAll(): Flow<List<ReserveEntity>>

    @Query("SELECT * FROM reserves WHERE id = :id")
    suspend fun getById(id: Long): ReserveEntity?
}
