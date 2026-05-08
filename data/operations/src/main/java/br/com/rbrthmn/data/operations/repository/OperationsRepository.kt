package br.com.rbrthmn.data.operations.repository

import br.com.rbrthmn.data.operations.model.AccountItem
import br.com.rbrthmn.data.operations.model.NewOperationData
import br.com.rbrthmn.data.operations.model.OperationsData
import br.com.rbrthmn.data.operations.model.ReserveItem
import kotlinx.coroutines.flow.Flow

interface OperationsRepository {
    fun getOperationsForMonth(year: Int, month: Int): Flow<OperationsData>
    fun getAvailableAccounts(): Flow<List<AccountItem>>
    fun getAvailableReserves(): Flow<List<ReserveItem>>
    suspend fun addOperation(data: NewOperationData): Result<Unit>
    suspend fun deleteOperation(id: Long): Result<Unit>
    suspend fun updateOperation(id: Long, data: NewOperationData): Result<Unit>
}
