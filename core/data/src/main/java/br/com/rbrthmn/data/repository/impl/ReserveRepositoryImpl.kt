package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.ReserveDao
import br.com.rbrthmn.data.dao.ReserveTransactionDao
import br.com.rbrthmn.data.mapper.toDomain
import br.com.rbrthmn.data.mapper.toEntity
import br.com.rbrthmn.data.model.Reserve
import br.com.rbrthmn.data.model.ReserveTransaction
import br.com.rbrthmn.data.repository.ReserveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReserveRepositoryImpl(
    private val reserveDao: ReserveDao,
    private val reserveTransactionDao: ReserveTransactionDao
) : ReserveRepository {
    override fun getAll(): Flow<List<Reserve>> = reserveDao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): Reserve? = reserveDao.getById(id)?.toDomain()
    override suspend fun insert(reserve: Reserve): Long = reserveDao.insert(reserve.toEntity())
    override suspend fun update(reserve: Reserve): Int = reserveDao.update(reserve.toEntity())
    override suspend fun delete(reserve: Reserve): Int = reserveDao.delete(reserve.toEntity())
    override fun getTransactionsByReserve(reserveId: Long): Flow<List<ReserveTransaction>> =
        reserveTransactionDao.getByReserve(reserveId).map { it.map { e -> e.toDomain() } }
    override suspend fun insertTransaction(transaction: ReserveTransaction): Long =
        reserveTransactionDao.insert(transaction.toEntity())
    override suspend fun deleteTransaction(transaction: ReserveTransaction): Int =
        reserveTransactionDao.delete(transaction.toEntity())
}
