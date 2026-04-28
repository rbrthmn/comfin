package br.com.rbrthmn.data.mapper

import br.com.rbrthmn.data.entity.BankAccountEntity
import br.com.rbrthmn.data.model.BankAccount

fun BankAccountEntity.toDomain() = BankAccount(
    id = id,
    name = name,
    bankName = bankName,
    balanceValue = balanceValue,
    isMainAccount = isMainAccount
)

fun BankAccount.toEntity() = BankAccountEntity(
    id = id,
    name = name,
    bankName = bankName,
    balanceValue = balanceValue,
    isMainAccount = isMainAccount
)
