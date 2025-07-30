package br.com.rbrthmn.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import br.com.rbrthmn.home.di.homeModule
import br.com.rbrthmn.ui.BaseUITest
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

class HomeScreenUITest : BaseUITest() {
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

        @JvmStatic
        @BeforeClass
        fun setupKoin() {
            startKoin {
                modules(homeModule)
            }
        }

        @JvmStatic
        @AfterClass
        fun tearDownKoin() {
            stopKoin()
        }
    }
}