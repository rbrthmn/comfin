/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rbrthmn.operations.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import br.com.rbrthmn.operations.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val DAY_IN_MILLISECONDS = 86400000
const val CALENDAR_TEST_TAG = "dialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    onDateSelected: (LocalDate) -> Unit,
    isError: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate
            .now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun onDismiss() {
        showDatePicker = false
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate.format(formatter),
            onValueChange = { },
            label = { Text(stringResource(id = R.string.date_picker_label)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(id = R.string.date_picker_title)
                    )
                }
            },
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.date_picker_field_height))
        )

        if (showDatePicker) {
            DatePickerDialog(
                modifier = Modifier.testTag(CALENDAR_TEST_TAG),
                onDismissRequest = { onDismiss() },
                confirmButton = {
                    TextButton(onClick = {
                        val localDate = datePickerState.selectedDateMillis?.let {
                            convertMillisToLocalDate(it + DAY_IN_MILLISECONDS)
                        } ?: LocalDate.now()
                        onDateSelected(localDate)
                        selectedDate = localDate ?: LocalDate.now()
                        onDismiss()
                    }) {
                        Text(stringResource(id = R.string.date_picker_ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onDismiss() }) {
                        Text(stringResource(id = R.string.date_picker_cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

private fun convertMillisToLocalDate(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}