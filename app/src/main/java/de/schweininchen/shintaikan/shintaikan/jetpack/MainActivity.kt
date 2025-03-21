package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity.AppContent
import de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import de.schweininchen.shintaikan.shintaikan.jetpack.util.autoSetConnectionState
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

            val context = baseContext
            LaunchedEffect(Unit) {
                createNotificationChannel()
                initRemoteConfig(remoteConfig, context, viewModel)
            }

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                Log.d(TAG, "Firebase Messaging fetched.")
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
                        if (!viewModel.lazyState.isScrollInProgress)
                            viewModel.lazyState.scrollToItem(0)
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
                AppContent(
                    onClick = ::navDrawerClickie,
                    navHostController = navController,
                    wordpressList = viewModel.wordpressList,
                    scope = scope,
                    selectedDrawerItem = selectedDrawerItem,
                    viewModel = viewModel,
                    drawerState = drawerState,
                    remoteConfig = remoteConfig
                )
            }
        }
    }

    private fun initRemoteConfig(
        remoteConfig: FirebaseRemoteConfig,
        context: Context,
        viewModel: MyViewModel
    ) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Config params updated: $updated")
                Log.i(TAG, "Config updated.")
            } else {
                Toast.makeText(context, "Fetch failed", Toast.LENGTH_SHORT).show()
            }
            val url = remoteConfig.getString("wp_api_url")

            // this also will auto query wp posts
            autoSetConnectionState(context, viewModel, url)
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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default"
            val descriptionText = "Alle Benachrichtigungen, Einstellungen sind in der App"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
