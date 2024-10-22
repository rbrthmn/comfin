package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import br.com.rbrthmn.R


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
