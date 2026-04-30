/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rbrthmn.data

import br.com.rbrthmn.data.db.ComFinDatabase
import br.com.rbrthmn.data.entity.AllocationEntity
import br.com.rbrthmn.data.entity.BankAccountEntity
import br.com.rbrthmn.data.entity.CreditCardEntity
import br.com.rbrthmn.data.entity.InvestmentEntity
import br.com.rbrthmn.data.entity.RecurringExpenseEntity
import br.com.rbrthmn.data.entity.ReserveEntity
import br.com.rbrthmn.data.entity.ReserveTransactionEntity
import br.com.rbrthmn.data.entity.TransactionEntity
import java.time.LocalDate

object DatabaseSeeder {
    suspend fun seed(db: ComFinDatabase) {
        val accountIds = seedBankAccounts(db)
        val cardIds = seedCreditCards(db)
        val reserveIds = seedReserves(db)
        seedInvestments(db)
        seedRecurringExpenses(db)
        seedAllocations(db)
        seedTransactions(db, accountIds, cardIds, reserveIds)
    }

    private suspend fun seedBankAccounts(db: ComFinDatabase): Map<String, Long> {
        val dao = db.bankAccountDao()
        return mapOf(
            "nubank" to dao.insert(BankAccountEntity(
                name = "Nubank",
                bankName = "Nubank",
                balanceValue = 4554.0,
                isMainAccount = true
            )),
            "picpay" to dao.insert(BankAccountEntity(
                name = "PicPay",
                bankName = "PicPay",
                balanceValue = 1200.0,
                isMainAccount = false
            )),
            "btg" to dao.insert(BankAccountEntity(
                name = "BTG",
                bankName = "BTG Pactual",
                balanceValue = 500.0,
                isMainAccount = false
            )),
        )
    }

    private suspend fun seedCreditCards(db: ComFinDatabase): Map<String, Long> {
        val dao = db.creditCardDao()
        return mapOf(
            "nubank" to dao.insert(CreditCardEntity(
                name = "Nubank",
                issuerName = "Nubank",
                currentStatementValue = 182.17,
                availableLimit = 5000.0,
                dueDay = 10
            )),
            "btg" to dao.insert(CreditCardEntity(
                name = "BTG",
                issuerName = "BTG Pactual",
                currentStatementValue = 2075.13,
                availableLimit = 8000.0,
                dueDay = 15
            )),
            "xp" to dao.insert(CreditCardEntity(
                name = "XP",
                issuerName = "XP Investimentos",
                currentStatementValue = 4973.39,
                availableLimit = 10000.0,
                dueDay = 20
            )),
        )
    }

    private suspend fun seedReserves(db: ComFinDatabase): Map<String, Long> {
        val reserveDao = db.reserveDao()
        val txDao = db.reserveTransactionDao()

        val emergencialId = reserveDao.insert(ReserveEntity(
            name = "Reserva emergencial",
            institution = "PicPay",
            currentTotal = 11_500.0,
            monthlyYield = 16.37,
            totalInflow = 21_126.55,
            totalOutflow = 9_038.14
        ))
        listOf(
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2026, 1, 7).toEpochDay(),  inflow = 1130.0,   outflow = 0.0,    yield = 16.37,  value = 11_500.0),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 12, 8).toEpochDay(), inflow = 1130.0,   outflow = 0.0,    yield = 109.25, value = 10_353.63),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 11, 21).toEpochDay(),inflow = 1130.0,   outflow = 0.0,    yield = 84.77,  value = 9_114.38),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 11, 10).toEpochDay(),inflow = 335.59,   outflow = 450.0,  yield = 0.0,    value = 7_899.61),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 10, 14).toEpochDay(),inflow = 1427.19,  outflow = 0.0,    yield = 77.97,  value = 8_014.02),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 9, 22).toEpochDay(), inflow = 0.0,      outflow = 335.59, yield = 68.63,  value = 6_508.86),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 8, 6).toEpochDay(),  inflow = 1130.0,   outflow = 0.0,    yield = 60.89,  value = 6_775.82),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 7, 10).toEpochDay(), inflow = 1380.0,   outflow = 0.0,    yield = 63.84,  value = 5_584.93),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 6, 16).toEpochDay(), inflow = 2900.0,   outflow = 0.0,    yield = 25.83,  value = 4_141.09),
            ReserveTransactionEntity(reserveId = emergencialId, date = LocalDate.of(2025, 5, 10).toEpochDay(), inflow = 1000.0,   outflow = 0.0,    yield = 6.31,   value = 1_215.26),
        ).forEach { txDao.insert(it) }

        val recisaoId = reserveDao.insert(ReserveEntity(
            name = "Recisão",
            institution = "Nubank",
            currentTotal = 0.0,
            monthlyYield = 0.0,
            totalInflow = 11_027.68,
            totalOutflow = 11_433.95
        ))
        listOf(
            ReserveTransactionEntity(reserveId = recisaoId, date = LocalDate.of(2024, 5, 4).toEpochDay(),  inflow = 0.0,       outflow = 1334.07, yield = 5.64,   value = 0.0),
            ReserveTransactionEntity(reserveId = recisaoId, date = LocalDate.of(2024, 4, 1).toEpochDay(),  inflow = 0.0,       outflow = 592.0,   yield = 69.21,  value = 1334.07),
            ReserveTransactionEntity(reserveId = recisaoId, date = LocalDate.of(2024, 3, 1).toEpochDay(),  inflow = 0.0,       outflow = 2400.0,  yield = 378.49, value = 1926.07),
            ReserveTransactionEntity(reserveId = recisaoId, date = LocalDate.of(2024, 1, 2).toEpochDay(),  inflow = 0.0,       outflow = 7100.0,  yield = 0.0,    value = 4326.07),
            ReserveTransactionEntity(reserveId = recisaoId, date = LocalDate.of(2023, 9, 1).toEpochDay(),  inflow = 11_027.68, outflow = 0.0,     yield = 0.0,    value = 11_027.68),
        ).forEach { txDao.insert(it) }

        val fgtsId = reserveDao.insert(ReserveEntity(
            name = "FGTS",
            institution = "Caixa Econômica Federal",
            currentTotal = 0.0,
            monthlyYield = 0.0,
            totalInflow = 0.0,
            totalOutflow = 1_507.95
        ))
        listOf(
            ReserveTransactionEntity(reserveId = fgtsId, date = LocalDate.of(2024, 4, 1).toEpochDay(), inflow = 0.0, outflow = 1507.95, yield = 0.0, value = 0.0),
        ).forEach { txDao.insert(it) }

        return mapOf(
            "emergencial" to emergencialId,
            "recisao" to recisaoId,
            "fgts" to fgtsId
        )
    }

    private suspend fun seedInvestments(db: ComFinDatabase) {
        val dao = db.investmentDao()
        listOf(
            InvestmentEntity(type = "Renda Fixa", institution = "Itaú", assetName = "Tesouro Prefixado 2026",
                purchaseDate = LocalDate.of(2023, 4, 20).toEpochDay(), expiryDate = LocalDate.of(2026, 1, 1).toEpochDay(),
                quantity = 1.83, proventos = 0.0, investedValue = 1350.43, currentValue = 1757.12),
            InvestmentEntity(type = "Renda Fixa", institution = "Itaú", assetName = "CDB LUSOBRASILEIROBM",
                purchaseDate = LocalDate.of(2023, 4, 27).toEpochDay(), expiryDate = LocalDate.of(2026, 4, 15).toEpochDay(),
                quantity = 1.0, proventos = 0.0, investedValue = 1000.0, currentValue = 1374.96),
            InvestmentEntity(type = "Renda Fixa", institution = "XP Investimentos", assetName = "CDB Banco Carrefour",
                purchaseDate = LocalDate.of(2025, 7, 1).toEpochDay(), expiryDate = LocalDate.of(2026, 2, 4).toEpochDay(),
                quantity = 1.0, proventos = 0.0, investedValue = 5000.0, currentValue = 5213.58),
            InvestmentEntity(type = "Renda Fixa", institution = "BTG Investimentos", assetName = "LFT Tesouro Selic 2028",
                purchaseDate = LocalDate.of(2025, 9, 29).toEpochDay(), expiryDate = LocalDate.of(2028, 3, 1).toEpochDay(),
                quantity = 1.0, proventos = 0.0, investedValue = 1917.12, currentValue = 1356.34),
            InvestmentEntity(type = "FII", institution = "Nubank", assetName = "VILG11 (Vinci Logística)",
                purchaseDate = null, expiryDate = null,
                quantity = 5.0, proventos = 39.75, investedValue = 589.05, currentValue = 461.9),
            InvestmentEntity(type = "FII", institution = "Nubank", assetName = "TGAR11 (TG Ativo Real)",
                purchaseDate = null, expiryDate = null,
                quantity = 5.0, proventos = 62.7, investedValue = 682.34, currentValue = 436.8),
            InvestmentEntity(type = "FII", institution = "Nubank", assetName = "MXRF11 (Maxi Renda)",
                purchaseDate = null, expiryDate = null,
                quantity = 40.0, proventos = 46.0, investedValue = 423.2, currentValue = 383.6),
            InvestmentEntity(type = "Ações", institution = "BTG Investimentos", assetName = "RANI3",
                purchaseDate = LocalDate.of(2025, 11, 21).toEpochDay(), expiryDate = null,
                quantity = 71.0, proventos = 0.0, investedValue = 596.53, currentValue = 596.4),
            InvestmentEntity(type = "Ações", institution = "BTG Investimentos", assetName = "ITSA4",
                purchaseDate = LocalDate.of(2025, 11, 21).toEpochDay(), expiryDate = null,
                quantity = 50.0, proventos = 0.0, investedValue = 588.5, currentValue = 588.5),
            InvestmentEntity(type = "Ações", institution = "BTG Investimentos", assetName = "PETR4F",
                purchaseDate = LocalDate.of(2025, 10, 1).toEpochDay(), expiryDate = null,
                quantity = 47.0, proventos = 0.0, investedValue = 1477.68, currentValue = 1527.03),
            InvestmentEntity(type = "Criptomoeda", institution = "Binance", assetName = "Bitcoin",
                purchaseDate = null, expiryDate = null,
                quantity = 298909.0, proventos = 0.0, investedValue = 0.0, currentValue = 1453.99),
        ).forEach { dao.insert(it) }
    }

    private suspend fun seedRecurringExpenses(db: ComFinDatabase) {
        val dao = db.recurringExpenseDao()
        listOf(
            RecurringExpenseEntity(description = "Boxe",               amount = 80.0,  paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Combustível",        amount = 300.0, paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Youtube Premium",    amount = 26.9,  paymentDay = 5,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Amazon Prime",       amount = 19.9,  paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Recarga TIM",        amount = 15.0,  paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Google One",         amount = 14.99, paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Doação Petit Journal",amount = 10.0, paymentDay = 10, validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Mega Sena",          amount = 6.0,   paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Forró",              amount = 0.0,   paymentDay = 10, validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Terapia",            amount = 0.0,   paymentDay = 1,  validUntil = null, isPaid = false),
            RecurringExpenseEntity(description = "Escalada",           amount = 0.0,   paymentDay = 1,  validUntil = null, isPaid = false),
        ).forEach { dao.insert(it) }
    }

    private suspend fun seedAllocations(db: ComFinDatabase) {
        val dao = db.allocationDao()
        listOf(
            AllocationEntity(name = "Investimentos",     plannedPercentage = 10.1, targetValue = 460.0,  actualAllocated = 460.0),
            AllocationEntity(name = "Prev. Privada",     plannedPercentage = 10.1, targetValue = 460.0,  actualAllocated = 460.0),
            AllocationEntity(name = "Prev. Pública",     plannedPercentage = 10.1, targetValue = 460.8,  actualAllocated = 460.8),
            AllocationEntity(name = "Reserva",           plannedPercentage = 20.2, targetValue = 920.0,  actualAllocated = 920.0),
            AllocationEntity(name = "Presente",          plannedPercentage = 0.0,  targetValue = 0.0,    actualAllocated = 0.0),
            AllocationEntity(name = "Gastos recorrentes",plannedPercentage = 10.5, targetValue = 478.77, actualAllocated = 478.77),
        ).forEach { dao.insert(it) }
    }

    private suspend fun seedTransactions(
        db: ComFinDatabase,
        accountIds: Map<String, Long>,
        cardIds: Map<String, Long>,
        reserveIds: Map<String, Long>
    ) {
        val dao = db.transactionDao()
        val nubankId = accountIds["nubank"]
        val emergencialReserveId = reserveIds["emergencial"]

        listOf(
            TransactionEntity(date = LocalDate.of(2026, 1, 31).toEpochDay(), counterparty = "Izzy",                   notes = "Aposta",             amount = -10.0,   type = "PIX",    category = "Lazer",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 28).toEpochDay(), counterparty = "Mila",                   notes = null,                 amount = 1000.0,  type = "PIX",    category = "Renda",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 25).toEpochDay(), counterparty = "Resgaute Ltda",          notes = "Doação",             amount = -20.0,   type = "PIX",    category = "Doação",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 22).toEpochDay(), counterparty = "Cafe Store Commercio",   notes = null,                 amount = -66.52,  type = "PIX",    category = "Alimentação",  bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 20).toEpochDay(), counterparty = "Petit Jornal",           notes = "Doação",             amount = -10.0,   type = "PIX",    category = "Doação",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 18).toEpochDay(), counterparty = "Pedro",                  notes = "Show",               amount = 556.08,  type = "PIX",    category = "Renda",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 15).toEpochDay(), counterparty = "Luan Yuti",              notes = "Doação",             amount = -5.0,    type = "TED",    category = "Doação",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 12).toEpochDay(), counterparty = "Izzy",                   notes = "Passagem carnaval",  amount = 414.84,  type = "PIX",    category = "Viagem",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 10).toEpochDay(), counterparty = "Fabrício",               notes = null,                 amount = -20.0,   type = "PIX",    category = "Outros",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 8).toEpochDay(),  counterparty = "Roberto PicPay",         notes = "Metade mentoria",    amount = 2792.0,  type = "PIX",    category = "Renda",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 6).toEpochDay(),  counterparty = "Mila",                   notes = "Psi",                amount = 300.0,   type = "PIX",    category = "Renda",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 5).toEpochDay(),  counterparty = "Vakinha",                notes = "Ajuda Gabriel",      amount = -25.0,   type = "PIX",    category = "Doação",       bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 4).toEpochDay(),  counterparty = "Biscoitos J Pereira",    notes = null,                 amount = -8.0,    type = "PIX",    category = "Alimentação",  bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 3).toEpochDay(),  counterparty = "Isabela",                notes = "Terapia",            amount = -600.0,  type = "PIX",    category = "Saúde",        bankAccountId = nubankId, creditCardId = null, reserveId = null),
            TransactionEntity(date = LocalDate.of(2026, 1, 7).toEpochDay(),  counterparty = "PicPay - Res. Emergencial", notes = "Aporte mensal", amount = -1130.0, type = "PIX",    category = "Reserva",      bankAccountId = nubankId, creditCardId = null, reserveId = emergencialReserveId),
        ).forEach { dao.insert(it) }
    }
}
