package br.com.rbrthmn.data.budget.repository

import br.com.rbrthmn.data.budget.model.RecurringExpense
import kotlinx.coroutines.flow.Flow

interface RecurringExpenseRepository {
    fun getAll(): Flow<List<RecurringExpense>>
    suspend fun getById(id: Long): RecurringExpense?
    suspend fun insert(expense: RecurringExpense): Long
    suspend fun update(expense: RecurringExpense): Int
    suspend fun delete(expense: RecurringExpense): Int
}
