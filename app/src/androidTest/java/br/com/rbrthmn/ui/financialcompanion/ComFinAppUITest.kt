package br.com.rbrthmn.ui.financialcompanion

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.rbrthmn.ui.BaseUITest
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

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithText(activity.getString(id))