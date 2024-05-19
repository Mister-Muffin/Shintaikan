package de.schweininchen.shintaikan.shintaikan.jetpack

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
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
            val viewModel: MyViewModel = viewModel()
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            val selectedDrawerItem = remember {
                mutableStateOf(NavigationDrawerRoutes.HOME)
            }

            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            val url = remoteConfig.getString("wp_api_url")

            val context = applicationContext
            LaunchedEffect(Unit) {
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d(TAG, "Config params updated: $updated")
                        Toast.makeText(
                            context,
                            "Fetch and activate succeeded",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Fetch failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }

            LaunchedEffect(true) {
                autoSetConnectionState(context, viewModel, url)
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
                if (route != null) {

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
                    drawerState = drawerState,
                    remoteConfig = remoteConfig
                )
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
    drawerState: DrawerState,
    remoteConfig: FirebaseRemoteConfig
) {
    val appBarTitle = remember {
        mutableStateOf("Shintaikan")
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(viewModel, selectedDrawerItem.value, remoteConfig) {
                    onClick(it, scope, drawerState)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                ShintaikanAppBar(
                    appBarTitle,
                    scope,
                    drawerState,
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { innerPadding ->
            val firestoreData = viewModel.firestoreData
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

            Column(
                modifier = Modifier
                    .imePadding()
                    .padding(innerPadding),
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
                        Icons.Outlined.CloudOff,
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
