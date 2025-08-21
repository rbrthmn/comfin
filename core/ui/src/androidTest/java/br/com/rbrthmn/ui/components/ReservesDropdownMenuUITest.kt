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

package br.com.rbrthmn.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.rbrthmn.ui.BaseUITest
import br.com.rbrthmn.ui.R
import org.junit.Test

class ReservesDropdownMenuTest : BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var selectedReserve: String? = null
    private val testReserves = listOf(
        "Reserva A",
        "Reserva B",
        "Reserva C",
        "Reserva D",
        "Reserva E",
    )

    override fun setup() {
        composeTestRule.setContent {
            ReservesDropdownMenu(
                onReserveClicked = { selectedReserve = it },
                isError = false,
                reserves = testReserves
            )
        }
    }

    @Test
    fun reservesDropdownMenu_displaysCorrectOptions() {
        composeTestRule.onNodeWithContentDescription(
            "${composeTestRule.activity.getString(R.string.reserve_hint)} ${
                composeTestRule.activity.getString(
                    R.string.drop_down_arrow_icon_description
                )
            }"
        ).performClick()

        testReserves.forEach { reserve ->
            composeTestRule.onNodeWithText(reserve).assertIsDisplayed()
        }
    }

    @Test
    fun reservesDropdownMenu_selectOption_callsOnReserveClicked() {
        composeTestRule.onNodeWithContentDescription(
            "${composeTestRule.activity.getString(R.string.reserve_hint)} ${
                composeTestRule.activity.getString(
                    R.string.drop_down_arrow_icon_description
                )
            }"
        ).performClick()

        val optionToSelect = "Reserva C"
        composeTestRule.onNodeWithText(optionToSelect).performClick()

        assert(selectedReserve == optionToSelect)
    }
}
