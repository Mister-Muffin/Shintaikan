package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.getFirestoreData
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.*
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    viewModel: MyViewModel,
    wordpressList: List<Array<String>>,
    appBarTitle: MutableState<String>,
    firestoreData: Map<String, MutableMap<String, Any>>,
    imageList: IntArray,
    lazyState: LazyListState
) {
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
        startDestination = NavigationDrawerRoutes.HOME.toString(),
        modifier = Modifier.padding(top = if (viewModel.isConnected.value) 0.dp else 30.dp),
    ) {
        composable(NavigationDrawerRoutes.HOME.toString()) {
            Home(wordpressList, viewModel = viewModel, lazyState = lazyState)
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
                Text(
                    text = "Die Ausschreibungen hängen auch im Dojo!",
                    style = Typography.h3
                )
            }
            appBarTitle.value = "Lehrgänge + Turniere"
        }
        composable(
            "?page_id={id}",
            deepLinks = listOf(navDeepLink { uriPattern = "shintaikan.de/?page_id={id}" }) // 18
        ) { backStackEntry ->
            if (backStackEntry.arguments?.getString("id") == "18") {
                Trplan(viewModel)
                appBarTitle.value = "Trainingsplan"
            } else {
                Home(wordpressList, viewModel = viewModel, lazyState = lazyState)
                appBarTitle.value = "Shintaikan"
            }
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