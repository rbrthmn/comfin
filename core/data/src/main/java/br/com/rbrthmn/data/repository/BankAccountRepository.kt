package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.model.BankAccount
import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun getAll(): Flow<List<BankAccount>>
    suspend fun getById(id: Long): BankAccount?
    suspend fun insert(account: BankAccount): Long
    suspend fun update(account: BankAccount): Int
    suspend fun delete(account: BankAccount): Int
}
