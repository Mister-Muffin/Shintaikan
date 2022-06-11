package de.schweininchen.shintaikan.shintaikan.jetpack

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.material3.MenuDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer.DrawerItems

val customPadding = 12.dp

@ExperimentalMaterial3Api
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
            title = "Weiteres",
            text = "Was Rüdiger noch sagen wollte:\nTiefer stehen, schneller schlagen! :)"
        ) {
            openCustomDialog.value = false
        }

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsWithImePadding()
                .fillMaxWidth()
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.pelli),
                    contentDescription = "Shintaikan logo",
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .size(120.dp)
                        .padding(top = 8.dp)
                )
                Divider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outline
                )
                DrawerItems(
                    selectedMain, onClickMain, context, openCustomDialog,
                    openDialog, showDebugInfo, coroutineScope, listState
                )
            }
            if (showDebugInfo.value) {
                item {
                    DebugInfo(vm = vm)
                    Box(modifier = Modifier.size(8.dp))
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
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = vm.firebaseMessagingToken.value,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                fontStyle = MaterialTheme.typography.bodySmall.fontStyle
            ),
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
fun CustomAlertDialog(title: String, text: String, onDissmiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
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

    androidx.compose.material3.AlertDialog(
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