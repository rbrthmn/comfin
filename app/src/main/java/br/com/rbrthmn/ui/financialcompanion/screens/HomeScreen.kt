package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.components.SelectMonthTopBar
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination
import br.com.rbrthmn.ui.financialcompanion.utils.MonthsOfTheYear
import kotlin.math.sin

data class Account(val id: Int, val name: String, val balance: String)

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        SelectMonthTopBar(
            currentMonth = MonthsOfTheYear.January,
            onCurrentMonthClick = { println("CURRENT MONTH") },
            onPreviousMonthClick = { println("PREVIOUS MONTH") },
            onNextMonthClick = { println("NEXT MONTH") },
            scrollBehavior = scrollBehavior,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }, modifier = modifier) { innerPadding ->
        HomeScreenContent(navController, innerPadding, modifier)
    }
}

@Composable
private fun HomeScreenContent(
    navController: NavController,
    innerPaddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val accounts = listOf(
        Account(1, "Banco A", "500,00"),
        Account(2, "Banco B", "1.000.000,00"),
        Account(3, "Banco C", "5,00")
    )
    val totalBalance = "1.000,00"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(innerPaddingValues)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MonthlyLimitCard(
            onCardClick = { navController.navigate(route = IncomeDivisionsDestination.route) },
            monthLimit = "R$ 1.000,00",
            monthDifference = "-R$ 5435,99"
        )
        TotalBalanceCard(totalBalance = totalBalance, accounts = accounts)
        CreditCardsBillCard(totalCreditCardsBill = "R$ 2300,00", cardBills = accounts)
        DifferenceFromLastMonthCard("-5435,99")
    }
}

@Composable
private fun MonthlyLimitCard(
    onCardClick: () -> Unit,
    monthLimit: String,
    monthDifference: String,
    modifier: Modifier = Modifier
) {
    val showMonthlyLimitDialog = remember { mutableStateOf(false) }
    val showMonthlyDifferenceDialog = remember { mutableStateOf(false) }

    if (showMonthlyLimitDialog.value)
        InfoDialog(
            dialogText = stringResource(id = R.string.monthly_limit_dialog_text),
            onCloseButtonClick = { showMonthlyLimitDialog.value = false }
        )

    if (showMonthlyDifferenceDialog.value)
        InfoDialog(
            dialogText = stringResource(id = R.string.monthly_difference_dialog_text),
            onCloseButtonClick = { showMonthlyDifferenceDialog.value = false }
        )


    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
            .clickable { onCardClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.monthly_limit_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = stringResource(id = R.string.help_icon_description),
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_extra_small))
                        .clickable { showMonthlyLimitDialog.value = true }
                )
            }
            Text(
                text = monthLimit,
                fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(
                    bottom = dimensionResource(id = R.dimen.padding_medium),
                    top = dimensionResource(id = R.dimen.padding_extra_small)
                )
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(id = R.string.difference_title),
                    fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = stringResource(id = R.string.help_icon_description),
                    tint = Color.Gray,
                    modifier = modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_extra_small))
                        .clickable { showMonthlyDifferenceDialog.value = true }
                )
            }
            Text(
                text = monthDifference,
                fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_extra_small))
            )
        }
    }
}

@Composable
fun InfoDialog(dialogText: String, onCloseButtonClick: () -> Unit) {
    Dialog(onDismissRequest = onCloseButtonClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = dialogText)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCloseButtonClick) {
                        Text(text = stringResource(id = R.string.close_button))
                    }
                }
            }
        }
    }
}

@Composable
private fun TotalBalanceCard(
    totalBalance: String,
    accounts: List<Account>,
    modifier: Modifier = Modifier
) {
    val showAddBalanceDialog = remember { mutableStateOf(false) }

    if (showAddBalanceDialog.value)
        AddBalanceDialog(
            onCancelButtonClick = { showAddBalanceDialog.value = false },
            onSaveButtonClick = { showAddBalanceDialog.value = false })

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small)),
        onClick = { showAddBalanceDialog.value = true }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            TotalValueText(
                totalValueName = stringResource(id = R.string.total_balance_title),
                totalValue = totalBalance,
                modifier = modifier
            )
            HorizontalDivider()
            ItemsList(items = accounts, modifier = modifier)
            AddItemButton(buttonText = stringResource(id = R.string.add_account_button))
        }
    }
}

@Composable
fun AddBalanceDialog(
    modifier: Modifier = Modifier,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large),
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                )
            ) {
                OutlinedTextField(
                    prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
                    label = { Text(text = stringResource(id = R.string.balance_input_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = "",
                    onValueChange = { },
                    singleLine = true
                )
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.balance_description_input_hint)) },
                    maxLines = 100,
                    value = "",
                    onValueChange = { }
                )
                BanksDropdownMenu(modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(text = stringResource(id = R.string.cancel_button))
                    }
                    Button(onClick = onSaveButtonClick) {
                        Text(text = stringResource(id = R.string.save_button))
                    }
                }
            }
        }
    }
}

@Composable
fun BanksDropdownMenu(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "Banco Inter",
        "Banco Interpanamericano latino das americas",
        "Banco Santander",
        "Banco Bradesco",
        "Banco do Brasil",
        "Banco Itaú",
        "Banco BMG",
        "Banco do Nordeste",
        "Banco do ddasdsad",
        "Banco Inter",
        "Banco Interpanamericano latino das americas",
        "Banco Santander",
        "Banco Bradesco",
        "Banco do Brasil",
        "Banco Itaú",
        "Banco BMG",
        "Banco do Nordeste",
        "Banco do ddasdsad",
    )
    var selectedOptionText by remember { mutableStateOf(options.first()) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedOptionText,
            onValueChange = { selectedOptionText = it },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, "contentDescription")
                }
            },
            singleLine = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                    text = { Text(text = selectionOption) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.help),
                            contentDescription = "Balance Icon"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TotalValueText(
    totalValueName: String,
    totalValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = totalValueName,
            fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = totalValue,
            fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ItemsList(
    modifier: Modifier = Modifier,
    items: List<Account>,
    canEditValue: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        for (item in items) {
            Item(itemName = item.name, itemValue = item.balance, canEditValue = canEditValue)
        }
    }
}

@Composable
fun Item(itemName: String, itemValue: String, modifier: Modifier = Modifier, canEditValue: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.weight(0.6f)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(id = R.string.bank_icon_description),
                tint = Color.Gray,
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
            )
            Text(
                text = itemName,
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
            )
        }
        TextField(
            prefix = { Text(text = stringResource(id = R.string.brl_currency)) },
            value = itemValue,
            onValueChange = { },
            readOnly = !canEditValue,
            singleLine = true,
            modifier = modifier.weight(0.4f)
        )
    }
}

@Composable
fun AddItemButton(buttonText: String, modifier: Modifier = Modifier) {
    TextButton(
        onClick = { },
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_icon_description),
                    tint = Color.Gray,
                    modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small))
                )
                Text(
                    text = buttonText,
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Composable
fun CreditCardsBillCard(
    totalCreditCardsBill: String,
    cardBills: List<Account>,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            TotalValueText(
                totalValueName = stringResource(id = R.string.total_bills_title),
                totalValue = totalCreditCardsBill,
                modifier = modifier
            )
            HorizontalDivider()
            ItemsList(items = cardBills, canEditValue = true, modifier = modifier)
            AddItemButton(buttonText = stringResource(id = R.string.add_card_button), modifier)
        }
    }
}

@Composable
fun DifferenceFromLastMonthCard(differenceValue: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.padding_medium),
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
        ) {
            Text(
                text = stringResource(id = R.string.difference_from_last_month_title),
                fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = modifier.weight(0.6f)
            )
            Row(horizontalArrangement = Arrangement.End, modifier = modifier.weight(0.4f)) {
                Text(
                    text =  "${stringResource(id = R.string.brl_currency)} $differenceValue",
                    fontSize = dimensionResource(id = R.dimen.font_size_medium).value.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    HomeScreen(navController = rememberNavController(), modifier = modifier)
}

@Preview
@Composable
fun MonthlyLimitCardPreview(modifier: Modifier = Modifier) {
    MonthlyLimitCard({}, "323", "323")
}

@Preview
@Composable
fun BalanceCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
    val totalBalance = "1234.00"
    TotalBalanceCard(totalBalance = totalBalance, accounts = accounts, modifier = modifier)
}

@Preview
@Composable
fun CreditCardsBillCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
    val totalCreditCardsBill = "12234.00"
    CreditCardsBillCard(
        totalCreditCardsBill = totalCreditCardsBill,
        cardBills = accounts,
        modifier = modifier
    )
}

@Preview
@Composable
fun DifferenceFromLastMonthCardPreview(modifier: Modifier = Modifier) {
    DifferenceFromLastMonthCard(differenceValue = "-4.00", modifier = modifier)
}

@Preview
@Composable
fun MonthlyLimitDialogPreview() {
    InfoDialog(stringResource(id = R.string.monthly_limit_dialog_text)) { }
}

@Preview
@Composable
fun MonthlyDifferenceDialogPreview() {
    InfoDialog(stringResource(id = R.string.monthly_difference_dialog_text)) {}
}

@Preview
@Composable
fun AddBalanceDialogPreview(modifier: Modifier = Modifier) {
    AddBalanceDialog(onSaveButtonClick = { }, onCancelButtonClick = { })
}