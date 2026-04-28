package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.entity.BankAccountEntity
import br.com.rbrthmn.data.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow

class BankAccountRepositoryImpl(private val dao: BankAccountDao) : BankAccountRepository {
    override fun getAll(): Flow<List<BankAccountEntity>> = dao.getAll()
    override suspend fun getById(id: Long): BankAccountEntity? = dao.getById(id)
    override suspend fun insert(account: BankAccountEntity): Long = dao.insert(account)
    override suspend fun update(account: BankAccountEntity): Int = dao.update(account)
    override suspend fun delete(account: BankAccountEntity): Int = dao.delete(account)
}
