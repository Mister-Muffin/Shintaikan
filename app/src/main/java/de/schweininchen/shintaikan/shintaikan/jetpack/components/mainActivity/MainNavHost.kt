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
            Home(wordpressList, firestoreDataNotEmpty = viewModel.firestoreData.isNotEmpty(), trplanData = viewModel.trplanData, isConnected = viewModel.isConnected)
        }
        composable(Destinations.TRPLAN.id) {
            Trplan(viewModel.trplanData, viewModel::updateTrplan)
        }
        composable(Destinations.PRUEFUNGEN.id) {
            FirebaseDataPage(
                title = "Gürtelprüfungen",
                firestoreData = viewModel.firestoreData["pruefungen"],
                imageResource = imageList[0],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
        }
        composable(Destinations.FERIEN.id) {
            FirebaseDataPage(
                title = "Ferientraining",
                firestoreData = viewModel.firestoreData["ferientraining"],
                imageResource = imageList[1],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
        }
        composable(Destinations.NACHSOFE.id) {
            NachSoFe()
        }
        composable(Destinations.CLUBWEG.id) {
            ClubWeg()
        }
        composable(Destinations.ANFAENGER.id) {
            Anfaenger()
        }
        composable(Destinations.VORFUEHRUNGEN.id) {
            FirebaseDataPage(
                title = "Vorführungen",
                firestoreData = viewModel.firestoreData["vorfuehrungen"],
                imageResource = imageList[2],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            )
        }
        composable(Destinations.LEHRGAENGE.id) {
            FirebaseDataPage(
                title = "Lehrgänge + Turniere",
                firestoreData = viewModel.firestoreData["turniere"],
                imageResource = imageList[3],
                isRefreshing = viewModel.isRefreshing.collectAsState().value,
                onRefresh = ::refresh
            ){
                Text(
                    text = "Die Ausschreibungen hängen auch im Dojo!",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        composable(Destinations.COLORS.id) {
            Colors()
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