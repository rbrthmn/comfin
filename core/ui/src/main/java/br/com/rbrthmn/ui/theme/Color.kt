package br.com.rbrthmn.ui.theme

import androidx.compose.ui.graphics.Color

// ComFin brand identity — deep indigo-blue primary (trust, stability), warm amber tertiary
// (calls to action, highlights), muted slate secondary. Semantic income/expense/warning
// colors are kept separate from the M3 role palette so financial meaning stays sparse and
// deliberate rather than decorative.

// Primary — Indigo Blue
val PrimaryLight = Color(0xFF2E5AAC)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD8E2FF)
val OnPrimaryContainerLight = Color(0xFF001A41)

val PrimaryDark = Color(0xFFAEC6FF)
val OnPrimaryDark = Color(0xFF002E6D)
val PrimaryContainerDark = Color(0xFF00458F)
val OnPrimaryContainerDark = Color(0xFFD8E2FF)

// Secondary — Slate
val SecondaryLight = Color(0xFF55606F)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFDAE2F2)
val OnSecondaryContainerLight = Color(0xFF121C29)

val SecondaryDark = Color(0xFFBCC7D9)
val OnSecondaryDark = Color(0xFF26323F)
val SecondaryContainerDark = Color(0xFF3C4857)
val OnSecondaryContainerDark = Color(0xFFDAE2F2)

// Tertiary — Amber (CTAs, highlights)
val TertiaryLight = Color(0xFF8A5D00)
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFFDEA1)
val OnTertiaryContainerLight = Color(0xFF2A1800)

val TertiaryDark = Color(0xFFF0BD5E)
val OnTertiaryDark = Color(0xFF452B00)
val TertiaryContainerDark = Color(0xFF654200)
val OnTertiaryContainerDark = Color(0xFFFFDEA1)

// Error
val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

// Neutral surfaces
val BackgroundLight = Color(0xFFFAF8FF)
val OnBackgroundLight = Color(0xFF1A1B21)
val SurfaceLight = Color(0xFFFAF8FF)
val OnSurfaceLight = Color(0xFF1A1B21)
val SurfaceVariantLight = Color(0xFFE1E2EC)
val OnSurfaceVariantLight = Color(0xFF44474F)
val OutlineLight = Color(0xFF74777F)
val OutlineVariantLight = Color(0xFFC4C6D0)
val InverseSurfaceLight = Color(0xFF2F3036)
val InverseOnSurfaceLight = Color(0xFFF1F0F7)
val InversePrimaryLight = Color(0xFFAEC6FF)

val BackgroundDark = Color(0xFF121318)
val OnBackgroundDark = Color(0xFFE3E2E9)
val SurfaceDark = Color(0xFF121318)
val OnSurfaceDark = Color(0xFFE3E2E9)
val SurfaceVariantDark = Color(0xFF44474F)
val OnSurfaceVariantDark = Color(0xFFC4C6D0)
val OutlineDark = Color(0xFF8E9099)
val OutlineVariantDark = Color(0xFF44474F)
val InverseSurfaceDark = Color(0xFFE3E2E9)
val InverseOnSurfaceDark = Color(0xFF2F3036)
val InversePrimaryDark = Color(0xFF2E5AAC)

val ScrimColor = Color(0xFF000000)

// Semantic finance colors — used only to signal income/expense/warning, never as decoration.
val IncomeLight = Color(0xFF1E8E5A)
val IncomeDark = Color(0xFF7DDBA8)
val ExpenseLight = Color(0xFFC1392B)
val ExpenseDark = Color(0xFFFFB4A8)
val WarningLight = Color(0xFFB25E00)
val WarningDark = Color(0xFFFFB870)