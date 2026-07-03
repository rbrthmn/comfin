package br.com.rbrthmn.operations.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.operations.R
import br.com.rbrthmn.operations.ui.OperationsScreenContract
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import br.com.rbrthmn.ui.R as uiR

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    uiState: OperationsScreenContract.UiState,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.shadow(elevation = dimensionResource(id = uiR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = uiR.dimen.padding_medium))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = dimensionResource(id = uiR.dimen.padding_small))
            ) {
                Text(
                    text = valueWithCurrencyString(
                        currencyStringId = uiR.string.brl_currency,
                        value = uiState.totalBalance
                    ),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = stringResource(id = R.string.balance_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = uiR.dimen.padding_small))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = valueWithCurrencyString(
                            currencyStringId = uiR.string.brl_currency,
                            value = uiState.totalIncome
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(id = R.string.incomes_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = valueWithCurrencyString(
                            currencyStringId = uiR.string.brl_currency,
                            value = uiState.totalOutcome
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(id = R.string.outflow_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TotalBalanceCardPreview() {
    TotalBalanceCard(
        uiState = OperationsScreenContract.UiState(
            totalBalance = "100,00",
            totalOutcome = "50,00",
            totalIncome = "50,00"
        )
    )
}
