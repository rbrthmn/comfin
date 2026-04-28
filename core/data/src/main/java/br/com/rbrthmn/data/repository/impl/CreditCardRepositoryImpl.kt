package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.entity.CreditCardEntity
import br.com.rbrthmn.data.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow

class CreditCardRepositoryImpl(private val dao: CreditCardDao) : CreditCardRepository {
    override fun getAll(): Flow<List<CreditCardEntity>> = dao.getAll()
    override suspend fun getById(id: Long): CreditCardEntity? = dao.getById(id)
    override suspend fun insert(card: CreditCardEntity): Long = dao.insert(card)
    override suspend fun update(card: CreditCardEntity): Int = dao.update(card)
    override suspend fun delete(card: CreditCardEntity): Int = dao.delete(card)
}
