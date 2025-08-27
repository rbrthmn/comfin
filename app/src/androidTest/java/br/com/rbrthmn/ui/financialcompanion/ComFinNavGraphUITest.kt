
package br.com.rbrthmn.ui.financialcompanion

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.home.ui.HOME_SCREEN_CONTENT_TEST_TAG
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import org.junit.Test

class ComFinNavGraphUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule: ComposeContentTestRule =
        createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    override fun setup() = composeTestRule.setContent {
        navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        ComFinNavGraph(navController = navController)
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule
            .onNodeWithTag(HOME_SCREEN_CONTENT_TEST_TAG)
            .assertIsDisplayed()
    }
}