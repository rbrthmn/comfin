package br.com.rbrthmn.home.ui.components.creditcardbillscard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import br.com.rbrthmn.home.R
import br.com.rbrthmn.home.ui.components.AddItemButton
import br.com.rbrthmn.home.ui.components.BanksDropdownMenu
import br.com.rbrthmn.home.ui.components.TotalValueText
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract.Intent
import br.com.rbrthmn.ui.components.DecimalInputField
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import br.com.rbrthmn.home.ui.components.creditcardbillscard.CreditCardBillsCardContract as Contract
import br.com.rbrthmn.ui.R as commonR

const val ADD_CARD_DIALOG_TAG = "add_card_dialog"
const val BILL_CLOSE_DAY_DROPDOWN_MENU_TAG = "bill_close_day_dropdown_menu"

@Composable
fun CreditCardBillsCard(
    modifier: Modifier = Modifier,
    viewModel: Contract.ViewModel = koinViewModel(),
    currentDateFilter: LocalDate
) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddCardDialog = rememberSaveable { mutableStateOf(false) }
    viewModel.onIntent(Intent.OnDateFilterChange(currentDateFilter))

    if (showAddCardDialog.value)
        AddCreditCardDialog(
            viewModel = viewModel,
            onCancelButtonClick = {
                viewModel.onIntent(Intent.CleanInputs)
                showAddCardDialog.value = false
            },
            onSaveButtonClick = { viewModel.onIntent(Intent.OnSaveClick(showAddCardDialog)) })

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
    ) {
        CreditCardBillsList(
            totalBill = uiState.totalBill,
            creditCards = uiState.bills,
            onAddItemButtonClick = { showAddCardDialog.value = true },
        )
    }
}

@Composable
private fun CreditCardBillsList(
    modifier: Modifier = Modifier,
    totalBill: String,
    creditCards: List<Contract.CreditCardBillUiState>,
    onAddItemButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(dimensionResource(id = commonR.dimen.padding_medium))
    ) {
        TotalValueText(
            totalValueTitle = stringResource(id = R.string.total_bills_title),
            totalValue = totalBill,
            modifier = modifier
        )
        HorizontalDivider()
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_small)),
            modifier = modifier.padding(top = dimensionResource(id = commonR.dimen.padding_small))
        ) {
            for (card in creditCards) {
                CreditCardItem(
                    itemName = card.name,
                    itemValue = card.value,
                    canEditValue = card.canValueBeEdited,
                    dueDay = card.dueDay.toString()
                )
            }
            AddItemButton(
                buttonText = stringResource(id = R.string.add_card_button),
                onButtonClick = onAddItemButtonClick
            )
        }
    }
}

@Composable
private fun CreditCardItem(
    modifier: Modifier = Modifier,
    itemName: String,
    itemValue: String,
    canEditValue: Boolean,
    dueDay: String
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier.padding(horizontal = dimensionResource(id = commonR.dimen.padding_extra_small))
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.credit_card_due_day_label) + " " + dueDay,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        TextField(
            prefix = { Text(text = stringResource(id = commonR.string.brl_currency)) },
            value = itemValue,
            onValueChange = { },
            readOnly = !canEditValue,
            singleLine = true,
            modifier = modifier.weight(0.4f)
        )
    }
}


@Composable
private fun AddCreditCardDialog(
    modifier: Modifier = Modifier,
    viewModel: Contract.ViewModel,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onCancelButtonClick) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag(ADD_CARD_DIALOG_TAG)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.padding(
                    horizontal = dimensionResource(id = commonR.dimen.padding_large),
                    vertical = dimensionResource(id = commonR.dimen.padding_medium)
                )
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.balance_name_input_hint)) },
                    maxLines = 100,
                    value = uiState.newCreditCardName,
                    onValueChange = {
                        viewModel.onIntent(Intent.OnNewCreditCardNameChange(name = it))
                    },
                    isError = !uiState.isNewCreditCardNameValid,
                )
                DecimalInputField(
                    onValueChange = { viewModel.onIntent(Intent.OnNewCreditCardBillValueChange(bill = it)) },
                    value = uiState.newCreditCardBill,
                    label = stringResource(id = R.string.card_bill_input_hint),
                    prefix = stringResource(id = commonR.string.brl_currency),
                    isError = !uiState.isNewCreditCardBillValid
                )
                BanksDropdownMenu(
                    onBankSelected = { icon, name ->
                        viewModel.onIntent(Intent.OnBankChange(bankIcon = icon, bankName = name))
                    },
                    isValid = uiState.isNewCreditCardBankNameValid,
                    modifier = modifier.padding(vertical = dimensionResource(id = commonR.dimen.padding_small))
                )
                CardBillCloseDayDropdownMenu(
                    onDayClicked = { viewModel.onIntent(Intent.OnNewCreditCardBillDueDayChange(day = it)) },
                    isError = !uiState.isNewCreditCardBillDueDayValid
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = commonR.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    TextButton(onClick = onCancelButtonClick) {
                        Text(text = stringResource(id = commonR.string.cancel_button))
                    }
                    Button(onClick = onSaveButtonClick) {
                        Text(text = stringResource(id = commonR.string.save_button))
                    }
                }
            }
        }
    }
}

@Composable
private fun CardBillCloseDayDropdownMenu(onDayClicked: (day: Int) -> Unit, isError: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText: String? by remember { mutableStateOf(null) }
    val options = List(31) { (it + 1) }

    OutlinedTextField(
        value = selectedOptionText ?: stringResource(id = commonR.string.blank),
        label = { Text(text = stringResource(id = R.string.card_bill_close_day_hint)) },
        onValueChange = { selectedOptionText = it },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "${stringResource(id = R.string.card_bill_close_day_hint)} ${stringResource(R.string.drop_down_arrow_icon_description)}"
                )
            }
        },
        singleLine = true,
        isError = isError
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.testTag(BILL_CLOSE_DAY_DROPDOWN_MENU_TAG)
    ) {
        options.forEach { selectionOption ->
            DropdownMenuItem(
                onClick = {
                    selectedOptionText = selectionOption.toString()
                    onDayClicked(selectionOption)
                    expanded = false
                },
                text = { Text(text = selectionOption.toString()) }
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun CreditCardsBillCardPreview() {
    CreditCardBillsCard(
        viewModel = CreditCardBillsCardViewModel().doOnInit(),
        modifier = Modifier,
        currentDateFilter = LocalDate.now()
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun AddCardBillDialogPreview() {
    AddCreditCardDialog(
        onSaveButtonClick = { },
        onCancelButtonClick = {},
        viewModel = CreditCardBillsCardViewModel().doOnInit()
    )
}
