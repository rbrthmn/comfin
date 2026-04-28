package br.com.rbrthmn.operations.ui

import androidx.annotation.StringRes
import br.com.rbrthmn.operations.R

enum class OperationType(
    @StringRes val stringId: Int,
    val hasReserve: Boolean = false,
    val hasOriginAccount: Boolean = false,
    val hasDestinationAccount: Boolean = false
) {
    PIX(stringId = R.string.operation_type_pix, hasOriginAccount = true, hasDestinationAccount = true),
    TED(stringId = R.string.operation_type_ted, hasOriginAccount = true, hasDestinationAccount = true),
    DEBIT_PURCHASE(stringId = R.string.operation_type_debit_purchase, hasOriginAccount = true),
    CREDIT_PURCHASE(stringId = R.string.operation_type_credit_purchase),
    BILL_PAYMENT(stringId = R.string.operation_type_bill_payment, hasOriginAccount = true),
    WITHDRAWAL(stringId = R.string.operation_type_withdrawal, hasOriginAccount = true),
    DEPOSIT(stringId = R.string.operation_type_deposit, hasDestinationAccount = true),
    RESERVE_CONTRIBUTION(
        stringId = R.string.operation_type_reserve_contribution,
        hasReserve = true,
        hasOriginAccount = true
    ),
    RESERVE_REDEMPTION(
        stringId = R.string.operation_type_reserve_redemption,
        hasReserve = true,
        hasDestinationAccount = true
    ),
    INCOME(stringId = R.string.operation_type_income, hasDestinationAccount = true),
    OTHER(stringId = R.string.operation_type_other, hasOriginAccount = true)
}
