package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.LightBlue800
import kotlinx.coroutines.launch

val customPadding = 15.dp

@Composable
fun drawerContent(onClick: (String) -> Unit): @Composable (ColumnScope.() -> Unit) =
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
                Divider()
                Column {
                    DrawerItem("Start", Icons.Outlined.Info) { onClick("Home") }
                    DrawerItem("Trainingsplan", Icons.Outlined.DateRange) { onClick("Trplan") }
                    DrawerItem(
                        "Gürtelprüfungen",
                        Icons.Outlined.NorthEast
                    ) { onClick("Pruefungen") }
                    DrawerItem("Ferientraining", Icons.Outlined.BeachAccess) { onClick("Ferien") }
                    DrawerItem(
                        "Nach den Sommerferien",
                        Icons.Outlined.WbSunny
                    ) { onClick("NachSoFe") }
                    DrawerItem(
                        "Der Club / Wegbeschreibung",
                        Icons.Outlined.Home
                    ) { onClick("ClubWeg") }
                    DrawerItem(
                        "Anfänger / Interressenten",
                        Icons.Outlined.DirectionsWalk
                    ) { onClick("Anfaenger") }
                    DrawerItem(
                        "Vorführungen",
                        Icons.Outlined.RemoveRedEye
                    ) { onClick("Vorfuehrungen") }
                    DrawerItem(
                        "Lehrgänge + Turniere",
                        Icons.Outlined.People
                    ) { onClick("Lehrgaenge") }
                    Divider()
                    DrawerItem(
                        "Infofilmchen",
                        Icons.Outlined.Movie
                    ) { onClick("Lehrgaenge") }
                    DrawerItem(
                        "Seefest 2019",
                        Icons.Outlined.Movie
                    ) { onClick("Lehrgaenge") }
                    DrawerItem(
                        "Mixfilm 2019",
                        Icons.Outlined.Movie
                    ) { onClick("Lehrgaenge") }
                    Divider()
                    DrawerItem(
                        "Kontakt und Feedback",
                        Icons.Outlined.Mail,
                    ) { onClick("Lehrgaenge") }
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
                        Icons.Outlined.Info
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
                    DebugInfo()
                }
            }
        }
    }

@Composable
fun DebugInfo() {
    Text(
        text = BuildConfig.VERSION_CODE.toString(),
        style = TextStyle(fontWeight = FontWeight.Bold),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}


@Composable
private fun DrawerItem(
    name: String, icon: ImageVector, externalLink: Boolean = false, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(top = customPadding, bottom = customPadding, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, "Drawer item", tint = Color(0xFF898989))
        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .weight(1f), text = name,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        if (externalLink) {
            Box(contentAlignment = Alignment.CenterEnd) {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    "",
                    tint = Color(0xFF898989)
                )
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
                    Text(text = "Version ${BuildConfig.VERSION_CODE}")
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