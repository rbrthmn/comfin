package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.ReserveEntity
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import kotlinx.coroutines.flow.Flow

interface ReserveRepository {
    fun getAll(): Flow<List<ReserveEntity>>
    suspend fun getById(id: Long): ReserveEntity?
    suspend fun insert(reserve: ReserveEntity): Long
    suspend fun update(reserve: ReserveEntity): Int
    suspend fun delete(reserve: ReserveEntity): Int
    fun getTransactionsByReserve(reserveId: Long): Flow<List<ReserveTransactionEntity>>
    suspend fun insertTransaction(transaction: ReserveTransactionEntity): Long
    suspend fun deleteTransaction(transaction: ReserveTransactionEntity): Int
}
