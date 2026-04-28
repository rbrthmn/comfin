package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.TransactionDao
import br.com.rbrthmn.data.entity.TransactionEntity
import br.com.rbrthmn.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(private val dao: TransactionDao) : TransactionRepository {
    override fun getAll(): Flow<List<TransactionEntity>> = dao.getAll()
    override fun getByMonth(startEpochDay: Long, endEpochDay: Long): Flow<List<TransactionEntity>> =
        dao.getByMonth(startEpochDay, endEpochDay)
    override suspend fun getById(id: Long): TransactionEntity? = dao.getById(id)
    override suspend fun insert(transaction: TransactionEntity): Long = dao.insert(transaction)
    override suspend fun update(transaction: TransactionEntity): Int = dao.update(transaction)
    override suspend fun delete(transaction: TransactionEntity): Int = dao.delete(transaction)
}
