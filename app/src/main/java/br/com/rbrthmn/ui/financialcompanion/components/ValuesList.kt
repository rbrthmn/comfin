package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.utils.valueWithCurrencyString

data class Account(val id: Int, val name: String, val balance: String)

@Composable
fun ValuesList(
    modifier: Modifier = Modifier,
    totalValue: String,
    totalValueTitle: String,
    values: List<Account>,
    onAddItemButtonClick: () -> Unit,
    addItemButtonText: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TotalValueText(
            totalValueTitle = totalValueTitle,
            totalValue = totalValue,
            modifier = modifier
        )
        HorizontalDivider()
        ItemsList(
            items = values,
            canEditValue = true,
            modifier = modifier,
            onButtonClick = onAddItemButtonClick,
            addItemButtonText = addItemButtonText
        )
    }
}

@Composable
private fun TotalValueText(
    totalValueTitle: String,
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
            text = totalValueTitle,
            fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = valueWithCurrencyString(
                currencyStringId = R.string.brl_currency,
                value = totalValue
            ),
            fontSize = dimensionResource(id = R.dimen.font_size_month_limit_value).value.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ItemsList(
    modifier: Modifier = Modifier,
    items: List<Account>,
    canEditValue: Boolean = false,
    addItemButtonText: String,
    onButtonClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
    ) {
        for (item in items) {
            Item(itemName = item.name, itemValue = item.balance, canEditValue = canEditValue)
        }
        AddItemButton(buttonText = addItemButtonText, onButtonClick = onButtonClick)
    }
}

@Composable
private fun Item(
    itemName: String,
    itemValue: String,
    modifier: Modifier = Modifier,
    canEditValue: Boolean
) {
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
private fun AddItemButton(buttonText: String, modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    TextButton(
        onClick = onButtonClick,
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
