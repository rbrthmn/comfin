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

package br.com.rbrthmn.ui.financialcompanion

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Test

class ComFinAppUITest : BaseUITest() {
    override val composeTestRule: ComposeContentTestRule = createComposeRule()

    override fun setup() {
        composeTestRule.setContent {
            ComFinApp(windowSize = WindowWidthSizeClass.Compact)
        }
    }

    @Test
    fun comFinApp_should_have_navGraph_and_navBar() {
        val navGraph = composeTestRule.onNodeWithTag(NAV_GRAPH_TAG)
        val navBAR = composeTestRule.onNodeWithTag(NAV_BAR_TAG)

        navGraph.assertExists()
        navBAR.assertExists()
    }
}