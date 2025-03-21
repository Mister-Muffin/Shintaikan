package de.schweininchen.shintaikan.shintaikan.jetpack.components.navDrawer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import de.schweininchen.shintaikan.shintaikan.jetpack.BuildConfig
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@ExperimentalMaterial3Api
@Composable
fun DrawerContent(
    vm: MyViewModel,
    selectedMain: NavigationDrawerRoutes,
    remoteConfig: FirebaseRemoteConfig,
    onClickMain: (NavigationDrawerRoutes?) -> Unit
) {
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
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.pelli),
                contentDescription = "Shintaikan logo",
                Modifier
                    .fillMaxWidth()
                    .size(120.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            DrawerItems(
                selectedMain, onClickMain, openCustomDialog,
                openDialog, showDebugInfo, coroutineScope, listState,
                remoteConfig
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
            TextButton(onClick = onDissmiss) {
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
                Button(
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
            OutlinedButton(onClick = onDissmiss) { Text("Close") }

        },
        confirmButton = {
            Button(
                onClick = {
                    context.startActivity(
                        Intent(context, OssLicensesMenuActivity::class.java)
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
    openURL.data = uri.toUri()
    context.startActivity(openURL, null)
}
