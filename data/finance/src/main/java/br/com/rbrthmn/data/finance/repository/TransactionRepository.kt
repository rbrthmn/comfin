package br.com.rbrthmn.data.finance.repository

import br.com.rbrthmn.data.finance.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getByMonth(startEpochDay: Long, endEpochDay: Long): Flow<List<Transaction>>
    suspend fun getById(id: Long): Transaction?
    suspend fun insert(transaction: Transaction): Long
    suspend fun update(transaction: Transaction): Int
    suspend fun delete(transaction: Transaction): Int
}
