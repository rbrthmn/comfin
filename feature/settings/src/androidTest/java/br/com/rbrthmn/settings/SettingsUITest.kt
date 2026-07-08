package br.com.rbrthmn.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.ui.BaseUITest
import org.junit.Test

class SettingsUITest : BaseUITest() {
    override val composeTestRule = createComposeRule()

    override fun setup() {
        composeTestRule.setContent {
            SettingsScreen(modifier = Modifier.Companion.fillMaxSize())
        }
    }

    @Test
    fun settings_screen_should_have_a_list_of_settings() {
        val settingsList = composeTestRule.onNodeWithTag(SETTINGS_LIST_TAG)
        val childNodes = settingsList.onChildren()

        childNodes.assertCountEquals(2)
        composeTestRule.onNodeWithTag(SETTINGS_LIST_TAG).assertExists()
    }
}
