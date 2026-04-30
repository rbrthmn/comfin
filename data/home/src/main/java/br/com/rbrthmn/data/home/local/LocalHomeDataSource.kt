package br.com.rbrthmn.data.home.local

import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.AccountSummaryItem
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import br.com.rbrthmn.data.home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalHomeDataSource(
    private val bankAccountDao: BankAccountDao,
    private val creditCardDao: CreditCardDao
) : HomeRepository {

    override fun getAccountsSummary(): Flow<AccountsSummary> =
        bankAccountDao.getAll().map { accounts ->
            AccountsSummary(
                totalBalance = accounts.sumOf { it.balanceValue },
                accounts = accounts.map { a ->
                    AccountSummaryItem(
                        id = a.id,
                        name = a.name,
                        bankName = a.bankName,
                        balance = a.balanceValue,
                        isMainAccount = a.isMainAccount
                    )
                }
            )
        }

    override fun getCreditCardBills(): Flow<List<CreditCardBillItem>> =
        creditCardDao.getAll().map { cards ->
            cards.map { c ->
                CreditCardBillItem(
                    id = c.id,
                    name = c.name,
                    issuerName = c.issuerName,
                    currentStatementValue = c.currentStatementValue,
                    dueDay = c.dueDay
                )
            }
        }
}
