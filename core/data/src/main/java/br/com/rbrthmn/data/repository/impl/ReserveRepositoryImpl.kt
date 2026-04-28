package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.ReserveDao
import br.com.rbrthmn.data.dao.ReserveTransactionDao
import br.com.rbrthmn.data.entity.ReserveEntity
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import br.com.rbrthmn.data.repository.ReserveRepository
import kotlinx.coroutines.flow.Flow

class ReserveRepositoryImpl(
    private val reserveDao: ReserveDao,
    private val reserveTransactionDao: ReserveTransactionDao
) : ReserveRepository {
    override fun getAll(): Flow<List<ReserveEntity>> = reserveDao.getAll()
    override suspend fun getById(id: Long): ReserveEntity? = reserveDao.getById(id)
    override suspend fun insert(reserve: ReserveEntity): Long = reserveDao.insert(reserve)
    override suspend fun update(reserve: ReserveEntity): Int = reserveDao.update(reserve)
    override suspend fun delete(reserve: ReserveEntity): Int = reserveDao.delete(reserve)
    override fun getTransactionsByReserve(reserveId: Long): Flow<List<ReserveTransactionEntity>> =
        reserveTransactionDao.getByReserve(reserveId)
    override suspend fun insertTransaction(transaction: ReserveTransactionEntity): Long =
        reserveTransactionDao.insert(transaction)
    override suspend fun deleteTransaction(transaction: ReserveTransactionEntity): Int =
        reserveTransactionDao.delete(transaction)
}
