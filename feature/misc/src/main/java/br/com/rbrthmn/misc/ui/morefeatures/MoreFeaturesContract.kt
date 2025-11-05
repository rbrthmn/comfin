package br.com.rbrthmn.misc.ui.morefeatures

import androidx.annotation.StringRes
import br.com.rbrthmn.ui.BaseViewModel

data class FeatureLabel(@StringRes val nameResId: Int, val route: String)

abstract class MoreFeaturesContract {

    abstract class ViewModel : BaseViewModel<UIState, Intent>()

    data class UIState(
        val features: List<FeatureLabel> = emptyList()
    )

    sealed class Intent
}
