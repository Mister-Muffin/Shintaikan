package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavOptionsBuilder
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.Destinations.*
import kotlinx.coroutines.launch

val customPadding = 12.dp

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@Composable
fun ColumnScope.DrawerContent(
    currentSelection: Destinations,
    navigate: (route: String, builder: NavOptionsBuilder.() -> Unit) -> Unit,
    closeDrawer: () -> Unit,
    firebaseMessagingToken: String
) {
    val context = LocalContext.current

    val showDebugInfo = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val dialogsOpen = remember {
        mutableStateMapOf(
            MORE to false,
            ABOUT to false
        )
    }

    val aboutDialogOpen = remember { mutableStateOf(false) }

    val moreDialogOpen = remember { mutableStateOf(false) }

    if (dialogsOpen[MORE] == true) CustomAlertDialog(
        title = "Weiteres",
        text = "Was Rüdiger noch sagen wollte:\nTiefer stehen, schneller schlagen! :)"
    ) {
        dialogsOpen[MORE] = false
    }
    if (dialogsOpen[ABOUT] == true) AboutAlertDialog { dialogsOpen[ABOUT] = false }

    val drawerDestinations = listOf(
        HOME,
        TRPLAN,
        PRUEFUNGEN,
        FERIEN,
        NACHSOFE,
        CLUBWEG,
        ANFAENGER,
        VORFUEHRUNGEN,
        LEHRGAENGE,
        NONE,
        IMPRESSUM,
        PRIVACY,
        MORE,
        ABOUT,
        NONE
    )

    val uriHandler = LocalUriHandler.current

    @Composable
    fun NavDivider(modifier: Modifier = Modifier) = Divider(modifier.padding(horizontal = 24.dp))

    LazyColumn(
        state = listState,
        modifier = Modifier
            .statusBarsPadding()
            .imePadding()
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        item { Box(modifier = Modifier.height(16.dp)) }
        item {
            Image(
                painter = painterResource(id = R.drawable.pelli),
                contentDescription = "Shintaikan logo",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .size(120.dp)
                    .padding(bottom = 8.dp)
            )
        }
        items(drawerDestinations) { destination ->
            if (destination == NONE) {
                NavDivider()
            } else {
                NavigationDrawerItem(
                    label = { Text(destination.navName) },
                    selected = currentSelection == destination,
                    onClick = {
                        destination.call(navigate, closeDrawer, uriHandler::openUri) {
                            dialogsOpen[it] = true
                        }
                    },
                    icon = destination.icon?.let {
                        {
                            Icon(
                                imageVector = it,
                                contentDescription = it.name
                            )
                        }
                    },
                    badge = destination.uri?.let {
                        {
                            Icon(
                                imageVector = Icons.Outlined.OpenInBrowser,
                                contentDescription = "Open external link in Browser"
                            )
                        }
                    }
                )
            }
        }
        if (showDebugInfo.value) {
            item {
                DebugInfo(firebaseMessagingToken = firebaseMessagingToken)
                Box(modifier = Modifier.size(8.dp))
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.jetpackcompose_logo),
                    contentDescription = "Jetpack Compose logo",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clip(CircleShape)
                        .combinedClickable(
                            onLongClick = {
                                COLORS.call(navigate, closeDrawer, null, null)
                            },
                            onClick = {
                                showDebugInfo.value = true
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index = listState.layoutInfo.totalItemsCount)
                                }
                            }
                        )
                )
            }
        }
        item { Box(modifier = Modifier.height(16.dp)) }
    }
}


@Composable
fun DebugInfo(firebaseMessagingToken: String) {
    Column {
        val context = LocalContext.current
        Text(
            text = BuildConfig.VERSION_CODE.toString(),
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = firebaseMessagingToken,
            style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 10.sp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val clip = ClipData.newPlainText("token", firebaseMessagingToken)
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
fun CustomAlertDialog(title: String, text: String, onDissmiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDissmiss,
        title = { if (title.isNotEmpty()) Text(text = title) },
        icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "Info icon") },
        text = {
            Text(
                text = text,
                style = TextStyle(color = Color.Black),
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                /*modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),*/
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

    val gitHubButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.DarkGray,
    )

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
                androidx.compose.material3.Button(
                    onClick = {
                        linkToWebpage(
                            "https://github.com/Mister-Muffin/Shintaikan",
                            context = context
                        )
                    },
                    colors = gitHubButtonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                {
                    Text(text = "GitHub", color = Color.White)
                }
            }
        },
        dismissButton = {
            androidx.compose.material3.OutlinedButton(
                onClick = onDissmiss
            ) {
                Text("Close")
            }

        },
        confirmButton = {
            androidx.compose.material3.Button(
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
        }
    )

}

fun linkToWebpage(uri: String, context: Context) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(uri)
    startActivity(context, openURL, null)
}