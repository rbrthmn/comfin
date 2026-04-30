package br.com.rbrthmn.data.finance.repository.impl

import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.finance.mapper.toDomain
import br.com.rbrthmn.data.finance.mapper.toEntity
import br.com.rbrthmn.data.finance.model.CreditCard
import br.com.rbrthmn.data.finance.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreditCardRepositoryImpl(private val dao: CreditCardDao) : CreditCardRepository {
    override fun getAll(): Flow<List<CreditCard>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): CreditCard? = dao.getById(id)?.toDomain()
    override suspend fun insert(card: CreditCard): Long = dao.insert(card.toEntity())
    override suspend fun update(card: CreditCard): Int = dao.update(card.toEntity())
    override suspend fun delete(card: CreditCard): Int = dao.delete(card.toEntity())
}
