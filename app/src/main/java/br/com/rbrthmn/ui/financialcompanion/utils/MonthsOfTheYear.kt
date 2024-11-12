package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.annotation.StringRes
import br.com.rbrthmn.R

enum class MonthsOfTheYear(
    @StringRes val shortStringId: Int,
    @StringRes val longStringId: Int
) {
    JANUARY(shortStringId = R.string.january_short, longStringId = R.string.january),
    FEBRUARY(shortStringId = R.string.february_short, longStringId = R.string.february),
    MARCH(shortStringId = R.string.march_short, longStringId = R.string.march),
    APRIL(shortStringId = R.string.april_short, longStringId = R.string.april),
    MAY(shortStringId = R.string.may_short, longStringId = R.string.may),
    JUNE(shortStringId = R.string.june_short, longStringId = R.string.june),
    JULY(shortStringId = R.string.july_short, longStringId = R.string.july),
    AUGUST(shortStringId = R.string.august_short, longStringId = R.string.august),
    SEPTEMBER(shortStringId = R.string.september_short, longStringId = R.string.september),
    OCTOBER(shortStringId = R.string.october_short, longStringId = R.string.october),
    NOVEMBER(shortStringId = R.string.november_short, longStringId = R.string.november),
    DECEMBER(shortStringId = R.string.december_short, longStringId = R.string.december)
}
