/*
 *
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
 *
 */

package br.com.rbrthmn.ui.financialcompanion.viewmodels

import br.com.rbrthmn.ui.financialcompanion.utils.DecimalFormatter
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.text.DecimalFormatSymbols
import java.util.Locale

class DecimalFormatterTest {
    private var formatter = DecimalFormatter()

    @Test
    fun cleanup_should_return_empty_string_when_input_is_empty() {
        val actual = formatter.cleanup(EMPTY_STRING)

        assertEquals(EMPTY_STRING, actual)
    }

    @Test
    fun cleanup_with_non_numeric_string_should_return_empty_string() {
        val actual = formatter.cleanup(INVALID_STRING)

        assertEquals(EMPTY_STRING, actual)
    }

    @Test
    fun cleanup_with_more_than_one_zero_should_return_one_zero() {
        val actual = formatter.cleanup(MANY_ZEROS)

        assertEquals(ZERO_STRING, actual)
    }

    @Test
    fun cleanup_with_valid_numeric_should_return_numeric_string() {
        val actual = formatter.cleanup(NUMERIC_STRING)

        assertEquals(NUMERIC_STRING, actual)
    }

    @Test
    fun cleanup_with_decimal_separator_should_return_numeric_string_with_single_decimal_separator() {
        val actual = formatter.cleanup(INPUT_WITH_DECIMAL_SEPARATOR)

        assertEquals(EXPECTED_WITH_DECIMAL_SEPARATOR, actual)
    }

    @Test
    fun cleanup_with_leading_decimal_separator_should_return_numeric_string_without_leading_decimal_separator() {
        val actual = formatter.cleanup(INPUT_WITH_LEADING_DECIMAL_SEPARATOR)

        assertEquals(NUMERIC_STRING, actual)
    }

    @Test
    fun cleanup_with_multiple_decimal_separators_should_return_numeric_string_with_single_decimal_separator() {
        val actual = formatter.cleanup(INPUT_WITH_MULTIPLE_DECIMAL_SEPARATORS)

        assertEquals(EXPECTED_WITH_DECIMAL_SEPARATOR, actual)
    }

    @Test
    fun cleanup_with_non_numeric_and_decimal_separators_should_return_numeric_string_with_single_decimal_separator() {
        val actual = formatter.cleanup(INPUT_WITH_NON_NUMERIC_AND_DECIMAL_SEPARATORS)

        assertEquals(EXPECTED_WITH_DECIMAL_SEPARATOR, actual)
    }

    @Test
    fun cleanup_with_br_locale_and_decimal_value_should_return_correct_string() {
        formatter = DecimalFormatter(DecimalFormatSymbols(Locale("pt", "BR")))

        val actual = formatter.cleanup(BR_DECIMAL_VALUE)

        assertEquals(BR_DECIMAL_VALUE, actual)
    }

    @Test
    fun cleanup_with_us_locale_and_decimal_value_should_return_correct_string() {
        formatter = DecimalFormatter(DecimalFormatSymbols(Locale.US))

        val actual = formatter.cleanup(US_DECIMAL_VALUE)

        assertEquals(US_DECIMAL_VALUE, actual)
    }

    private companion object {
        const val INVALID_STRING = "abc"
        const val EMPTY_STRING = ""
        const val MANY_ZEROS = "0000"
        const val ZERO_STRING = "0"
        const val NUMERIC_STRING = "123456"
        private const val INPUT_WITH_DECIMAL_SEPARATOR = "12.34.56"
        private const val EXPECTED_WITH_DECIMAL_SEPARATOR = "12.3456"
        private const val INPUT_WITH_LEADING_DECIMAL_SEPARATOR = ".123456"
        private const val INPUT_WITH_MULTIPLE_DECIMAL_SEPARATORS = "12..34..56"
        private const val INPUT_WITH_NON_NUMERIC_AND_DECIMAL_SEPARATORS = "a12.b34.c56"
        const val BR_DECIMAL_VALUE = "123,45"
        const val US_DECIMAL_VALUE = "123.45"
    }
}