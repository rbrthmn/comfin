package br.com.rbrthmn.data.misc.local

import br.com.rbrthmn.data.dao.budget.AllocationDao
import br.com.rbrthmn.data.dao.wealth.InvestmentDao
import br.com.rbrthmn.data.dao.budget.RecurringExpenseDao
import br.com.rbrthmn.data.dao.wealth.ReserveDao
import br.com.rbrthmn.data.dao.wealth.ReserveTransactionDao
import br.com.rbrthmn.data.misc.model.AllocationDetail
import br.com.rbrthmn.data.misc.model.InvestmentDetail
import br.com.rbrthmn.data.misc.model.RecurringExpenseDetail
import br.com.rbrthmn.data.misc.model.ReserveDetail
import br.com.rbrthmn.data.misc.model.ReserveTransactionItem
import br.com.rbrthmn.data.misc.repository.MiscRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class LocalMiscDataSource(
    private val reserveDao: ReserveDao,
    private val reserveTransactionDao: ReserveTransactionDao,
    private val investmentDao: InvestmentDao,
    private val allocationDao: AllocationDao,
    private val recurringExpenseDao: RecurringExpenseDao
) : MiscRepository {

    override fun getReserves(): Flow<List<ReserveDetail>> =
        combine(
            reserveDao.getAll(),
            reserveTransactionDao.getAll()
        ) { reserves, transactions ->
            reserves.map { r ->
                ReserveDetail(
                    id = r.id,
                    name = r.name,
                    institution = r.institution,
                    currentTotal = r.currentTotal,
                    monthlyYield = r.monthlyYield,
                    totalInflow = r.totalInflow,
                    totalOutflow = r.totalOutflow,
                    transactions = transactions
                        .filter { it.reserveId == r.id }
                        .map { t ->
                            ReserveTransactionItem(
                                id = t.id,
                                date = LocalDate.ofEpochDay(t.date),
                                inflow = t.inflow,
                                outflow = t.outflow,
                                yield = t.yield,
                                value = t.value
                            )
                        }
                )
            }
        }

    override fun getInvestments(): Flow<List<InvestmentDetail>> =
        investmentDao.getAll().map { investments ->
            investments.map { i ->
                InvestmentDetail(
                    id = i.id,
                    type = i.type,
                    institution = i.institution,
                    assetName = i.assetName,
                    purchaseDate = i.purchaseDate?.let { LocalDate.ofEpochDay(it) },
                    expiryDate = i.expiryDate?.let { LocalDate.ofEpochDay(it) },
                    quantity = i.quantity,
                    proventos = i.proventos,
                    investedValue = i.investedValue,
                    currentValue = i.currentValue
                )
            }
        }

    override fun getAllocations(): Flow<List<AllocationDetail>> =
        allocationDao.getAll().map { allocations ->
            allocations.map { a ->
                AllocationDetail(
                    id = a.id,
                    name = a.name,
                    plannedPercentage = a.plannedPercentage,
                    targetValue = a.targetValue,
                    actualAllocated = a.actualAllocated
                )
            }
        }

    override fun getRecurringExpenses(): Flow<List<RecurringExpenseDetail>> =
        recurringExpenseDao.getAll().map { expenses ->
            expenses.map { e ->
                RecurringExpenseDetail(
                    id = e.id,
                    description = e.description,
                    amount = e.amount,
                    paymentDay = e.paymentDay,
                    validUntil = e.validUntil?.let { LocalDate.ofEpochDay(it) },
                    isPaid = e.isPaid
                )
            }
        }
}
