package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.CreditCardEntity
import kotlinx.coroutines.flow.Flow

interface CreditCardRepository {
    fun getAll(): Flow<List<CreditCardEntity>>
    suspend fun getById(id: Long): CreditCardEntity?
    suspend fun insert(card: CreditCardEntity): Long
    suspend fun update(card: CreditCardEntity): Int
    suspend fun delete(card: CreditCardEntity): Int
}
