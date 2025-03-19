package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import de.schweininchen.shintaikan.shintaikan.jetpack.BuildConfig
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Anfaenger
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.ClubWeg
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Colors
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.FirebaseDataPage
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Home
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.NachSoFe
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.Trplan
import kotlinx.coroutines.launch

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    viewModel: MyViewModel,
    wordpressList: List<Array<String>>,
    appBarTitle: MutableState<String>,
    imageList: IntArray
) {
    val webLinkScheme = "https://"
    val webLinkDomain = "shintaikan.de"

    val refreshScope = rememberCoroutineScope()
    fun refresh(endRefresh: () -> Unit) {
        refreshScope.launch {
            viewModel.updateFirestoreData {
                refreshScope.launch {
                    endRefresh()
                }
            }
        }
    }
    // Note: As it seems, NavHost automatically routes to a composable via an app shortcut,
    // if the shortcut ID matches the composable ID without any explicit code.
    NavHost(
        navController = navHostController,
        startDestination = NavigationDrawerRoutes.HOME.toString(),
    ) {
        composable(NavigationDrawerRoutes.HOME.toString()) {
            Home(wordpressList, viewModel)
            appBarTitle.value = "Shintaikan"
        }
        composable(
            NavigationDrawerRoutes.TRPLAN.toString(),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$webLinkScheme$webLinkDomain/trainingsplan"
                    action = Intent.ACTION_VIEW
                }
            ),
        ) {
            Trplan(viewModel)
            appBarTitle.value = "Trainingsplan"
        }
        composable(NavigationDrawerRoutes.PRUEFUNGEN.toString()) {
            FirebaseDataPage(
                title = "Gürtelprüfungen",
                document = "pruefungen",
                imageResource = imageList[0],
                vm = viewModel,
                onRefresh = ::refresh
            )
            appBarTitle.value = "Gürtelprüfungen"
        }
        composable(NavigationDrawerRoutes.NACHSOFE.toString()) {
            NachSoFe(viewModel)
            appBarTitle.value = "Nach den Sommerferien"
        }
        composable(
            NavigationDrawerRoutes.CLUBWEG.toString(), deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$webLinkScheme$webLinkDomain/weg-und-dojo"
                    action = Intent.ACTION_VIEW
                }
            )) {
            ClubWeg(viewModel)
            appBarTitle.value = "Der Club"
        }
        composable(NavigationDrawerRoutes.ANFAENGER.toString()) {
            Anfaenger()
            appBarTitle.value = "Anfänger / Interressenten"
        }
        composable(NavigationDrawerRoutes.COLORS.toString()) {
            Colors(vm = viewModel)
            appBarTitle.value = BuildConfig.BUILD_TYPE
        }
    }
}
