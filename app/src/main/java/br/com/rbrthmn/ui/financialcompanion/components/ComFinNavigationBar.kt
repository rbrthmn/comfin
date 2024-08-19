package br.com.rbrthmn.ui.financialcompanion.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.sharp.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.rbrthmn.R

@Composable
fun ComFinNavigationBar(
    modifier: Modifier = Modifier,
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            icon = Icons.Default.Home,
            text = stringResource(id = R.string.home)
        ),
        NavigationItemContent(
            icon = Icons.Outlined.Menu,
            text = stringResource(id = R.string.operations)
        ),
        NavigationItemContent(
            icon = Icons.Sharp.Lock,
            text = stringResource(id = R.string.dashboard)
        ),
        NavigationItemContent(
            icon = Icons.Filled.MoreVert,
            text = stringResource(id = R.string.more)
        )
    )

    ComFinBottomNavigationBar(
        onTabPressed = { },
        navigationItemContentList = navigationItemContentList,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ComFinBottomNavigationBar(
    onTabPressed: (() -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = false,
                onClick = { onTabPressed() },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ComFinNavigationBarPreview(modifier: Modifier = Modifier) {
    ComFinNavigationBar(
        modifier = Modifier,
    )
}

data class NavigationItemContent(
    val icon: ImageVector,
    val text: String
)
