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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.rbrthmn.ui.financialcompanion.navigation.NavigationDestination

data class FeatureLabel(val name: String, val route: String)

object MoreFeaturesDestination : NavigationDestination {
    override val route = "more_features"
}

@Composable
fun MoreFeaturesScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MoreFeaturesCard()
    }
}
@Composable
fun MoreFeaturesCard(modifier: Modifier = Modifier) {
    val featuresList = listOf<FeatureLabel>(
        FeatureLabel("Reservas", "reservas"),
        FeatureLabel("Gastos Recorrentes", "gastos_recorrentes"),
        FeatureLabel("Rendas e Distribuições", "distribution"),
        FeatureLabel("Configurações", "configurations")
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .shadow(elevation = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            for (index in featuresList.indices) {
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(0.dp),
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
    MoreFeaturesScreen()
}