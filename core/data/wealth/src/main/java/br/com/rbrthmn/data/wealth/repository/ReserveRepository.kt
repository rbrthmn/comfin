package br.com.rbrthmn.data.wealth.repository

import br.com.rbrthmn.data.wealth.model.Reserve
import br.com.rbrthmn.data.wealth.model.ReserveTransaction
import kotlinx.coroutines.flow.Flow

interface ReserveRepository {
    fun getAll(): Flow<List<Reserve>>
    suspend fun getById(id: Long): Reserve?
    suspend fun insert(reserve: Reserve): Long
    suspend fun update(reserve: Reserve): Int
    suspend fun delete(reserve: Reserve): Int
    fun getTransactionsByReserve(reserveId: Long): Flow<List<ReserveTransaction>>
    suspend fun insertTransaction(transaction: ReserveTransaction): Long
    suspend fun deleteTransaction(transaction: ReserveTransaction): Int
}
