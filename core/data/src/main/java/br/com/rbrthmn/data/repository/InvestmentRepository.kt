package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.model.Investment
import kotlinx.coroutines.flow.Flow

interface InvestmentRepository {
    fun getAll(): Flow<List<Investment>>
    fun getByType(type: String): Flow<List<Investment>>
    suspend fun getById(id: Long): Investment?
    suspend fun insert(investment: Investment): Long
    suspend fun update(investment: Investment): Int
    suspend fun delete(investment: Investment): Int
}
