package br.com.rbrthmn.data.finance.repository.impl

import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.finance.mapper.toDomain
import br.com.rbrthmn.data.finance.mapper.toEntity
import br.com.rbrthmn.data.finance.model.BankAccount
import br.com.rbrthmn.data.finance.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BankAccountRepositoryImpl(private val dao: BankAccountDao) : BankAccountRepository {
    override fun getAll(): Flow<List<BankAccount>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): BankAccount? = dao.getById(id)?.toDomain()
    override suspend fun insert(account: BankAccount): Long = dao.insert(account.toEntity())
    override suspend fun update(account: BankAccount): Int = dao.update(account.toEntity())
    override suspend fun delete(account: BankAccount): Int = dao.delete(account.toEntity())
}
