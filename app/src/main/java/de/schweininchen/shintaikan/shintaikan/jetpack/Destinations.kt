package de.schweininchen.shintaikan.shintaikan.jetpack

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import de.schweininchen.shintaikan.shintaikan.jetpack.pages.*

private const val TAG = "Destinations"

@ExperimentalMaterial3Api
enum class Destinations(
    val id: String,
    val icon: ImageVector? = null,
    private val displayName: String,
    val navName: String = displayName,
    val uri: String? = null,
    private val imageIndex: Int? = null,
    private val deepLinks: List<String> = listOf(),
    val page: (@Composable (viewModel: MyViewModel) -> Unit)? = { viewModel ->
        FirebaseDataPage(
            imageResource = imageList[imageIndex ?: 0],
            firestoreData = viewModel.firestoreData[id],
            swipeRefreshState = viewModel.swipeResfreshState,
            onRefresh = { refresh(viewModel) })
    }
) {
    HOME(
        id = "home",
        displayName = "Shintaikan",
        navName = "Start",
        icon = Icons.Outlined.Info,
        page = { viewModel ->
            Home(
                viewModel.wordpressList,
                viewModel.firestoreData.isNotEmpty(),
                viewModel.trplanData,
                viewModel.isConnected
            )
        },
        deepLinks = listOf("shintaikan.de")
    ),
    TRPLAN(
        id = "trplan",
        displayName = "Trainingsplan",
        icon = Icons.Outlined.DateRange,
        page = { viewModel -> Trplan(viewModel.trplanData, viewModel::updateTrplan) },
        deepLinks = listOf("shintaikan.de/?page_id=18")
    ),
    PRUEFUNGEN(
        id = "pruefungen",
        displayName = "Gürtelprüfungen",
        icon = Icons.Outlined.SportsMartialArts,
        imageIndex = 0
    ),
    FERIEN(
        id = "ferientraining",
        displayName = "Ferientraining",
        icon = Icons.Outlined.BeachAccess,
        imageIndex = 1
    ),
    NACHSOFE(
        id = "nachsofe",
        displayName = "Nach den Sommerferien",
        icon = Icons.Outlined.WbSunny,
        page = { NachSoFe() }),
    CLUBWEG(
        id = "clubweg",
        displayName = "Der Club / Wegbeschreibung",
        icon = Icons.Outlined.Home,
        page = { ClubWeg() }),
    ANFAENGER(
        id = "anfaenger",
        displayName = "Anfänger / Interressenten",
        icon = Icons.Outlined.DirectionsWalk,
        page = { Anfaenger() }
    ),
    VORFUEHRUNGEN(
        id = "vorfuehrungen",
        displayName = "Vorführungen",
        icon = Icons.Outlined.RemoveRedEye,
        imageIndex = 2
    ),
    LEHRGAENGE(
        id = "turniere",
        displayName = "Lehrgänge & Turniere",
        icon = Icons.Outlined.People,
        imageIndex = 3
    ),
    IMPRESSUM(
        id = "impressum",
        displayName = "Impressum",
        icon = Icons.Outlined.AttachFile,
        uri = "https://shintaikan.de/?page_id=207",
        page = null
    ),
    PRIVACY(
        id = "privacy",
        displayName = "Datenschutz",
        icon = Icons.Outlined.Lock,
        uri = "https://shintaikan.de/?page_id=378",
        page = null
    ),
    MORE(
        id = "more",
        displayName = "Weiteres",
        icon = Icons.Outlined.Face,
        page = null
    ),
    ABOUT(
        id = "about",
        displayName = "Über",
        icon = Icons.Outlined.Info,
        page = null
    ),
    COLORS(id = "colors", displayName = BuildConfig.BUILD_TYPE, page = { Colors() }),
    NONE(id = "", displayName = "Unknown", page = null);

    override fun toString(): String = displayName

    fun NavGraphBuilder.navigationDestination(viewModel: MyViewModel) {
        this@Destinations.page?.let { page ->
            composable(
                route = this@Destinations.id,
                deepLinks = deepLinks.map {
                    navDeepLink {
                        uriPattern = it.replace("?", "\$")
                    }
                },
                content = { page(viewModel) })
        }
    }

    fun call(
        navigate: ((route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit)?,
        closeDrawer: (() -> Unit)?,
        handleUri: ((uri: String) -> Unit)?,
        openDialog: ((Destinations) -> Unit)?
    ) {
        try {
            navigate?.invoke(id) {
                popUpTo(HOME.id)
                launchSingleTop = true
            }
        } catch (e: Exception) {
            Log.e(TAG, ": ", e)
        }
        closeDrawer?.invoke()
        uri?.let { handleUri?.invoke(it) }
        openDialog?.invoke(values().find { it.id == id }!!)
    }
}

private val imageList: IntArray = intArrayOf(
    R.drawable.bonsai,
    R.drawable.sakura,
    R.drawable.seerose1,
    R.drawable.bonsai
).apply { shuffle() }

fun refresh(viewModel: MyViewModel) {
    viewModel.setRefresh(true)
    viewModel.updateFirestoreData {
        viewModel.setRefresh(false)
    }
}
