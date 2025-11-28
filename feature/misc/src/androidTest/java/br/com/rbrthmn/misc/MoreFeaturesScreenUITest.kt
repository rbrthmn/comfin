package br.com.rbrthmn.misc

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import br.com.rbrthmn.misc.ui.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.misc.ui.morefeatures.FEATURES_LIST_TAG
import br.com.rbrthmn.misc.ui.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.misc.ui.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.settings.ui.SettingsDestination
import org.junit.Assert.assertEquals
import org.junit.Test

class MoreFeaturesScreenUITest : br.com.rbrthmn.ui.BaseUITest() {
    override val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    override fun setup() = composeTestRule.setContent {
        navController = TestNavHostController(LocalContext.current)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        navController.navigate(MoreFeaturesDestination.route)
    }

    @Test
    fun moreFeaturesList_hasCorrectLabels() {
        composeTestRule.run {
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_reserves))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_recurring_expenses))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_income_distribution))
                .assertExists()
            onNodeWithText(composeTestRule.activity.getString(R.string.feature_label_settings))
                .assertExists()
        }
    }

    @Test
    fun more_features_list_should_have_correct_number_of_options() {
        val expectedNumberOfOptions = 4
        val featuresList = composeTestRule.onNodeWithTag(FEATURES_LIST_TAG).onChildren()

        featuresList.assertCountEquals(expectedNumberOfOptions)
    }

    @Test
    fun on_income_divisions_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(IncomeDivisionsDestination.route).performClick()

        assertEquals(
            IncomeDivisionsDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun on_settings_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(SettingsDestination.route).performClick()

        assertEquals(
            SettingsDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun on_reserves_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(ReservesDestination.route).performClick()

        assertEquals(
            ReservesDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }

    @Test
    fun on_recurring_expenses_feature_click_should_navigate_to_its_route() {
        composeTestRule.onNodeWithTag(RecurringExpensesDestination.route).performClick()

        assertEquals(
            RecurringExpensesDestination.route,
            navController.currentBackStackEntry?.destination?.route
        )
    }
}