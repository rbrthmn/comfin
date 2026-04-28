package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.InvestmentEntity
import kotlinx.coroutines.flow.Flow

interface InvestmentRepository {
    fun getAll(): Flow<List<InvestmentEntity>>
    fun getByType(type: String): Flow<List<InvestmentEntity>>
    suspend fun getById(id: Long): InvestmentEntity?
    suspend fun insert(investment: InvestmentEntity): Long
    suspend fun update(investment: InvestmentEntity): Int
    suspend fun delete(investment: InvestmentEntity): Int
}
