package br.com.rbrthmn.data.home.repository

import br.com.rbrthmn.data.home.model.AccountsSummary
import br.com.rbrthmn.data.home.model.CreditCardBillItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAccountsSummary(): Flow<AccountsSummary>
    fun getCreditCardBills(): Flow<List<CreditCardBillItem>>
}
