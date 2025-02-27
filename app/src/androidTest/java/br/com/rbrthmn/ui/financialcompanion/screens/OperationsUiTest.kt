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

package br.com.rbrthmn.ui.financialcompanion.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.ui.financialcompanion.BaseUITest
import br.com.rbrthmn.ui.financialcompanion.screens.operations.DATE_FILTER_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OPERATIONS_CARD_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.operations.OperationsScreen
import br.com.rbrthmn.ui.financialcompanion.screens.operations.TOTAL_BALANCE_CARD_TAG
import org.junit.Test

class OperationsUiTest : BaseUITest() {
    override val composeTestRule: ComposeContentTestRule = createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { OperationsScreen() }

    @Test
    fun operationsScreen_should_have_date_filter_and_two_cards() {
        composeTestRule.run {
            onNodeWithTag(OPERATIONS_CARD_TAG).assertIsDisplayed()
            onNodeWithTag(DATE_FILTER_TAG).assertIsDisplayed()
            onNodeWithTag(TOTAL_BALANCE_CARD_TAG).assertIsDisplayed()
        }
    }
}