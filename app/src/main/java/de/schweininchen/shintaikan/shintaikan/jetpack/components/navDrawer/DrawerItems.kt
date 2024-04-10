package de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.SportsMartialArts
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.ContactActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.linkToWebpage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun DrawerItem(
    name: String,
    icon: ImageVector,
    externalLink: Boolean = false,
    disabled: Boolean = false,
    selected: NavigationDrawerRoutes? = null,
    route: NavigationDrawerRoutes = NavigationDrawerRoutes.NONE,
    onClick: (NavigationDrawerRoutes?) -> Unit
) {
    val disabledColor = MaterialTheme.colorScheme.surfaceVariant

    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(name) },
        selected = selected == route,
        onClick = { onClick(route) },
        badge = {
            if (externalLink) Icon(
                Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = null
            )
        },
        colors = if (disabled) NavigationDrawerItemDefaults.colors(
            unselectedTextColor = disabledColor,
            unselectedBadgeColor = disabledColor,
            unselectedIconColor = disabledColor
        ) else NavigationDrawerItemDefaults.colors(),
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItems(
    selectedMain: NavigationDrawerRoutes,
    onClickMain: (NavigationDrawerRoutes?) -> Unit,
    openCustomDialog: MutableState<Boolean>,
    openDialog: MutableState<Boolean>,
    showDebugInfo: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    listState: LazyListState
) {
    val context = LocalContext.current
    Column {
        DrawerItem(
            "Start",
            Icons.Outlined.Info,
            selected = selectedMain,
            route = NavigationDrawerRoutes.HOME,
            onClick = onClickMain,
        )
        DrawerItem(
            "Trainingsplan",
            Icons.Outlined.DateRange,
            selected = selectedMain,
            route = NavigationDrawerRoutes.TRPLAN,
            onClick = onClickMain,
        )
        DrawerItem(
            "Gürtelprüfungen",
            Icons.Outlined.SportsMartialArts,
            selected = selectedMain,
            route = NavigationDrawerRoutes.PRUEFUNGEN,
            onClick = onClickMain,
        )
        DrawerItem(
            "Nach den Sommerferien",
            Icons.Outlined.WbSunny,
            selected = selectedMain,
            route = NavigationDrawerRoutes.NACHSOFE,
            onClick = onClickMain,
        )
        DrawerItem(
            "Der Club / Wegbeschreibung",
            Icons.Outlined.Home,
            selected = selectedMain,
            route = NavigationDrawerRoutes.CLUBWEG,
            onClick = onClickMain,
        )
        DrawerItem(
            "Anfänger / Interressenten",
            Icons.AutoMirrored.Outlined.DirectionsWalk,
            selected = selectedMain,
            route = NavigationDrawerRoutes.ANFAENGER,
            onClick = onClickMain,
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        DrawerItem(
            "Filmchen",
            Icons.Outlined.Movie, externalLink = true,
        ) { linkToWebpage("https://shintaikan.de/?page_id=235", context = context) }
        HorizontalDivider(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        DrawerItem(
            "Kontakt und Feedback",
            Icons.Outlined.Mail, externalLink = false,
        ) {
            Intent(context, ContactActivity::class.java).also { context.startActivity(it) }
        }
        DrawerItem(
            "Impressum",
            Icons.Outlined.AttachFile, true
        ) { linkToWebpage("https://shintaikan.de/?page_id=207", context = context) }
        DrawerItem(
            "Datenschutz",
            Icons.Outlined.Lock, true
        ) { linkToWebpage("https://shintaikan.de/?page_id=378", context = context) }
        DrawerItem(
            "Weiteres",
            Icons.Outlined.Face
        ) { openCustomDialog.value = true }
        //
        DrawerItem(
            "Über",
            Icons.Outlined.Info,
        ) {
            openDialog.value = true
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Image(
            painter = painterResource(id = R.drawable.jetpackcompose_logo),
            contentDescription = "Jetpack Compose logo",
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .align(Alignment.CenterHorizontally)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            onClickMain(NavigationDrawerRoutes.COLORS)
                        },
                        onTap = {
                            showDebugInfo.value = true
                            coroutineScope.launch {
                                listState.animateScrollToItem(index = listState.layoutInfo.totalItemsCount)
                            }
                        }
                    )
                }
        )
        Box(modifier = Modifier.size(8.dp))
    }
}
