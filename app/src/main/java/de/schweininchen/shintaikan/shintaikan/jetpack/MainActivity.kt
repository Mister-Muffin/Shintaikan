package de.schweininchen.shintaikan.shintaikan.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.*
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "MainActivity.kt"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MyViewModel = viewModel()
            val navController = rememberNavController()
            val url = "https://shintaikan.de/?rest_route=/wp/v2/posts"
            val scope = rememberCoroutineScope()

            if (viewModel.wordpressList.isEmpty()) viewModel.updateHomeData(url, cacheDir)

            fun navDrawerClickie(
                route: String,
                scope: CoroutineScope,
                scaffoldState: ScaffoldState
            ) {
                navController.navigate(route) {
                    popUpTo("Home")
                    launchSingleTop = true
                }
                scope.launch { scaffoldState.drawerState.close() }
            }
            ShintaikanJetpackTheme {
                Bob(
                    onClick = ::navDrawerClickie,
                    navHostController = navController,
                    viewModel.wordpressList, scope, viewModel = viewModel
                )
            }
        }
    }

}

@Composable
private fun Bob(
    onClick: (String, CoroutineScope, ScaffoldState) -> Unit,
    navHostController: NavHostController,
    wordpressList: List<Array<String>>,
    scope: CoroutineScope, viewModel: MyViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val appBarTitle = remember {
        mutableStateOf("Shintaikan")
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(appBarTitle.value) },
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
        },
        drawerContent = drawerContent { onClick(it, scope, scaffoldState) }
    ) {
        val firestoreData = viewModel.firestoreData.value
        if (firestoreData.isEmpty()) {
            viewModel.updateFirestoreData()
        }
        val imageList: IntArray = intArrayOf(
            R.drawable.bonsai,
            R.drawable.sakura,
            R.drawable.seerose1,
            R.drawable.bonsai
        )
        imageList.shuffle()

        val refreshScope = rememberCoroutineScope()
        fun refresh() {
            refreshScope.launch {
                viewModel.setRefresh(true)
                getFirestoreData {
                    refreshScope.launch {
                        viewModel.setRefresh(false)
                    }
                }
            }
        }

        NavHost(navController = navHostController, startDestination = "Home") {
            composable("Home") {
                Home(wordpressList)
                appBarTitle.value = "Shintaikan"
            }
            composable("Trplan") {
                Trplan(viewModel)
                appBarTitle.value = "Trainingsplan"
            }
            composable("Pruefungen") {
                FirebaseDataPage(
                    title = "Gürtelprüfungen",
                    firestoreData = firestoreData["pruefungen"],
                    imageResource = imageList[0],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Gürtelprüfungen"
            }
            composable("Ferien") {
                FirebaseDataPage(
                    title = "Ferientraining",
                    firestoreData = firestoreData["ferientraining"],
                    imageResource = imageList[1],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Ferientraining"
            }
            composable("NachSoFe") {
                NachSoFe()
                appBarTitle.value = "Nach den Sommerferien"
            }
            composable("ClubWeg") {
                ClubWeg()
                appBarTitle.value = "Der Club"
            }
            composable("Anfaenger") {
                Anfaenger()
                appBarTitle.value = "Anfänger / Interressenten"
            }
            composable("Vorfuehrungen") {
                FirebaseDataPage(
                    title = "Vorführungen",
                    firestoreData = firestoreData["vorfuehrungen"],
                    imageResource = imageList[2],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Vorführungen"
            }
            composable("Lehrgaenge") {
                FirebaseDataPage(
                    title = "Lehrgänge + Turniere",
                    firestoreData = firestoreData["turniere"],
                    imageResource = imageList[3],
                    vm = viewModel,
                    onRefresh = ::refresh
                ) {
                    Text(text = "Die Ausschreibungen hängen auch im Dojo!", style = Typography.h3)
                }
                appBarTitle.value = "Lehrgänge + Turniere"
            }
        }
    }
}
