package br.com.rbrthmn.data.budget.repository

import br.com.rbrthmn.data.budget.model.Allocation
import kotlinx.coroutines.flow.Flow

interface AllocationRepository {
    fun getAll(): Flow<List<Allocation>>
    suspend fun getById(id: Long): Allocation?
    suspend fun insert(allocation: Allocation): Long
    suspend fun update(allocation: Allocation): Int
    suspend fun delete(allocation: Allocation): Int
}
