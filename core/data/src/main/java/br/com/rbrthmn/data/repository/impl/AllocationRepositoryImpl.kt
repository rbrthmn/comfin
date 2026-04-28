package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.AllocationDao
import br.com.rbrthmn.data.entity.AllocationEntity
import br.com.rbrthmn.data.repository.AllocationRepository
import kotlinx.coroutines.flow.Flow

class AllocationRepositoryImpl(private val dao: AllocationDao) : AllocationRepository {
    override fun getAll(): Flow<List<AllocationEntity>> = dao.getAll()
    override suspend fun getById(id: Long): AllocationEntity? = dao.getById(id)
    override suspend fun insert(allocation: AllocationEntity): Long = dao.insert(allocation)
    override suspend fun update(allocation: AllocationEntity): Int = dao.update(allocation)
    override suspend fun delete(allocation: AllocationEntity): Int = dao.delete(allocation)
}
