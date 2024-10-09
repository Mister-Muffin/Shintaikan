package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer.DrawerContent
import de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer.NavigationDrawerRoutes
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterial3Api
@Composable
fun AppContent(
    onClick: (NavigationDrawerRoutes?, CoroutineScope, DrawerState) -> Unit,
    navHostController: NavHostController,
    wordpressList: List<Array<String>>,
    selectedDrawerItem: MutableState<NavigationDrawerRoutes>,
    scope: CoroutineScope,
    viewModel: MyViewModel,
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
            LaunchedEffect(null) {
                imageList.shuffle()
            }

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
                    imageList
                )
            }
        }
    }
}
