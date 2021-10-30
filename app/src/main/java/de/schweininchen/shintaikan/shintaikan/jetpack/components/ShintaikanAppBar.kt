package de.schweininchen.shintaikan.shintaikan.jetpack.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
        }
    )
}
