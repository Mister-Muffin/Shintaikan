package de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.NavigationDrawerRoutes
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.customPadding
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
    val color =
        if (selected == route && !disabled) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color(
            0xFF898989
        )
    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = { onClick(route) })
            .background(
                color = if (selected == route) androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(
                    alpha = .15f
                ) else Color.Transparent
            )
            .padding(top = customPadding, bottom = customPadding, start = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (disabled) {
            Icon(imageVector = icon, "Drawer item", tint = Color(0xFFC9C9C9))
            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f), text = name,
                style = TextStyle(fontWeight = FontWeight.Bold, color = Color(0xFFC9C9C9))
            )
            if (externalLink) {
                Box(contentAlignment = Alignment.CenterEnd) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        "",
                        tint = Color(0xFFC9C9C9)
                    )
                }
            }
        } else {
            Icon(imageVector = icon, "Drawer item", tint = color)
            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f), text = name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = if (selected == route) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.Black
                )
            )
            if (externalLink) {
                Box(contentAlignment = Alignment.CenterEnd) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        "",
                        tint = color
                    )
                }
            }
        }
    }

}

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
            "Ferientraining",
            Icons.Outlined.BeachAccess,
            selected = selectedMain,
            route = NavigationDrawerRoutes.FERIEN,
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
        DrawerItem(
            "Vorführungen",
            Icons.Outlined.RemoveRedEye,
            selected = selectedMain,
            route = NavigationDrawerRoutes.VORFUEHRUNGEN,
            onClick = onClickMain,
        )
        DrawerItem(
            "Lehrgänge + Turniere",
            Icons.Outlined.People,
            selected = selectedMain,
            route = NavigationDrawerRoutes.LEHRGAENGE,
            onClick = onClickMain,
        )
        Divider()
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
        Divider()
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
        Divider(modifier = Modifier.padding(bottom = 8.dp))
        Image(
            painter = painterResource(id = R.drawable.jetpackcompose_logo),
            contentDescription = "Jetpack Compose logo",
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    showDebugInfo.value = true
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = listState.layoutInfo.totalItemsCount)
                    }
                }
        )
        Box(modifier = Modifier.size(8.dp))
    }
}