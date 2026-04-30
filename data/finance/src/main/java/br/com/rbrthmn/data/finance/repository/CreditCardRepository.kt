package br.com.rbrthmn.data.finance.repository

import br.com.rbrthmn.data.finance.model.CreditCard
import kotlinx.coroutines.flow.Flow

interface CreditCardRepository {
    fun getAll(): Flow<List<CreditCard>>
    suspend fun getById(id: Long): CreditCard?
    suspend fun insert(card: CreditCard): Long
    suspend fun update(card: CreditCard): Int
    suspend fun delete(card: CreditCard): Int
}
