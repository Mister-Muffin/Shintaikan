package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ShintaikanAppBar(
    appBarTitle: String,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val appBarContainerColor by TopAppBarDefaults.centerAlignedTopAppBarColors().containerColor(scrollBehavior.scrollFraction)

    Box(
        modifier = Modifier.background(appBarContainerColor)
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    appBarTitle
                )
            },

            navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            },
            actions = {
                /*IconButton(onClick = { *//* doSomething() *//* }) {
                         Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
                     }*/
            },
            scrollBehavior = scrollBehavior
            /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) colorBelowS
             else colorS*/
        )
    }
}

fun Float.clamp(min: Float, max: Float): Float = Math.max(min, Math.min(max, this))
