package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ShintaikanAppBar(
    appBarTitle: MutableState<String>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior,

    ) {
    Box {
        CenterAlignedTopAppBar(
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
        )
    }
}
