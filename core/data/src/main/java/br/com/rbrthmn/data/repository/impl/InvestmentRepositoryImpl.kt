package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.InvestmentDao
import br.com.rbrthmn.data.entity.InvestmentEntity
import br.com.rbrthmn.data.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow

class InvestmentRepositoryImpl(private val dao: InvestmentDao) : InvestmentRepository {
    override fun getAll(): Flow<List<InvestmentEntity>> = dao.getAll()
    override fun getByType(type: String): Flow<List<InvestmentEntity>> = dao.getByType(type)
    override suspend fun getById(id: Long): InvestmentEntity? = dao.getById(id)
    override suspend fun insert(investment: InvestmentEntity): Long = dao.insert(investment)
    override suspend fun update(investment: InvestmentEntity): Int = dao.update(investment)
    override suspend fun delete(investment: InvestmentEntity): Int = dao.delete(investment)
}
