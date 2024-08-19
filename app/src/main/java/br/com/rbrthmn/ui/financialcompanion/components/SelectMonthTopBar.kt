package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import br.com.rbrthmn.ui.financialcompanion.utils.nextMonth
import br.com.rbrthmn.ui.financialcompanion.utils.previousMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMonthTopBar(
    currentMonth: MonthsOfTheYear,
    onCurrentMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    val currentMonthStringId by remember { mutableIntStateOf(currentMonth.stringId) }
    val previousMonthStringId by remember { mutableIntStateOf(currentMonth.previousMonth().stringId) }
    val nextMonthStringId by remember { mutableIntStateOf(currentMonth.nextMonth().stringId) }

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = previousMonthStringId),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.clickable { onPreviousMonthClick() }
                )
                Text(
                    text = stringResource(id = currentMonthStringId),
                    fontSize = 30.sp,
                    modifier = modifier
                        .padding(15.dp)
                        .clickable { onCurrentMonthClick() }
                )
                Text(
                    text = stringResource(id = nextMonthStringId),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    modifier = modifier.clickable { onNextMonthClick() }
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
            .shadow(elevation = 10.dp)
            .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MonthTopAppBarPreview(modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    SelectMonthTopBar(
        currentMonth = MonthsOfTheYear.December,
        onCurrentMonthClick = { println("CURRENT MONTH") },
        onPreviousMonthClick = { println("PREVIOUS MONTH") },
        onNextMonthClick = { println("NEXT MONTH") },
        scrollBehavior = scrollBehavior,
    )
}
