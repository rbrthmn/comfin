package br.com.rbrthmn.misc.ui.morefeatures

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.misc.R
import br.com.rbrthmn.navigation.NavigationDestination
import org.koin.androidx.compose.koinViewModel
import br.com.rbrthmn.ui.R as commonR

object MoreFeaturesDestination : NavigationDestination {
    override val route = "more_features"
}

const val FEATURES_LIST_TAG = "features_list"

@Composable
fun MoreFeaturesScreen(
    modifier: Modifier = Modifier,
    viewModel: MoreFeaturesContract.ViewModel = koinViewModel<MoreFeaturesContract.ViewModel>(),
    onFeatureClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    MoreFeaturesScreen(
        modifier = modifier,
        uiState = uiState,
        onFeatureClick = onFeatureClick
    )
}

@Composable
private fun MoreFeaturesScreen(
    modifier: Modifier = Modifier,
    uiState: MoreFeaturesContract.UIState,
    onFeatureClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = commonR.dimen.padding_medium)),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = commonR.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MoreFeaturesCard(uiState.features, onFeatureClick)
    }
}

@Composable
private fun MoreFeaturesCard(features: List<FeatureLabel>, onFeatureClick: (String) -> Unit) {
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
            features.forEachIndexed { index, feature ->
                TextButton(
                    onClick = { onFeatureClick(feature.route) },
                    contentPadding = PaddingValues(dimensionResource(id = commonR.dimen.zero_padding)),
                    modifier = Modifier.testTag(feature.route)
                ) {
                    Text(
                        text = stringResource(id = feature.nameResId),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (index < features.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreFeaturesScreenPreview() {
    val features = listOf(
        FeatureLabel(R.string.feature_label_reserves, "reserves"),
        FeatureLabel(R.string.feature_label_recurring_expenses, "recurring_expenses"),
        FeatureLabel(R.string.feature_label_income_distribution, "income_divisions"),
        FeatureLabel(R.string.feature_label_settings, "settings")
    )
    MoreFeaturesScreen(
        uiState = MoreFeaturesContract.UIState(features = features),
        onFeatureClick = {}
    )
}
