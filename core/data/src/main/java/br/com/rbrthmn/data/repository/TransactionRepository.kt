package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<TransactionEntity>>
    fun getByMonth(startEpochDay: Long, endEpochDay: Long): Flow<List<TransactionEntity>>
    suspend fun getById(id: Long): TransactionEntity?
    suspend fun insert(transaction: TransactionEntity): Long
    suspend fun update(transaction: TransactionEntity): Int
    suspend fun delete(transaction: TransactionEntity): Int
}
