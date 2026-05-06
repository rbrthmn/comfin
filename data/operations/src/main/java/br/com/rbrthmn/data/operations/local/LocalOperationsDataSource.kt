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

package br.com.rbrthmn.data.operations.local

import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.dao.ReserveDao
import br.com.rbrthmn.data.dao.TransactionDao
import br.com.rbrthmn.data.entity.TransactionEntity
import br.com.rbrthmn.data.operations.model.AccountItem
import br.com.rbrthmn.data.operations.model.NewOperationData
import br.com.rbrthmn.data.operations.model.OperationItem
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.model.ReserveItem
import br.com.rbrthmn.data.operations.repository.OperationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class LocalOperationsDataSource(
    private val transactionDao: TransactionDao,
    private val bankAccountDao: BankAccountDao,
    private val creditCardDao: CreditCardDao,
    private val reserveDao: ReserveDao
) : OperationsRepository {

    override fun getOperationsForMonth(year: Int, month: Int): Flow<OperationsData> {
        val date = LocalDate.of(year, month, 1)
        val start = date.toEpochDay()
        val end = date.withDayOfMonth(date.lengthOfMonth()).toEpochDay()

        return combine(
            transactionDao.getByMonth(start, end),
            bankAccountDao.getAll(),
            creditCardDao.getAll()
        ) { transactions, accounts, cards ->
            val totalIncome = transactions
                .filter { it.category in INCOME_TYPES }
                .sumOf { it.amount }
            val totalOutcome = transactions
                .filter { it.category !in INCOME_TYPES }
                .sumOf { it.amount }
            val operations = transactions.map { t ->
                val accountName = accounts.find { it.id == t.bankAccountId }?.name
                    ?: cards.find { it.id == t.creditCardId }?.name
                OperationItem(
                    id = t.id,
                    date = LocalDate.ofEpochDay(t.date),
                    counterparty = t.counterparty,
                    notes = t.notes,
                    amount = t.amount,
                    type = t.type,
                    category = t.category,
                    accountName = accountName
                )
            }.sortedByDescending { it.date }

            OperationsData(
                operations = operations,
                totalIncome = totalIncome,
                totalOutcome = totalOutcome,
                totalBalance = totalIncome - totalOutcome
            )
        }
    }

    override fun getAvailableAccounts(): Flow<List<AccountItem>> =
        bankAccountDao.getAll().map { accounts ->
            accounts.map { AccountItem(id = it.id, name = it.name, bankName = it.bankName) }
        }

    override fun getAvailableReserves(): Flow<List<ReserveItem>> =
        reserveDao.getAll().map { reserves ->
            reserves.map { ReserveItem(id = it.id, name = it.name, institution = it.institution) }
        }

    override suspend fun addOperation(data: NewOperationData): Result<Unit> = runCatching {
        val (bankAccountId, creditCardId, reserveId) = when (data) {
            is NewOperationData.BankAccountOperation -> Triple(data.bankAccountId, null, null)
            is NewOperationData.CreditCardOperation  -> Triple(null, data.creditCardId, null)
            is NewOperationData.ReserveOperation     -> Triple(data.bankAccountId, null, data.reserveId)
        }
        transactionDao.insert(
            TransactionEntity(
                counterparty = data.counterparty,
                amount = data.amount,
                type = data.type,
                category = data.category,
                date = data.date.toEpochDay(),
                notes = data.notes,
                bankAccountId = bankAccountId,
                creditCardId = creditCardId,
                reserveId = reserveId
            )
        )
    }

    private companion object {
        val INCOME_TYPES = setOf("INCOME", "DEPOSIT", "RESERVE_REDEMPTION")
    }
}
