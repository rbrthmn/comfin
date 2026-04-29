package br.com.rbrthmn.data.budget.repository.impl

import br.com.rbrthmn.data.dao.AllocationDao
import br.com.rbrthmn.data.budget.mapper.toDomain
import br.com.rbrthmn.data.budget.mapper.toEntity
import br.com.rbrthmn.data.budget.model.Allocation
import br.com.rbrthmn.data.budget.repository.AllocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AllocationRepositoryImpl(private val dao: AllocationDao) : AllocationRepository {
    override fun getAll(): Flow<List<Allocation>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): Allocation? = dao.getById(id)?.toDomain()
    override suspend fun insert(allocation: Allocation): Long = dao.insert(allocation.toEntity())
    override suspend fun update(allocation: Allocation): Int = dao.update(allocation.toEntity())
    override suspend fun delete(allocation: Allocation): Int = dao.delete(allocation.toEntity())
}
