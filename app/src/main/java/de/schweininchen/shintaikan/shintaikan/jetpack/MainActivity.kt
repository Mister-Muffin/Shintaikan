package de.schweininchen.shintaikan.shintaikan.jetpack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

            val selectedDrawerItem = remember {
                mutableStateOf(NavigationDrawerRoutes.HOME)
            }

            if (viewModel.wordpressList.isEmpty()) viewModel.updateHomeData(url, cacheDir)
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                if (token != null) {
                    viewModel.updatefirebaseMessagingToken(token = token)
                }

            })

            fun navDrawerClickie(
                route: NavigationDrawerRoutes?,
                scope: CoroutineScope,
                scaffoldState: ScaffoldState
            ) {
                if (route !== null) {
                    navController.navigate(route.toString()) {
                        popUpTo(NavigationDrawerRoutes.HOME.toString())
                        launchSingleTop = true
                    }
                    selectedDrawerItem.value = route
                    scope.launch { scaffoldState.drawerState.close() }
                }
            }
            ShintaikanJetpackTheme {
                Bob(
                    onClick = ::navDrawerClickie,
                    navHostController = navController,
                    viewModel.wordpressList,
                    scope = scope,
                    selectedDrawerItem = selectedDrawerItem.value,
                    viewModel = viewModel
                )
            }
        }
    }

}

@Composable
private fun Bob(
    onClick: (NavigationDrawerRoutes?, CoroutineScope, ScaffoldState) -> Unit,
    navHostController: NavHostController,
    wordpressList: List<Array<String>>,
    selectedDrawerItem: NavigationDrawerRoutes,
    scope: CoroutineScope, viewModel: MyViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val appBarTitle = remember {
        mutableStateOf("Shintaikan")
    }

    /* if (viewModel.exoPlayer == null) {

     }

     val uri =
         "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
     val context = LocalContext.current

     if (appBarTitle.value.contains("film")) {
         exoPlayer.play()
     } else if (!appBarTitle.value.contains("Film") && exoPlayer.isPlaying) {
         exoPlayer.stop()
     }
     exoPlayer.stop()*/

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        appBarTitle.value
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
        },
        drawerContent = drawerContent(viewModel, selectedDrawerItem) {
            onClick(
                it,
                scope,
                scaffoldState
            )
        }
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

        NavHost(
            navController = navHostController,
            startDestination = NavigationDrawerRoutes.HOME.toString()
        ) {
            composable(NavigationDrawerRoutes.HOME.toString()) {
                Home(wordpressList)
                appBarTitle.value = "Shintaikan"
            }
            composable(NavigationDrawerRoutes.TRPLAN.toString()) {
                Trplan(viewModel)
                appBarTitle.value = "Trainingsplan"
            }
            composable(NavigationDrawerRoutes.PRUEFUNGEN.toString()) {
                FirebaseDataPage(
                    title = "Gürtelprüfungen",
                    firestoreData = firestoreData["pruefungen"],
                    imageResource = imageList[0],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Gürtelprüfungen"
            }
            composable(NavigationDrawerRoutes.FERIEN.toString()) {
                FirebaseDataPage(
                    title = "Ferientraining",
                    firestoreData = firestoreData["ferientraining"],
                    imageResource = imageList[1],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Ferientraining"
            }
            composable(NavigationDrawerRoutes.NACHSOFE.toString()) {
                NachSoFe()
                appBarTitle.value = "Nach den Sommerferien"
            }
            composable(NavigationDrawerRoutes.CLUBWEG.toString()) {
                ClubWeg()
                appBarTitle.value = "Der Club"
            }
            composable(NavigationDrawerRoutes.ANFAENGER.toString()) {
                Anfaenger()
                appBarTitle.value = "Anfänger / Interressenten"
            }
            composable(NavigationDrawerRoutes.VORFUEHRUNGEN.toString()) {
                FirebaseDataPage(
                    title = "Vorführungen",
                    firestoreData = firestoreData["vorfuehrungen"],
                    imageResource = imageList[2],
                    vm = viewModel,
                    onRefresh = ::refresh
                )
                appBarTitle.value = "Vorführungen"
            }
            composable(NavigationDrawerRoutes.LEHRGAENGE.toString()) {
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
            /* composable("Movie1") {

                 ExoVideoPlayer(
                     exoPlayer = exoPlayer
                 )
                 appBarTitle.value = "Infofilmchen"
                 exoPlayer.play()
             }*/
        }
    }
}
