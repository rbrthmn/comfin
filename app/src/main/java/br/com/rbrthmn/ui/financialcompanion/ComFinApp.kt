package br.com.rbrthmn.ui.financialcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.rbrthmn.R
import br.com.rbrthmn.auth.passwordrecovery.PasswordRecoveryDestination
import br.com.rbrthmn.auth.signin.SignInDestination
import br.com.rbrthmn.auth.signup.SignUpDestination
import br.com.rbrthmn.data.auth.repository.AuthRepository
import br.com.rbrthmn.home.ui.HomeDestination
import br.com.rbrthmn.misc.morefeatures.MoreFeaturesDestination
import br.com.rbrthmn.navigation.ComFinNavigationBar
import br.com.rbrthmn.navigation.ComFinNavigationType
import br.com.rbrthmn.navigation.NavigationItemContent
import br.com.rbrthmn.operations.ui.OperationsDestination
import br.com.rbrthmn.ui.financialcompanion.navigation.ComFinNavGraph
import org.koin.compose.koinInject

const val NAV_GRAPH_TAG = "nav_graph"
const val NAV_BAR_TAG = "nav_bar"

private val authRoutes = setOf(
    SignInDestination.route,
    SignUpDestination.route,
    PasswordRecoveryDestination.route
)

@Composable
fun ComFinApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val authRepository = koinInject<AuthRepository>()
    val startDestination = remember {
        if (authRepository.isAuthenticated()) HomeDestination.route else SignInDestination.route
    }
    val navigationType: ComFinNavigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> ComFinNavigationType.BOTTOM_NAVIGATION
        else -> ComFinNavigationType.BOTTOM_NAVIGATION
    }
    val currentDestination by navController.currentBackStackEntryAsState()
    val navItemsList = listOf(
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.home),
            route = HomeDestination.route
        ),
        NavigationItemContent(
            icon = Icons.Outlined.Menu,
            text = stringResource(id = R.string.operations),
            route = OperationsDestination.route
        ),
        NavigationItemContent(
            icon = Icons.Filled.MoreVert,
            text = stringResource(id = R.string.more),
            route = MoreFeaturesDestination.route
        )
    )

    val currentRoute = currentDestination?.destination?.route ?: startDestination
    val isAuthRoute = currentRoute in authRoutes

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ComFinNavGraph(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .weight(0.92f)
                    .testTag(NAV_GRAPH_TAG)
            )
            if (!isAuthRoute) {
                ComFinNavigationBar(
                    modifier = Modifier
                        .weight(0.08f)
                        .testTag(NAV_BAR_TAG),
                    navigationItems = navItemsList,
                    navigationType = navigationType,
                    navigateToDestination = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    currentRoute = currentRoute
                )
            }
        }
    }
}
