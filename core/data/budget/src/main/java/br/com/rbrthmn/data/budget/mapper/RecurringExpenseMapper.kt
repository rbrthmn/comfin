package br.com.rbrthmn.data.budget.mapper

import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import br.com.rbrthmn.data.budget.model.RecurringExpense
import java.time.LocalDate

fun RecurringExpenseEntity.toDomain() = RecurringExpense(
    id = id,
    description = description,
    amount = amount,
    paymentDay = paymentDay,
    validUntil = validUntil?.let { LocalDate.ofEpochDay(it) },
    isPaid = isPaid
)

fun RecurringExpense.toEntity() = RecurringExpenseEntity(
    id = id,
    description = description,
    amount = amount,
    paymentDay = paymentDay,
    validUntil = validUntil?.toEpochDay(),
    isPaid = isPaid
)
