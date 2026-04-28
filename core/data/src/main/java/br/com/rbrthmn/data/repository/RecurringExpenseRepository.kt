package br.com.rbrthmn.data.repository

import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import kotlinx.coroutines.flow.Flow

interface RecurringExpenseRepository {
    fun getAll(): Flow<List<RecurringExpenseEntity>>
    suspend fun getById(id: Long): RecurringExpenseEntity?
    suspend fun insert(expense: RecurringExpenseEntity): Long
    suspend fun update(expense: RecurringExpenseEntity): Int
    suspend fun delete(expense: RecurringExpenseEntity): Int
}
