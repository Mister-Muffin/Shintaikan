package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import de.schweininchen.shintaikan.shintaikan.jetpack.BuildConfig
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.*
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun MainNavHost(
    navHostController: NavHostController,
    viewModel: MyViewModel,
    wordpressList: List<Array<String>>,
    imageList: IntArray,
    selectedDrawerItem: NavigationDrawerRoutes,
) {
    val refreshScope = rememberCoroutineScope()
    fun refresh() {
        refreshScope.launch {
            viewModel.setRefresh(true)
            viewModel.updateFirestoreData {
                refreshScope.launch {
                    viewModel.setRefresh(false)
                }
            }
        }
    }
    NavHost(
        navController = navHostController,
        startDestination = NavigationDrawerRoutes.HOME.id,
        modifier = Modifier.padding(top = if (viewModel.isConnected) 0.dp else 30.dp),
    ) {
        composable(NavigationDrawerRoutes.HOME.id) {
            Home(wordpressList, lazyListState = viewModel.lazyListStates[NavigationDrawerRoutes.HOME]!!, firestoreDataNotEmpty = viewModel.firestoreData.isNotEmpty(), trplanData = viewModel.trplanData, isConnected = viewModel.isConnected)
            //setAppBarTitle(NavigationDrawerRoutes.HOME.id)
        }
        composable(NavigationDrawerRoutes.TRPLAN.id) {
            Trplan(viewModel.lazyListStates[NavigationDrawerRoutes.TRPLAN]!!, viewModel.trplanData, viewModel::updateTrplan)
            //appBarTitle.value = "Trainingsplan"
        }
        composable(NavigationDrawerRoutes.PRUEFUNGEN.id) {
            FirebaseDataPage(
                title = "Gürtelprüfungen",
                firestoreData = viewModel.firestoreData["pruefungen"],
                imageResource = imageList[0],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Gürtelprüfungen"
        }
        composable(NavigationDrawerRoutes.FERIEN.id) {
            FirebaseDataPage(
                title = "Ferientraining",
                firestoreData = viewModel.firestoreData["ferientraining"],
                imageResource = imageList[1],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Ferientraining"
        }
        composable(NavigationDrawerRoutes.NACHSOFE.id) {
            NachSoFe(viewModel.lazyListStates[NavigationDrawerRoutes.NACHSOFE]!!)
            //appBarTitle.value = "Nach den Sommerferien"
        }
        composable(NavigationDrawerRoutes.CLUBWEG.id) {
            ClubWeg(viewModel.lazyListStates[NavigationDrawerRoutes.CLUBWEG]!!)
            //appBarTitle.value = "Der Club"
        }
        composable(NavigationDrawerRoutes.ANFAENGER.id) {
            Anfaenger()
            //appBarTitle.value = "Anfänger / Interressenten"
        }
        composable(NavigationDrawerRoutes.VORFUEHRUNGEN.id) {
            FirebaseDataPage(
                title = "Vorführungen",
                firestoreData = viewModel.firestoreData["vorfuehrungen"],
                imageResource = imageList[2],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Vorführungen"
        }
        composable(NavigationDrawerRoutes.LEHRGAENGE.id) {
            FirebaseDataPage(
                title = "Lehrgänge + Turniere",
                firestoreData = viewModel.firestoreData["turniere"],
                imageResource = imageList[3],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            ){
                Text(
                    text = "Die Ausschreibungen hängen auch im Dojo!",
                    style = Typography.h3
                )
            }
            //appBarTitle.value = "Lehrgänge + Turniere"
        }
        composable(NavigationDrawerRoutes.COLORS.id) {
            Colors(viewModel.lazyListStates[NavigationDrawerRoutes.COLORS]!!)
            //setAppBarTitle = BuildConfig.BUILD_TYPE
        }
        composable(
            "?page_id={id}",
            deepLinks = listOf(navDeepLink { uriPattern = "shintaikan.de/?page_id={id}" }) // 18
        ) { backStackEntry ->
            /* TODO: Fix
                if (backStackEntry.arguments?.getString("id") == "18") {
                Trplan(viewModel)
                appBarTitle.value = "Trainingsplan"
                navHostController.navigate(NavigationDrawerRoutes.TRPLAN.toString()) {
                    popUpTo(NavigationDrawerRoutes.HOME.toString())
                    launchSingleTop = true
                }
                selectedDrawerItem.value = NavigationDrawerRoutes.TRPLAN
            } else {
                Home(wordpressList, viewModel = viewModel)
                appBarTitle.value = "Shintaikan"
                navHostController.navigate(NavigationDrawerRoutes.HOME.toString()) {
                    popUpTo(NavigationDrawerRoutes.HOME.toString())
                    launchSingleTop = true
                }
                selectedDrawerItem.value = NavigationDrawerRoutes.HOME
            }*/
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