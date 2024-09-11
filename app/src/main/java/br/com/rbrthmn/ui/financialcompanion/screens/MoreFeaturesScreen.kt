package br.com.rbrthmn.ui.financialcompanion.screens

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.R
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination

data class FeatureLabel(val name: String, val route: String)

object MoreFeaturesDestination : NavigationDestination {
    override val route = "more_features"
}

@Composable
fun MoreFeaturesScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MoreFeaturesCard(navController)
    }
}
@Composable
private fun MoreFeaturesCard(navController: NavController) {
    val featuresList = listOf(
        FeatureLabel(stringResource(id = R.string.feature_label_reserves), "reservas"),
        FeatureLabel(stringResource(id = R.string.feature_label_recurring_expenses), "gastos_recorrentes"),
        FeatureLabel(stringResource(id = R.string.feature_label_income_distribution), IncomeDivisionsDestination.route),
        FeatureLabel(stringResource(id = R.string.feature_label_settings), "configurations")
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.padding_medium))
            .shadow(elevation = dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
        ) {
            for (index in featuresList.indices) {
                TextButton(
                    onClick = { navController.navigate(featuresList[index].route) },
                    contentPadding = PaddingValues(dimensionResource(id = R.dimen.zero_padding)),
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
fun MoreFeaturesScreenPreview(modifier: Modifier = Modifier) {
    MoreFeaturesScreen(navController = rememberNavController())
}