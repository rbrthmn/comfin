package br.com.rbrthmn.data.home.repository

import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HomeRepository {
    fun getAccountsSummary(month: LocalDate): Flow<AccountsSummary>
    fun getCreditCardBills(month: LocalDate): Flow<List<CreditCardBillItem>>
    fun getLastMonthDifference(month: LocalDate): Flow<Double>
    fun getMonthlySpent(month: LocalDate): Flow<Double>
}
