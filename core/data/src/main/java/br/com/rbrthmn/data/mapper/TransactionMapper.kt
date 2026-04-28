package br.com.rbrthmn.data.mapper

import br.com.rbrthmn.data.entity.TransactionEntity
import br.com.rbrthmn.data.model.Transaction
import java.time.LocalDate

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    date = LocalDate.ofEpochDay(date),
    counterparty = counterparty,
    notes = notes,
    amount = amount,
    type = type,
    category = category,
    reserveId = reserveId,
    creditCardId = creditCardId,
    bankAccountId = bankAccountId
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    date = date.toEpochDay(),
    counterparty = counterparty,
    notes = notes,
    amount = amount,
    type = type,
    category = category,
    reserveId = reserveId,
    creditCardId = creditCardId,
    bankAccountId = bankAccountId
)
