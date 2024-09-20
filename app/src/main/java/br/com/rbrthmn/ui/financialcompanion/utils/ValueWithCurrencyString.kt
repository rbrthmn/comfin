package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun valueWithCurrencyString(currencyStringId: Int, value: String) = "${stringResource(id = currencyStringId)} $value"