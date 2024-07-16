package br.com.rbrthmn.ui.financialcompanion.utils

import androidx.annotation.StringRes
import br.com.rbrthmn.R

enum class MonthsOfTheYear(@StringRes val stringId: Int) {
    January(stringId = R.string.january),
    February(stringId = R.string.february),
    March(stringId = R.string.march),
    April(stringId = R.string.april),
    May(stringId = R.string.may),
    June(stringId = R.string.june),
    July(stringId = R.string.july),
    August(stringId = R.string.august),
    September(stringId = R.string.september),
    October(stringId = R.string.october),
    November(stringId = R.string.november),
    December(stringId = R.string.december)
}

fun MonthsOfTheYear.previousMonth(): MonthsOfTheYear {
    val ordinal = this.ordinal
    return if (ordinal == 0) {
        MonthsOfTheYear.December
    } else {
        MonthsOfTheYear.entries[ordinal - 1]
    }
}

fun MonthsOfTheYear.nextMonth(): MonthsOfTheYear {
    val ordinal = this.ordinal
    return if (ordinal == MonthsOfTheYear.entries.size - 1) {
        MonthsOfTheYear.January
    } else {
        MonthsOfTheYear.entries[ordinal + 1]
    }
}
