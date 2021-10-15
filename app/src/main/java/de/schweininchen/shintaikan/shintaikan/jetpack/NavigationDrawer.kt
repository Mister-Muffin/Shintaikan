package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.LightBlue800
import kotlinx.coroutines.launch


val customPadding = 12.dp

@Composable
fun drawerContent(
    vm: MyViewModel,
    selectedMain: NavigationDrawerRoutes,
    onClickMain: (NavigationDrawerRoutes?) -> Unit
): @Composable (ColumnScope.() -> Unit) =
    {
        val context = LocalContext.current

        val showDebugInfo = remember { mutableStateOf(false) }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        val openDialog = remember { mutableStateOf(false) }
        val openCustomDialog = remember { mutableStateOf(false) }

        if (openDialog.value) AboutAlertDialog { openDialog.value = false }
        if (openCustomDialog.value) CustomAlertDialog(
            title = "",
            text = "Was Rüdiger noch sagen wollte:\nTiefer stehen, schneller schlagen! :)"
        ) {
            openCustomDialog.value = false
        }

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.pelli),
                    contentDescription = "Shintaikan logo",
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .size(120.dp)
                )
                Divider(modifier = Modifier.padding(bottom = 4.dp))
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
                }
            }
            if (showDebugInfo.value) {
                item {
                    DebugInfo(vm = vm)
                }
            }
        }
    }

@Composable
fun DebugInfo(vm: MyViewModel) {
    Column {
        val context = LocalContext.current
        Text(
            text = BuildConfig.VERSION_CODE.toString(),
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = vm.firebaseMessagingToken.value,
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val clip = ClipData.newPlainText("token", vm.firebaseMessagingToken.value)
                    getSystemService(
                        context,
                        ClipboardManager::class.java
                    )?.setPrimaryClip(clip)
                    Toast
                        .makeText(context, "copied!", Toast.LENGTH_LONG)
                        .show()
                },
            textAlign = TextAlign.Center
        )
    }
}


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
        if (selected == route && !disabled) MaterialTheme.colors.primary else Color(0xFF898989)

    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = { onClick(route) })
            .background(color = if (selected == route) MaterialTheme.colors.primary.copy(alpha = .15f) else Color.Transparent)
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
                    color = if (selected == route) MaterialTheme.colors.primary else Color.Black
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
fun CustomAlertDialog(title: String, text: String, onDissmiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDissmiss,
        title = { if (title.isNotEmpty()) Text(text = title) },
        text = {
            Text(
                text = text,
                style = TextStyle(color = Color.Black),
                textAlign = TextAlign.Start
            )
        },
        buttons = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = onDissmiss

            ) {
                Text("Ok")
            }
        }
    )

}

@Composable
fun AboutAlertDialog(onDissmiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDissmiss,
        title = {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.pelli),
                    contentDescription = "Shintaikan logo",
                    modifier = Modifier
                        .width(60.dp)
                        .padding(end = 8.dp),
                    alignment = Alignment.Center
                )
                Column {
                    Text(text = "Shintaikan")
                    Text(text = "Version ${BuildConfig.VERSION_NAME}")
                }
            }
        },
        text = {
            Column {
                Button(
                    onClick = {
                        linkToWebpage(
                            "https://github.com/Mister-Muffin/Shintaikan",
                            context = context
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                {
                    Text(text = "GitHub", color = Color.White)
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    border = BorderStroke(1.dp, LightBlue800),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LightBlue800),
                    onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                OssLicensesMenuActivity::class.java
                            )
                        )
                    }

                ) {
                    Text("View licenses")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = onDissmiss

                ) {
                    Text("Close")
                }
            }
        }
    )

}

fun linkToWebpage(uri: String, context: Context) {
    //val context = ContextAmbient.current
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(uri)
    startActivity(context, openURL, null)

}