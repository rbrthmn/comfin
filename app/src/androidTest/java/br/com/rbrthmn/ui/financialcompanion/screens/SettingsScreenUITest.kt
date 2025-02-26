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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.ui.financialcompanion.BaseUITest
import br.com.rbrthmn.ui.financialcompanion.screens.settings.SETTINGS_LIST_TAG
import br.com.rbrthmn.ui.financialcompanion.screens.settings.SettingsScreen
import org.junit.Test

class SettingsScreenUITest : BaseUITest() {
    override val composeTestRule = createComposeRule()

    override fun setup() {
        composeTestRule.setContent {
            SettingsScreen(modifier = Modifier.fillMaxSize())
        }
    }

    @Test
    fun settings_screen_should_have_a_list_of_settings() {
        val settingsList = composeTestRule.onNodeWithTag(SETTINGS_LIST_TAG)
        val childNodes = settingsList.onChildren()

        childNodes.assertCountEquals(1)
        composeTestRule.onNodeWithTag(SETTINGS_LIST_TAG).assertExists()
    }
}