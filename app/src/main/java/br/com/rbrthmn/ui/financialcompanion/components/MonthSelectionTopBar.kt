package br.com.rbrthmn.ui.financialcompanion.components

import InfiniteCircularList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthSelectionTopBar(
    currentMonth: MonthsOfTheYear,
    modifier: Modifier = Modifier
) {
    val currentMonthStringId by remember { mutableIntStateOf(currentMonth.longStringId) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog)
        MonthSelectionDialog(
            onDismissRequest = { showDialog = false },
            currentYear = currentYear,
            currentMonth = currentMonth
        )

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.month_top_bar_corner_shape_round)))
                    .clickable { showDialog = true }
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = stringResource(id = currentMonthStringId),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    modifier = modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.drop_down_arrow_icon_description)
                )
            }
        },
        modifier = modifier
            .shadow(elevation = dimensionResource(id = R.dimen.month_top_bar_elevation))
            .clip(
                RoundedCornerShape(
                    bottomStart = dimensionResource(id = R.dimen.month_top_bar_corner_shape_round),
                    bottomEnd = dimensionResource(id = R.dimen.month_top_bar_corner_shape_round)
                )
            )
    )
}

@Composable
private fun MonthSelectionDialog(
    onDismissRequest: () -> Unit,
    currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    currentMonth: MonthsOfTheYear = MonthsOfTheYear.entries.first(),
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                    ) {
                        InfiniteCircularList(
                            width = dimensionResource(id = R.dimen.dates_circular_list_width),
                            itemHeight = dimensionResource(id = R.dimen.dates_circular_list_item_height),
                            items = getYearsList(),
                            initialItem = currentYear,
                            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                            textColor = Color.LightGray,
                            selectedTextColor = Color.Black,
                            onItemSelected = { _, _ -> }
                        )
                        InfiniteCircularList(
                            width = dimensionResource(id = R.dimen.dates_circular_list_width),
                            itemHeight = dimensionResource(id = R.dimen.dates_circular_list_item_height),
                            items = MonthsOfTheYear.entries.map { stringResource(id = it.shortStringId) },
                            initialItem = currentMonth,
                            fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                            textColor = Color.LightGray,
                            selectedTextColor = Color.Black,
                            onItemSelected = { _, _ -> }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = { onDismissRequest() }) {
                            Text(text = stringResource(id = R.string.apply_date_button))
                        }
                        OutlinedButton(onClick = { onDismissRequest() }) {
                            Text(text = stringResource(id = R.string.current_month_button))
                        }
                    }
                }
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(dimensionResource(id = R.dimen.padding_small))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon_description)
                    )
                }

            }
        }
    }
}

private fun getYearsList(): List<Int> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return (currentYear - 20..currentYear + 10).toList()
}

@Preview
@Composable
fun MonthSelectionTopBarPreview(modifier: Modifier = Modifier) {
    MonthSelectionTopBar(
        currentMonth = MonthsOfTheYear.DECEMBER,
    )
}

@Preview
@Composable
fun MonthSelectionDialogPreview(modifier: Modifier = Modifier) {
    MonthSelectionDialog(onDismissRequest = {})
}
