/*
 *
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications made by Roberto Kenzo Hamano, 2024
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
