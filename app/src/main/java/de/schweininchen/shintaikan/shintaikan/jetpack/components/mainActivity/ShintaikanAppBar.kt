package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ShintaikanAppBar(
    appBarTitle: MutableState<String>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
) {
    // val appBarContainerColor = TopAppBarDefaults.centerAlignedTopAppBarColors()
    //    .containerColor(scrollBehavior.state.collapsedFraction)
    val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    val foregroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    // TODO: modifier = Modifier.background(backgroundColors.containerColor(scrollFraction = scrollBehavior.scrollFraction).value)
    Box {
        CenterAlignedTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            title = {
                Text(appBarTitle.value)
            },

            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            },
            actions = { },
        )
    }
}
