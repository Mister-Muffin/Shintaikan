package de.schweininchen.shintaikan.shintaikan.jetpack

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity.MainNavHost
import de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity.ShintaikanAppBar
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "MainActivity.kt"

@ExperimentalMaterial3Api
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                val viewModel: MyViewModel = viewModel()
                val navController = rememberNavController()
                val url = "https://shintaikan.de/?rest_route=/wp/v2/posts"
                val scope = rememberCoroutineScope()

                val selectedDrawerItem = remember {
                    mutableStateOf(NavigationDrawerRoutes.HOME)
                }

                LaunchedEffect(true) {
                    abc(baseContext, viewModel)
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

                if (viewModel.firestoreData.isEmpty()) viewModel.updateTrplan()

                viewModel.lazyState = viewModel.lazyStateStart

                val drawerState = rememberDrawerState(DrawerValue.Closed)

                fun navDrawerClickie(
                    route: NavigationDrawerRoutes?,
                    scope: CoroutineScope,
                    drawerState: DrawerState
                ) {
                    if (route !== null) {

                        changeLazyState(route, viewModel)

                        scope.launch {
                            if (!viewModel.lazyState.isScrollInProgress) viewModel.lazyState.scrollToItem(
                                0
                            )
                        }
                        navController.navigate(route.toString()) {
                            popUpTo(NavigationDrawerRoutes.HOME.toString())
                            launchSingleTop = true
                        }
                        selectedDrawerItem.value = route
                        scope.launch {
                            drawerState.close()
                        }
                    }
                }
                ShintaikanJetpackTheme {
                    Bob(
                        onClick = ::navDrawerClickie,
                        navHostController = navController,
                        viewModel.wordpressList,
                        scope = scope,
                        selectedDrawerItem = selectedDrawerItem,
                        viewModel = viewModel,
                        drawerState = drawerState
                    )
                }
            }
        }

    }

    private fun changeLazyState(
        route: NavigationDrawerRoutes,
        viewModel: MyViewModel
    ) {
        when (route) {
            NavigationDrawerRoutes.HOME -> {
                viewModel.lazyState = viewModel.lazyStateStart
            }
            NavigationDrawerRoutes.TRPLAN -> {
                viewModel.lazyState = viewModel.lazyStateTrplan
            }
            NavigationDrawerRoutes.PRUEFUNGEN -> {
                viewModel.lazyState = viewModel.lazyStatePruef
            }
            NavigationDrawerRoutes.NACHSOFE -> {
                viewModel.lazyState = viewModel.lazyStateSoFe
            }
            NavigationDrawerRoutes.CLUBWEG -> {
                viewModel.lazyState = viewModel.lazyStateClub
            }
            NavigationDrawerRoutes.ANFAENGER -> {
                viewModel.lazyState = viewModel.lazyStateAnf
            }
            NavigationDrawerRoutes.COLORS -> {
                viewModel.lazyState = viewModel.lazyStateColors
            }
            else -> {
                viewModel.lazyState = viewModel.lazyStateStart
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Bob(
    onClick: (NavigationDrawerRoutes?, CoroutineScope, DrawerState) -> Unit,
    navHostController: NavHostController,
    wordpressList: List<Array<String>>,
    selectedDrawerItem: MutableState<NavigationDrawerRoutes>,
    scope: CoroutineScope, viewModel: MyViewModel,
    drawerState: DrawerState
) {
    rememberScaffoldState()
    val appBarTitle = remember {
        mutableStateOf("Shintaikan")
    }

    /*val uri =
         "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
     val context = LocalContext.current

     if (appBarTitle.value.contains("film")) {
         exoPlayer.play()
     } else if (!appBarTitle.value.contains("Film") && exoPlayer.isPlaying) {
         exoPlayer.stop()
     }
     exoPlayer.stop()*/

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = drawerContent(viewModel, selectedDrawerItem.value) {
            onClick(it, scope, drawerState)
        },
    ) {
        Scaffold(
            topBar = {
                ShintaikanAppBar(
                    appBarTitle,
                    scope,
                    drawerState,
                    scrollBehavior= scrollBehavior
                )
            }
        ) { innerPadding ->
            val firestoreData = viewModel.firestoreData
            if (firestoreData.isEmpty()) {
                viewModel.updateFirestoreData {
                    scope.launch {
                        viewModel.setRefresh(false)
                    }
                }
            }
            val imageList: IntArray = intArrayOf(
                R.drawable.bonsai,
                R.drawable.sakura,
                R.drawable.seerose1,
                R.drawable.bonsai
            )
            imageList.shuffle()

            Column(
                modifier = Modifier
                    .navigationBarsWithImePadding()
                    .padding(innerPadding)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                if (!viewModel.isConnected.value) Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color.Red),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CloudOff,
                        tint = Color.White,
                        contentDescription = "Offline icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "OFFLINE!",
                        style = TextStyle(color = Color.White),
                    )
                }
                MainNavHost(
                    navHostController,
                    viewModel,
                    wordpressList,
                    appBarTitle,
                    imageList,
                    selectedDrawerItem,
                )
            }
        }
    }
}