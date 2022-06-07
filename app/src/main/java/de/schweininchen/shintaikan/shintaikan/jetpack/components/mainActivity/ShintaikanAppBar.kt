package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
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
    scaffoldState: ScaffoldState,
    lazyState: LazyListState
) {
    val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = if (lazyState.firstVisibleItemIndex == 0)
            (lazyState.firstVisibleItemScrollOffset.toFloat() - 50f).clamp(0f, 1f)
        else 1f
    ).value
    val foregroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Box(
        modifier = Modifier.background(backgroundColor)
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    appBarTitle, fontSize = 20.sp
                )
            },

            navigationIcon = {
                IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            },
            actions = {
                /*IconButton(onClick = { *//* doSomething() *//* }) {
                         Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
                     }*/
            },
            colors = foregroundColors
            /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) colorBelowS
             else colorS*/
        )
    }
}

fun Float.clamp(min: Float, max: Float): Float = Math.max(min, Math.min(max, this))
