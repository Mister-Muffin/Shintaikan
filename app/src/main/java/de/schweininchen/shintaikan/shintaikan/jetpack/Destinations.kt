package de.schweininchen.shintaikan.shintaikan.jetpack

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptionsBuilder

private const val TAG = "Destinations"

enum class Destinations(
    val id: String,
    val icon: ImageVector? = null,
    private val displayName: String,
    val navName: String = displayName,
    val uri: String? = null,
    val call: (navigate: ((route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit)?, closeDrawer: (() -> Unit)?, handleUri: ((uri: String) -> Unit)?, openDialog: ((Destinations) -> Unit)?) -> Unit = { navigate, closeDrawer, handleUri, openDialog ->
        try {
            navigate?.invoke(id) {
                popUpTo(HOME.id)
                launchSingleTop = true
            }
        } catch (e: Exception) {
            Log.e(TAG, ": ", e)
        }
        closeDrawer?.invoke()
        uri?.let { handleUri?.invoke(it)}
        openDialog?.invoke(values().find { it.id == id }!!)
    }
) {
    HOME(id = "home", displayName = "Shintaikan", navName = "Start", icon = Icons.Outlined.Info),
    TRPLAN(id = "trplan", displayName = "Trainingsplan", icon = Icons.Outlined.DateRange),
    PRUEFUNGEN(
        id = "pruefungen",
        displayName = "Gürtelprüfungen",
        icon = Icons.Outlined.SportsMartialArts
    ),
    FERIEN(
        id = "ferientraining",
        displayName = "Ferientraining",
        icon = Icons.Outlined.BeachAccess
    ),
    NACHSOFE(id = "nachsofe", displayName = "Nach den Sommerferien", icon = Icons.Outlined.WbSunny),
    CLUBWEG(id = "clubweg", displayName = "Der Club / Wegbeschreibung", icon = Icons.Outlined.Home),
    ANFAENGER(
        id = "anfaenger",
        displayName = "Anfänger / Interressenten",
        icon = Icons.Outlined.DirectionsWalk
    ),
    VORFUEHRUNGEN(
        id = "vorfuehrungen",
        displayName = "Vorführungen",
        icon = Icons.Outlined.RemoveRedEye
    ),
    LEHRGAENGE(id = "turniere", displayName = "Lehrgänge & Turniere", icon = Icons.Outlined.People),
    IMPRESSUM(
        id = "impressum",
        displayName = "Impressum",
        icon = Icons.Outlined.AttachFile,
        uri = "https://shintaikan.de/?page_id=207"
    ),
    PRIVACY(
        id = "privacy",
        displayName = "Datenschutz",
        icon = Icons.Outlined.Lock,
        uri = "https://shintaikan.de/?page_id=378"
    ),
    MORE(
        id = "more",
        displayName = "Weiteres",
        icon = Icons.Outlined.Face,
    ),
    ABOUT(
        id = "about",
        displayName = "Über",
        icon = Icons.Outlined.Info,
    ),
    COLORS(id = "colors", displayName = BuildConfig.BUILD_TYPE),
    NONE(id = "", displayName = "Unknown");

    override fun toString(): String = displayName
}