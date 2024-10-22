package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.R

@Composable
fun BalanceCard(
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
        modifier = modifier.shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        ValuesList(
            totalValue = totalBalance,
            totalValueTitle = stringResource(id = R.string.total_balance_title),
            values = accounts,
            onAddItemButtonClick = { showAddBalanceDialog.value = true },
            addItemButtonText = stringResource(id = R.string.add_account_button)
        )
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

@Preview
@Composable
fun BalanceCardPreview(modifier: Modifier = Modifier) {
    val accounts = listOf(Account(1, "Banco A", "500,00"), Account(2, "Banco B", "1.000,00"))
    val totalBalance = "1234.00"
    BalanceCard(
        totalBalance = totalBalance,
        accounts = accounts,
        modifier = modifier
    )
}

@Preview
@Composable
fun AddBalanceDialogPreview(modifier: Modifier = Modifier) {
    AddBalanceDialog(onSaveButtonClick = { }, onCancelButtonClick = { })
}
