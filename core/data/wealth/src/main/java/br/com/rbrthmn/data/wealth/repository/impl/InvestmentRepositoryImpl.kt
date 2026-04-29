package br.com.rbrthmn.data.wealth.repository.impl

import br.com.rbrthmn.data.dao.InvestmentDao
import br.com.rbrthmn.data.wealth.mapper.toDomain
import br.com.rbrthmn.data.wealth.mapper.toEntity
import br.com.rbrthmn.data.wealth.model.Investment
import br.com.rbrthmn.data.wealth.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InvestmentRepositoryImpl(private val dao: InvestmentDao) : InvestmentRepository {
    override fun getAll(): Flow<List<Investment>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override fun getByType(type: String): Flow<List<Investment>> = dao.getByType(type).map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): Investment? = dao.getById(id)?.toDomain()
    override suspend fun insert(investment: Investment): Long = dao.insert(investment.toEntity())
    override suspend fun update(investment: Investment): Int = dao.update(investment.toEntity())
    override suspend fun delete(investment: Investment): Int = dao.delete(investment.toEntity())
}
