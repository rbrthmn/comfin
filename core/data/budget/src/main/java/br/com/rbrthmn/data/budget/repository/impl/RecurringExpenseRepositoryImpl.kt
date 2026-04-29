package br.com.rbrthmn.data.budget.repository.impl

import br.com.rbrthmn.data.dao.RecurringExpenseDao
import br.com.rbrthmn.data.budget.mapper.toDomain
import br.com.rbrthmn.data.budget.mapper.toEntity
import br.com.rbrthmn.data.budget.model.RecurringExpense
import br.com.rbrthmn.data.budget.repository.RecurringExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecurringExpenseRepositoryImpl(private val dao: RecurringExpenseDao) : RecurringExpenseRepository {
    override fun getAll(): Flow<List<RecurringExpense>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun getById(id: Long): RecurringExpense? = dao.getById(id)?.toDomain()
    override suspend fun insert(expense: RecurringExpense): Long = dao.insert(expense.toEntity())
    override suspend fun update(expense: RecurringExpense): Int = dao.update(expense.toEntity())
    override suspend fun delete(expense: RecurringExpense): Int = dao.delete(expense.toEntity())
}
