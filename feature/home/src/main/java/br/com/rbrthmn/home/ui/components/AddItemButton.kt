package br.com.rbrthmn.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.com.rbrthmn.ui.R as uiR

@Composable
fun AddItemButton(buttonText: String, modifier: Modifier = Modifier, onButtonClick: () -> Unit) {
    TextButton(
        onClick = onButtonClick,
        contentPadding = PaddingValues(dimensionResource(id = uiR.dimen.zero_padding)),
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
                    contentDescription = stringResource(id = uiR.string.add_icon_description),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier.padding(horizontal = dimensionResource(id = uiR.dimen.padding_extra_small))
                )
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
