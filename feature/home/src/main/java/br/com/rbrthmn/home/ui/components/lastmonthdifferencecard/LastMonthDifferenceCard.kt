
package br.com.rbrthmn.home.ui.components.lastmonthdifferencecard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.home.R
import br.com.rbrthmn.ui.utils.valueWithCurrencyString
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import br.com.rbrthmn.ui.R as commonR

@Composable
fun LastMonthDifferenceCard(
    modifier: Modifier = Modifier,
    viewModel: LastMonthDifferenceCardContract.ViewModel = koinViewModel(),
    currentDateFilter: LocalDate
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.onIntent(LastMonthDifferenceCardContract.Intent.OnDateFilterChange(currentDateFilter))

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(bottom = dimensionResource(id = commonR.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = commonR.dimen.padding_medium),
                    horizontal = dimensionResource(id = commonR.dimen.padding_medium)
                )
        ) {
            Text(
                text = stringResource(id = R.string.difference_from_last_month_title),
                fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.weight(0.6f)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(0.4f)) {
                Text(
                    text = valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency,
                        value = uiState.valueOfLastMonth
                    ),
                    fontSize = dimensionResource(id = commonR.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun LastMonthDifferenceCardPreview(modifier: Modifier = Modifier) {
    LastMonthDifferenceCard(
        modifier = modifier,
        viewModel = LastMonthDifferenceCardViewModel(),
        currentDateFilter = LocalDate.now()
    )
}
