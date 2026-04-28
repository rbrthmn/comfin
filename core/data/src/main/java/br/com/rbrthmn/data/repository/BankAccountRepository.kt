package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.BankAccountEntity
import kotlinx.coroutines.flow.Flow

interface BankAccountRepository {
    fun getAll(): Flow<List<BankAccountEntity>>
    suspend fun getById(id: Long): BankAccountEntity?
    suspend fun insert(account: BankAccountEntity): Long
    suspend fun update(account: BankAccountEntity): Int
    suspend fun delete(account: BankAccountEntity): Int
}
