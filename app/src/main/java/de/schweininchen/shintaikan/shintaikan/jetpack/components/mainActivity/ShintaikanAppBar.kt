package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun ShintaikanAppBar(
    appBarTitle: MutableState<String>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val color =
        TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = .8f
            ),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    CenterAlignedTopAppBar(
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
        colors = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) color
        else TopAppBarDefaults.centerAlignedTopAppBarColors()
    )

}
