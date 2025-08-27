
package br.com.rbrthmn.operations.ui.components

import br.com.rbrthmn.operations.R

sealed class OperationAccountType(val stringId: Int)
data object OperationAimedAccount : OperationAccountType(R.string.aimed_account_hint)
data object OperationOriginAccount : OperationAccountType(R.string.origin_account_hint)
