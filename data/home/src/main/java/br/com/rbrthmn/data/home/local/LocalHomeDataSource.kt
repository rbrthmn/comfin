package br.com.rbrthmn.data.home.local

import br.com.rbrthmn.data.dao.BankAccountDao
import br.com.rbrthmn.data.dao.CreditCardDao
import br.com.rbrthmn.data.dao.TransactionDao
import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.AccountSummaryItem
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import br.com.rbrthmn.data.home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class LocalHomeDataSource(
    private val bankAccountDao: BankAccountDao,
    private val creditCardDao: CreditCardDao,
    private val transactionDao: TransactionDao
) : HomeRepository {

    override fun getAccountsSummary(month: LocalDate): Flow<AccountsSummary> =
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

    override fun getCreditCardBills(month: LocalDate): Flow<List<CreditCardBillItem>> {
        val start = month.withDayOfMonth(1)
        val end = month.withDayOfMonth(month.lengthOfMonth())
        return combine(
            creditCardDao.getAll(),
            transactionDao.getByMonth(start.toEpochDay(), end.toEpochDay())
        ) { cards, transactions ->
            val spentByCard = transactions
                .filter { it.creditCardId != null }
                .groupBy { it.creditCardId!! }
                .mapValues { (_, txs) -> txs.sumOf { it.amount } }
            cards.map { c ->
                CreditCardBillItem(
                    id = c.id,
                    name = c.name,
                    issuerName = c.issuerName,
                    currentStatementValue = kotlin.math.abs(spentByCard[c.id] ?: 0.0),
                    dueDay = c.dueDay
                )
            }
        }
    }

    override fun getLastMonthDifference(month: LocalDate): Flow<Double> {
        val thisMonthStart = month.withDayOfMonth(1)
        val thisMonthEnd = month.withDayOfMonth(month.lengthOfMonth())
        val lastMonth = month.minusMonths(1)
        val lastMonthStart = lastMonth.withDayOfMonth(1)
        val lastMonthEnd = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())

        val thisMonthFlow = transactionDao.getByMonth(thisMonthStart.toEpochDay(), thisMonthEnd.toEpochDay())
        val lastMonthFlow = transactionDao.getByMonth(lastMonthStart.toEpochDay(), lastMonthEnd.toEpochDay())

        return combine(thisMonthFlow, lastMonthFlow) { thisMonth, lastMonth ->
            thisMonth.sumOf { it.amount } - lastMonth.sumOf { it.amount }
        }
    }

    override fun getMonthlySpent(month: LocalDate): Flow<Double> {
        val start = month.withDayOfMonth(1)
        val end = month.withDayOfMonth(month.lengthOfMonth())
        return transactionDao.getByMonth(start.toEpochDay(), end.toEpochDay()).map { transactions ->
            transactions.filter { it.amount < 0 }.sumOf { it.amount }
        }
    }
}
