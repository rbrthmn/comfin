package br.com.rbrthmn.data.operations.repository

import br.com.rbrthmn.data.operations.model.AccountItem
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.model.ReserveItem
import kotlinx.coroutines.flow.Flow

interface OperationsRepository {
    fun getOperationsForMonth(year: Int, month: Int): Flow<OperationsData>
    fun getAvailableAccounts(): Flow<List<AccountItem>>
    fun getAvailableReserves(): Flow<List<ReserveItem>>
}
