package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.annotation.StringRes
import br.com.rbrthmn.R

enum class OperationType(@StringRes val stringId: Int) {
    TRANSFER(stringId = R.string.operation_type_transfer),
    DEBIT_PURCHASE(stringId = R.string.operation_type_debit_purchase),
    BILL_PAYMENT(stringId = R.string.operation_type_bill_payment),
    WITHDRAWAL(stringId = R.string.operation_type_withdrawal),
    DEPOSIT(stringId = R.string.operation_type_deposit),
    RESERVE_ALLOCATION(stringId = R.string.operation_type_reserve_allocation),
    RESERVE_WITHDRAWAL(stringId = R.string.operation_type_reserve_withdrawal),
    INCOME(stringId = R.string.operation_type_income),
    OTHER(stringId = R.string.operation_type_other)
}