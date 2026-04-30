package br.com.rbrthmn.data.wealth.mapper

import br.com.rbrthmn.data.entity.ReserveEntity
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import br.com.rbrthmn.data.wealth.model.Reserve
import br.com.rbrthmn.data.wealth.model.ReserveTransaction
import java.time.LocalDate

fun ReserveEntity.toDomain() = Reserve(
    id = id,
    name = name,
    institution = institution,
    currentTotal = currentTotal,
    monthlyYield = monthlyYield,
    totalInflow = totalInflow,
    totalOutflow = totalOutflow
)

fun Reserve.toEntity() = ReserveEntity(
    id = id,
    name = name,
    institution = institution,
    currentTotal = currentTotal,
    monthlyYield = monthlyYield,
    totalInflow = totalInflow,
    totalOutflow = totalOutflow
)

fun ReserveTransactionEntity.toDomain() = ReserveTransaction(
    id = id,
    reserveId = reserveId,
    date = LocalDate.ofEpochDay(date),
    inflow = inflow,
    outflow = outflow,
    yield = yield,
    value = value
)

fun ReserveTransaction.toEntity() = ReserveTransactionEntity(
    id = id,
    reserveId = reserveId,
    date = date.toEpochDay(),
    inflow = inflow,
    outflow = outflow,
    yield = yield,
    value = value
)
