package de.schweininchen.shintaikan.shintaikan.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Home
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Trplan
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            fun navDrawerClickie(route: String, scope: CoroutineScope, scaffoldState: ScaffoldState) {
                navController.navigate(route) {popUpTo("Home")
                    launchSingleTop = true;
                }
                scope.launch { scaffoldState.drawerState.close()}
            }
            ShintaikanJetpackTheme {
                Bob(onClick = ::navDrawerClickie, navHostController = navController)
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
private fun Bob(onClick: (String, CoroutineScope, ScaffoldState) -> Unit, navHostController: NavHostController) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Shintaikan") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                },
                actions = {
                    // RowScope here, so these icons will be placed horizontally
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
                    }
                }
            )
        },
        drawerContent = drawerContent({onClick(it, scope, scaffoldState)})
    ) {
        NavHost(navController = navHostController, startDestination = "Home") {
            composable("Home") { Home() }
            composable("Trplan") { Trplan() }
        }
    }
}

