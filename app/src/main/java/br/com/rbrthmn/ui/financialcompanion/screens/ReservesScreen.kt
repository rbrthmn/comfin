package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.AddOperationDialog
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.OperationType
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString
import java.text.SimpleDateFormat
import java.util.Locale

data class Reserve(
    val name: String,
    val value: String,
    val operations: List<ReserveOperation> = listOf()
)

data class ReserveOperation(val date: String, val value: String, val isWithdrawal: Boolean)

object ReservesDestination : NavigationDestination {
    override val route = "reserves"
}

@Composable
fun ReservesScreen(modifier: Modifier = Modifier) {
    val reserveA = Reserve(
        name = "Emergency Fund",
        value = "1000.00",
        operations = listOf(
            ReserveOperation(date = "2023-11-15", value = "200.00", isWithdrawal = false),
            ReserveOperation(date = "2023-11-22", value = "100.00", isWithdrawal = true),
            ReserveOperation(date = "2023-12-01", value = "300.00", isWithdrawal = false)
        )
    )
    val reserveB = Reserve(
        name = "Birthday money",
        value = "100.00",
        operations = listOf()
    )
    val reserves = listOf(reserveA, reserveB)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ReservesCard(reservesTotalValue = "10000", reserves = reserves)
    }
}

@Composable
private fun ReservesCard(reservesTotalValue: String, reserves: List<Reserve>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_medium),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            Row {
Text(text = reservesTotalValue)
            }
            for (reserve in reserves) {
                ReserveItem(reserve = reserve)
            }
            AddReserveButton()
        }
    }
}

@Composable
fun AddReserveButton(modifier: Modifier = Modifier) {
    val showDialog = remember { mutableStateOf(false) }

//    if (showDialog.value) {
////        NewOperationDialog(
////            onSaveButtonClick = { showDialog.value = false },
////            onCancelButtonClick = { showDialog.value = false })
//    }

    Button(
        onClick = { showDialog.value = true },
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.fillMaxWidth()
        ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_icon_description),
                    tint = Color.Gray,
                    modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                )
                Text(
                    text = stringResource(id = R.string.add_reserve_button),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
        }
    }
}

@Composable
private fun ReserveItem(modifier: Modifier = Modifier, reserve: Reserve) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f, label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = reserve.name,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency, value = reserve.value
                    ),
                    maxLines = 1,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    modifier = Modifier
                        .alpha(0.2f)
                        .rotate(rotationState)
                        .padding(0.dp),
                    onClick = {
                        isExpanded = !isExpanded
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
        }

        if (isExpanded) {
            ReserveOperationsList(reserve, modifier)
        }
    }
}

@Composable
private fun ReserveOperationsList(
    reserve: Reserve,
    modifier: Modifier
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AddOperationDialog(
            onSaveButtonClick = { showDialog.value = false },
            onCancelButtonClick = { showDialog.value = false },
            availableOperationTypes = listOf(OperationType.RESERVE_ALLOCATION, OperationType.RESERVE_WITHDRAWAL)
        )
    }

    Column(
        modifier = Modifier
//                    .background(Color.LightGray)
        ,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_extra_small))
    ) {
        for (operation in reserve.operations) {
            val operationValueColor = if (operation.isWithdrawal) Color.Red else Color.Green
            val operationValueSymbol = if (operation.isWithdrawal) "-" else "+"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                    .padding(vertical = dimensionResource(id = R.dimen.padding_extra_small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(operation.date)!!
                    ),
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
                Text(
                    text = operationValueSymbol + valueWithCurrencyString(
                        currencyStringId = R.string.brl_currency,
                        value = operation.value
                    ),
                    color = operationValueColor,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
        TextButton(
            onClick = { showDialog.value = true },
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier.fillMaxWidth()
            ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_icon_description),
                        tint = Color.Gray,
                        modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                    )
                    Text(
                        text = stringResource(id = R.string.add_operation_button),
                        fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                    )
                }
        }
    }
}

@Preview
@Composable
private fun ReservesScreenPreview(modifier: Modifier = Modifier) {
    ReservesScreen()
}