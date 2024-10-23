package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.annotation.StringRes
import br.com.rbrthmn.R

enum class MonthsOfTheYear(@StringRes val stringId: Int) {
    JANUARY(stringId = R.string.january),
    FEBRUARY(stringId = R.string.february),
    MARCH(stringId = R.string.march),
    APRIL(stringId = R.string.april),
    MAY(stringId = R.string.may),
    JUNE(stringId = R.string.june),
    JULY(stringId = R.string.july),
    AUGUST(stringId = R.string.august),
    SEPTEMBER(stringId = R.string.september),
    OCTOBER(stringId = R.string.october),
    NOVEMBER(stringId = R.string.november),
    DECEMBER(stringId = R.string.december)
}

fun MonthsOfTheYear.previousMonth(): MonthsOfTheYear {
    val ordinal = this.ordinal
    return if (ordinal == 0) {
        MonthsOfTheYear.DECEMBER
    } else {
        MonthsOfTheYear.entries[ordinal - 1]
    }
}

fun MonthsOfTheYear.nextMonth(): MonthsOfTheYear {
    val ordinal = this.ordinal
    return if (ordinal == MonthsOfTheYear.entries.size - 1) {
        MonthsOfTheYear.JANUARY
    } else {
        MonthsOfTheYear.entries[ordinal + 1]
    }
}
