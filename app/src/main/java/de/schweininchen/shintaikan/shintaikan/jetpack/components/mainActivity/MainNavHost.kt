package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.Destinations
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.*
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun MainNavHost(
    navHostController: NavHostController,
    viewModel: MyViewModel,
    wordpressList: List<Array<String>>,
    imageList: IntArray,
    selectedDrawerItem: Destinations,
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
    // TODO: Clean up repeated code
    NavHost(
        navController = navHostController,
        startDestination = Destinations.HOME.id,
        modifier = Modifier.padding(top = if (viewModel.isConnected) 0.dp else 30.dp),
    ) {
        composable(Destinations.HOME.id) {
            Home(wordpressList, lazyListState = viewModel.lazyListStates[Destinations.HOME]!!, firestoreDataNotEmpty = viewModel.firestoreData.isNotEmpty(), trplanData = viewModel.trplanData, isConnected = viewModel.isConnected)
            //setAppBarTitle(NavigationDrawerRoutes.HOME.id)
        }
        composable(Destinations.TRPLAN.id) {
            Trplan(lazyListState = viewModel.lazyListStates[Destinations.TRPLAN]!!, viewModel.trplanData, viewModel::updateTrplan)
            //appBarTitle.value = "Trainingsplan"
        }
        composable(Destinations.PRUEFUNGEN.id) {
            FirebaseDataPage(
                title = "Gürtelprüfungen",
                firestoreData = viewModel.firestoreData["pruefungen"],
                imageResource = imageList[0],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                lazyListState = viewModel.lazyListStates[Destinations.PRUEFUNGEN]!!,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Gürtelprüfungen"
        }
        composable(Destinations.FERIEN.id) {
            FirebaseDataPage(
                title = "Ferientraining",
                firestoreData = viewModel.firestoreData["ferientraining"],
                imageResource = imageList[1],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                lazyListState = viewModel.lazyListStates[Destinations.FERIEN]!!,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Ferientraining"
        }
        composable(Destinations.NACHSOFE.id) {
            NachSoFe(viewModel.lazyListStates[Destinations.NACHSOFE]!!)
            //appBarTitle.value = "Nach den Sommerferien"
        }
        composable(Destinations.CLUBWEG.id) {
            ClubWeg(viewModel.lazyListStates[Destinations.CLUBWEG]!!)
            //appBarTitle.value = "Der Club"
        }
        composable(Destinations.ANFAENGER.id) {
            Anfaenger(viewModel.lazyListStates[Destinations.ANFAENGER]!!)
            //appBarTitle.value = "Anfänger / Interressenten"
        }
        composable(Destinations.VORFUEHRUNGEN.id) {
            FirebaseDataPage(
                title = "Vorführungen",
                firestoreData = viewModel.firestoreData["vorfuehrungen"],
                imageResource = imageList[2],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                lazyListState = viewModel.lazyListStates[Destinations.VORFUEHRUNGEN]!!,
                onRefresh = ::refresh
            )
            //appBarTitle.value = "Vorführungen"
        }
        composable(Destinations.LEHRGAENGE.id) {
            FirebaseDataPage(
                title = "Lehrgänge + Turniere",
                firestoreData = viewModel.firestoreData["turniere"],
                imageResource = imageList[3],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                lazyListState = viewModel.lazyListStates[Destinations.LEHRGAENGE]!!,
                onRefresh = ::refresh
            ){
                Text(
                    text = "Die Ausschreibungen hängen auch im Dojo!",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            //appBarTitle.value = "Lehrgänge + Turniere"
        }
        composable(Destinations.COLORS.id) {
            Colors(viewModel.lazyListStates[Destinations.COLORS]!!)
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