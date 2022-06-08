package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity.MainNavHost
import de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity.ShintaikanAppBar
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "MainActivity.kt"

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
class MainActivity : AppCompatActivity() {

    var tryCloseNavigationDrawer: () -> Boolean = { false }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "hi")
        if (intent?.dataString != null) intent.data = intent.dataString!!.replace("?", "\$").toUri()
        Log.d(TAG, "onNewIntent: ${navHostController.handleDeepLink(intent)}")
        super.onNewIntent(intent)
    }

    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val viewModel: MyViewModel = viewModel()
            navHostController = rememberNavController()
            val url = "https://shintaikan.de/?rest_route=/wp/v2/posts"
            val scope = rememberCoroutineScope()

            var selectedDrawerItem by remember {
                mutableStateOf(Destinations.HOME)
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

            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val coroutineScope = rememberCoroutineScope()
            tryCloseNavigationDrawer = {
                coroutineScope.launch {
                    drawerState.close()
                }
                drawerState.isOpen
            }

            ShintaikanJetpackTheme {
                Bob(
                    navHostController = navHostController,
                    wordpressList = viewModel.wordpressList,
                    scope = scope,
                    viewModel = viewModel,
                    drawerState = drawerState
                )
            }
        }

    }

    override fun onBackPressed() {
        if (!tryCloseNavigationDrawer())
            super.onBackPressed()
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
private fun Bob(
    navHostController: NavHostController,
    wordpressList: List<Array<String>>,
    scope: CoroutineScope,
    viewModel: MyViewModel,
    drawerState: DrawerState
) {
    val currentDestination = Destinations.values()
        .find { it.id == navHostController.currentBackStackEntryAsState().value?.destination?.route }
        ?: Destinations.NONE
    val appBarTitle = currentDestination.toString()

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
        drawerContent = {
            DrawerContent(
                currentSelection = currentDestination,
                navigate = navHostController::navigate,
                closeDrawer = { scope.launch { drawerState.close() } },
                firebaseMessagingToken = viewModel.firebaseMessagingToken
            )
        }) {
        Scaffold(
            topBar = {
                ShintaikanAppBar(
                    appBarTitle,
                    scope,
                    drawerState,
                    scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { contentPadding ->
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
                    .padding(contentPadding)
                    .imePadding()
            ) {

                if (!viewModel.isConnected) Row(
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
                    imageList,
                    currentDestination,
                )
            }
        }
    }
}