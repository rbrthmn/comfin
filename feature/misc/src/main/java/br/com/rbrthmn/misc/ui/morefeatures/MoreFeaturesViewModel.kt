package br.com.rbrthmn.misc.ui.morefeatures

import br.com.rbrthmn.misc.R
import br.com.rbrthmn.misc.ui.incomedivisions.IncomeDivisionsDestination
import br.com.rbrthmn.misc.ui.recurringexpenses.RecurringExpensesDestination
import br.com.rbrthmn.misc.ui.reserves.ReservesDestination
import br.com.rbrthmn.settings.ui.SettingsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MoreFeaturesViewModel : MoreFeaturesContract.ViewModel() {

    override val uiState = MutableStateFlow(MoreFeaturesContract.UIState())

    override fun doOnInit(): MoreFeaturesContract.ViewModel {
        uiState.update {
            it.copy(
                features = listOf(
                    FeatureLabel(
                        nameResId = R.string.feature_label_reserves,
                        route = ReservesDestination.route
                    ),
                    FeatureLabel(
                        nameResId = R.string.feature_label_recurring_expenses,
                        route = RecurringExpensesDestination.route
                    ),
                    FeatureLabel(
                        nameResId = R.string.feature_label_income_distribution,
                        route = IncomeDivisionsDestination.route
                    ),
                    FeatureLabel(
                        nameResId = R.string.feature_label_settings,
                        route = SettingsDestination.route
                    )
                )
            )
        }
        return this
    }

    override fun onIntent(intent: MoreFeaturesContract.Intent) {}
}
