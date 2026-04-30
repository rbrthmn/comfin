package br.com.rbrthmn.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.rbrthmn.data.entity.AllocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AllocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(allocation: AllocationEntity): Long

    @Update
    suspend fun update(allocation: AllocationEntity): Int

    @Delete
    suspend fun delete(allocation: AllocationEntity): Int

    @Query("SELECT * FROM allocations ORDER BY name ASC")
    fun getAll(): Flow<List<AllocationEntity>>

    @Query("SELECT * FROM allocations WHERE id = :id")
    suspend fun getById(id: Long): AllocationEntity?
}
