package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.AllocationEntity
import kotlinx.coroutines.flow.Flow

interface AllocationRepository {
    fun getAll(): Flow<List<AllocationEntity>>
    suspend fun getById(id: Long): AllocationEntity?
    suspend fun insert(allocation: AllocationEntity): Long
    suspend fun update(allocation: AllocationEntity): Int
    suspend fun delete(allocation: AllocationEntity): Int
}
