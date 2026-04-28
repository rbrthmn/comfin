package br.com.rbrthmn.data.mapper

import br.com.rbrthmn.data.entity.InvestmentEntity
import br.com.rbrthmn.data.model.Investment
import java.time.LocalDate

fun InvestmentEntity.toDomain() = Investment(
    id = id,
    type = type,
    institution = institution,
    assetName = assetName,
    purchaseDate = purchaseDate?.let { LocalDate.ofEpochDay(it) },
    expiryDate = expiryDate?.let { LocalDate.ofEpochDay(it) },
    quantity = quantity,
    proventos = proventos,
    investedValue = investedValue,
    currentValue = currentValue
)

fun Investment.toEntity() = InvestmentEntity(
    id = id,
    type = type,
    institution = institution,
    assetName = assetName,
    purchaseDate = purchaseDate?.toEpochDay(),
    expiryDate = expiryDate?.toEpochDay(),
    quantity = quantity,
    proventos = proventos,
    investedValue = investedValue,
    currentValue = currentValue
)
