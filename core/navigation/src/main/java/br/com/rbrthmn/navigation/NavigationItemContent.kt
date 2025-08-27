package br.com.rbrthmn.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItemContent(
    val icon: ImageVector,
    val text: String,
    val route: String
)
