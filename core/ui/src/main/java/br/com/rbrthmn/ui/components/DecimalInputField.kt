package br.com.rbrthmn.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.utils.DecimalInputVisualTransformation

@Composable
fun DecimalInputField(
    modifier: Modifier = Modifier,
    decimalFormatter: DecimalInputFieldFormatter = DecimalFormatter(),
    onValueChange: (String) -> Unit,
    value: String,
    prefix: String = "",
    label: String = "",
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            val cleanedValue = decimalFormatter.cleanup(it)
            onValueChange(cleanedValue)
        },
        prefix = { Text(text = prefix)},
        label = { Text(text = label)},
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        visualTransformation = DecimalInputVisualTransformation(decimalFormatter)
    )
}
