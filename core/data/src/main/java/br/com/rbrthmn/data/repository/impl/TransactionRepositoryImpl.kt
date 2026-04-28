package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.TransactionDao
import br.com.rbrthmn.data.mapper.toDomain
import br.com.rbrthmn.data.mapper.toEntity
import br.com.rbrthmn.data.model.Transaction
import br.com.rbrthmn.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {
    override fun getAll(): Flow<List<Transaction>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override fun getByMonth(startEpochDay: Long, endEpochDay: Long): Flow<List<Transaction>> =
        dao.getByMonth(startEpochDay, endEpochDay).map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): Transaction? = dao.getById(id)?.toDomain()
    override suspend fun insert(transaction: Transaction): Long = dao.insert(transaction.toEntity())
    override suspend fun update(transaction: Transaction): Int = dao.update(transaction.toEntity())
    override suspend fun delete(transaction: Transaction): Int = dao.delete(transaction.toEntity())
}
