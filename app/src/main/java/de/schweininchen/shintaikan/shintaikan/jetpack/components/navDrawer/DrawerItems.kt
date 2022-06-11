package de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.linkToWebpage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
        onClick = {
            onClick(route)
        },
        badge = { if (externalLink) Icon(Icons.Outlined.ExitToApp, contentDescription = null) },
        colors = if (disabled) NavigationDrawerItemDefaults.colors(
            unselectedTextColor = disabledColor,
            unselectedBadgeColor = disabledColor,
            unselectedIconColor = disabledColor
        ) else NavigationDrawerItemDefaults.colors(),
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
        /*.clip(MaterialTheme.shapes.medium)
        .clickable(onClick = { onClick(route) })
        .background(
            color = if (selected == route) MaterialTheme.colorScheme.primary.copy(
                alpha = .15f
            ) else Color.Transparent
        )
        .padding(top = customPadding, bottom = customPadding, start = 12.dp, end = 12.dp),
    verticalAlignment = Alignment.CenterVertically*/
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItems(
    selectedMain: NavigationDrawerRoutes,
    onClickMain: (NavigationDrawerRoutes?) -> Unit,
    context: Context,
    openCustomDialog: MutableState<Boolean>,
    openDialog: MutableState<Boolean>,
    showDebugInfo: MutableState<Boolean>,
    coroutineScope: CoroutineScope,
    listState: LazyListState
) {
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
            Icons.Outlined.NorthEast,
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
            Icons.Outlined.DirectionsWalk,
            selected = selectedMain,
            route = NavigationDrawerRoutes.ANFAENGER,
            onClick = onClickMain,
        )
        Divider(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        DrawerItem(
            "Infofilmchen",
            Icons.Outlined.Movie, externalLink = false, disabled = true
        ) { }
        DrawerItem(
            "Seefest 2019",
            Icons.Outlined.Movie, externalLink = false, disabled = true
        ) { }
        DrawerItem(
            "Mixfilm 2019",
            Icons.Outlined.Movie, externalLink = false, disabled = true
        ) { }
        Divider(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colorScheme.outline
        )
        DrawerItem(
            "Kontakt und Feedback",
            Icons.Outlined.Mail, externalLink = false, disabled = true
        ) { }
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
        Divider(
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