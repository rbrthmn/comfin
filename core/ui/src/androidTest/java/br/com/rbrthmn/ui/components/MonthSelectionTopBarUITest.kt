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

package br.com.rbrthmn.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.ui.R
import org.junit.Test
import java.time.LocalDate

class MonthSelectionTopBarTest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var selectedDate: LocalDate? = null

    override fun setup() = composeTestRule.setContent {
        MonthSelectionTopBar(
            initialDate = LocalDate.now(),
            onDateSelected = { selectedDate = it }
        )
    }

    @Test
    fun monthSelectionTopBar_opensDialog_whenClicked() {
        composeTestRule.onNodeWithText(
            LocalDate.now().month.getDisplayName(
                java.time.format.TextStyle.SHORT,
                java.util.Locale.getDefault()
            ).uppercase().removeSuffix(".")
        ).performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.current_month_button))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.apply_date_button))
            .assertIsDisplayed()
    }

    @Test
    fun monthSelectionDialog_dismisses_whenCloseIconClicked() {
        composeTestRule.onNodeWithText(
            LocalDate.now().month.getDisplayName(
                java.time.format.TextStyle.SHORT,
                java.util.Locale.getDefault()
            ).uppercase().removeSuffix(".")
        ).performClick()

        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.close_icon_description))
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.current_month_button))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.apply_date_button))
            .assertDoesNotExist()
    }

    @Test
    fun monthSelectionDialog_selectsCurrentMonth_whenCurrentMonthButtonClicked() {
        composeTestRule.onNodeWithText(
            LocalDate.now().month.getDisplayName(
                java.time.format.TextStyle.SHORT,
                java.util.Locale.getDefault()
            ).uppercase().removeSuffix(".")
        ).performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.current_month_button))
            .performClick()

        assert(selectedDate == LocalDate.now())
    }
}