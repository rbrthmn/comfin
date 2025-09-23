package br.com.rbrthmn.misc.morefeatures

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.misc.R
import br.com.rbrthmn.misc.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.misc.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.misc.reserves.ReservesDestination
import br.com.rbrthmn.navigation.NavigationDestination
import br.com.rbrthmn.settings.SettingsDestination
import br.com.rbrthmn.ui.R as commonR

data class FeatureLabel(val name: String, val route: String)

object MoreFeaturesDestination : NavigationDestination {
    override val route = "more_features"
}

const val FEATURES_LIST_TAG = "features_list"

@Composable
fun MoreFeaturesScreen(onFeatureClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MoreFeaturesCard(onFeatureClick)
    }
}

@Composable
private fun MoreFeaturesCard(onFeatureClick: (String) -> Unit) {
    val featuresList = listOf(
        FeatureLabel(
            name = stringResource(id = R.string.feature_label_reserves),
            route = ReservesDestination.route
        ),
        FeatureLabel(
            name = stringResource(id = R.string.feature_label_recurring_expenses),
            route = RecurringExpensesDestination.route
        ),
        FeatureLabel(
            name = stringResource(id = R.string.feature_label_income_distribution),
            route = IncomeDivisionsDestination.route
        ),
        FeatureLabel(
            name = stringResource(id = R.string.feature_label_settings),
            route = SettingsDestination.route
        )
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = commonR.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = commonR.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
                .testTag(FEATURES_LIST_TAG)
        ) {
            for (index in featuresList.indices) {
                TextButton(
                    onClick = { onFeatureClick(featuresList[index].route) },
                    contentPadding = PaddingValues(dimensionResource(id = commonR.dimen.zero_padding)),
                    modifier = Modifier.testTag(featuresList[index].route)
                ) {
                    Text(text = featuresList[index].name, modifier = Modifier.fillMaxWidth())
                }
                if (index < featuresList.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreFeaturesScreenPreview() {
    MoreFeaturesScreen(onFeatureClick = {})
}