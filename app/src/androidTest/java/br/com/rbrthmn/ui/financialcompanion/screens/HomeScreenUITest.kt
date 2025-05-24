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
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.ui.financialcompanion.screens.home.HOME_SCREEN_CONTENT_TEST_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.home.HomeScreen
import org.junit.Test

class HomeScreenUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule: ComposeContentTestRule =
        createAndroidComposeRule<ComponentActivity>()

    override fun setup() = composeTestRule.setContent { HomeScreen(onMonthlyLimitCardClick = {}) }

    @Test
    fun homeScreen_should_haveFourCards() {
        val cards = composeTestRule.onNodeWithTag(HOME_SCREEN_CONTENT_TEST_TAG).onChildren()

        cards.assertCountEquals(EXPECTED_HOME_CARDS_COUNT)
    }

    private companion object {
        const val EXPECTED_HOME_CARDS_COUNT = 4
    }
}