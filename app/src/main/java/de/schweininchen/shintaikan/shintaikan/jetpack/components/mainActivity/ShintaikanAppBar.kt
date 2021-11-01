package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ShintaikanAppBar(
    modifier: Modifier,
    appBarTitle: MutableState<String>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    lazyState: LazyListState
) {
    val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = lazyState.firstVisibleItemScrollOffset.toFloat()
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
                    appBarTitle.value, fontSize = 20.sp
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
