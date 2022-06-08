package de.schweininchen.shintaikan.shintaikan.jetpack.components.mainActivity

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import de.schweininchen.shintaikan.shintaikan.jetpack.Destinations
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel

@ExperimentalMaterial3Api
@Composable
fun MainNavHost(
    navHostController: NavHostController,
    viewModel: MyViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Destinations.HOME.id,
        modifier = Modifier.padding(top = if (viewModel.isConnected) 0.dp else 30.dp),
    ) {
        for(destination in Destinations.values().filter { it.page != null }) {
            with(destination){ navigationDestination(viewModel) }
        }
    }
}