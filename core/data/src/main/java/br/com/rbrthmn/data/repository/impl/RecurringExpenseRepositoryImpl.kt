package br.com.rbrthmn.data.repository.impl

import br.com.rbrthmn.data.dao.RecurringExpenseDao
import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import br.com.rbrthmn.data.repository.RecurringExpenseRepository
import kotlinx.coroutines.flow.Flow

class RecurringExpenseRepositoryImpl(private val dao: RecurringExpenseDao) : RecurringExpenseRepository {
    override fun getAll(): Flow<List<RecurringExpenseEntity>> = dao.getAll()
    override suspend fun getById(id: Long): RecurringExpenseEntity? = dao.getById(id)
    override suspend fun insert(expense: RecurringExpenseEntity): Long = dao.insert(expense)
    override suspend fun update(expense: RecurringExpenseEntity): Int = dao.update(expense)
    override suspend fun delete(expense: RecurringExpenseEntity): Int = dao.delete(expense)
}
