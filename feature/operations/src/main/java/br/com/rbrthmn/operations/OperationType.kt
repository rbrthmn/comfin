package br.com.rbrthmn.operations

import androidx.annotation.StringRes

enum class OperationType(
    @StringRes val stringId: Int,
    val hasReserve: Boolean = false,
    val hasOriginAccount: Boolean = false,
    val hasDestinationAccount: Boolean = false
) {
    TRANSFER(
        stringId = R.string.operation_type_transfer,
        hasOriginAccount = true,
        hasDestinationAccount = true
    ),
    DEBIT_PURCHASE(stringId = R.string.operation_type_debit_purchase, hasOriginAccount = true),
    BILL_PAYMENT(stringId = R.string.operation_type_bill_payment, hasOriginAccount = true),
    WITHDRAWAL(stringId = R.string.operation_type_withdrawal, hasOriginAccount = true),
    DEPOSIT(stringId = R.string.operation_type_deposit, hasDestinationAccount = true),
    RESERVE_ALLOCATION(
        stringId = R.string.operation_type_reserve_allocation,
        hasReserve = true,
        hasOriginAccount = true
    ),
    RESERVE_WITHDRAWAL(
        stringId = R.string.operation_type_reserve_withdrawal,
        hasReserve = true,
        hasDestinationAccount = true
    ),
    INCOME(stringId = R.string.operation_type_income, hasDestinationAccount = true),
    OTHER(stringId = R.string.operation_type_other, hasOriginAccount = true)
}