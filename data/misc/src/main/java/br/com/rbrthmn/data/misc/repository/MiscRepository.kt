package br.com.rbrthmn.data.misc.repository

import br.com.rbrthmn.data.misc.model.AllocationDetail
import br.com.rbrthmn.data.misc.model.InvestmentDetail
import br.com.rbrthmn.data.misc.model.RecurringExpenseDetail
import br.com.rbrthmn.data.misc.model.ReserveDetail
import kotlinx.coroutines.flow.Flow

interface MiscRepository {
    fun getReserves(): Flow<List<ReserveDetail>>
    fun getInvestments(): Flow<List<InvestmentDetail>>
    fun getAllocations(): Flow<List<AllocationDetail>>
    fun getRecurringExpenses(): Flow<List<RecurringExpenseDetail>>
}
