package br.com.rbrthmn.data.mapper

import br.com.rbrthmn.data.entity.CreditCardEntity
import br.com.rbrthmn.data.model.CreditCard

fun CreditCardEntity.toDomain() = CreditCard(
    id = id,
    name = name,
    issuerName = issuerName,
    currentStatementValue = currentStatementValue,
    availableLimit = availableLimit,
    dueDay = dueDay
)

fun CreditCard.toEntity() = CreditCardEntity(
    id = id,
    name = name,
    issuerName = issuerName,
    currentStatementValue = currentStatementValue,
    availableLimit = availableLimit,
    dueDay = dueDay
)
