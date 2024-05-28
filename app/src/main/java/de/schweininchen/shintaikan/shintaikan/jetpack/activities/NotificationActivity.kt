package de.schweininchen.shintaikan.shintaikan.jetpack.activities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NotificationImportant
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class NotificationActivity : AppCompatActivity() {
    // Track the number of times the user has requested permission and the dialog was shown
    // in order to display the LOCKED status, when the permission is denied twice
    // and can't be requested again
    private val sharedPrefsRequestCountKey = "request_count"

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()


            val sharedPrefs = remember {
                val activity = context as Activity
                return@remember activity.getPreferences(Context.MODE_PRIVATE)
            }

            // This is only necessary for API level >= 33 (TIRAMISU)
            val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                null
            }
            val requestCount = sharedPrefs.getInt(sharedPrefsRequestCountKey, 0)
            val permissionStatus =
                if (notificationPermissionState == null) {
                    PermissionStatus.GRANTED
                } else if (notificationPermissionState.status.isGranted) {
                    PermissionStatus.GRANTED
                } else {
                    if (requestCount < 2) PermissionStatus.DENIED
                    else PermissionStatus.LOCKED
                }

            val scrollBehavior =
                TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

            var showPermissionDialog by remember { mutableStateOf(false) }

            var importantNotificationsChcked by remember { mutableStateOf(false) }
            var autoNotificationsChcked by remember { mutableStateOf(false) }

            ShintaikanJetpackTheme {
                Scaffold(
                    topBar = {
                        AppBar(scope, scrollBehavior)
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) { innerPadding ->
                    if (showPermissionDialog) PermissionDialog({
                        showPermissionDialog = false
                        notificationPermissionState?.launchPermissionRequest()
                        increaseRequestCount(sharedPrefs)
                    }, { showPermissionDialog = false })
                    Column(
                        modifier = Modifier
                            .imePadding()
                            .padding(innerPadding)
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MySwitchPreference(
                            title = "Wichtige Benachichtigungen",
                            description = "Benachichtigungen, die von uns manuell gesendet werden",
                            Icons.Outlined.NotificationImportant,
                            checked = importantNotificationsChcked,
                            enabled = permissionStatus.equals(PermissionStatus.GRANTED)
                        ) {
                            importantNotificationsChcked = !importantNotificationsChcked
                        }
                        HorizontalDivider()
                        MySwitchPreference(
                            title = "Auto Benachrichtigungen",
                            description = "Benachrichtigungen, die automatisch durch neue Posts auf der Homepage gesendet werden",
                            Icons.Outlined.Notifications,
                            checked = autoNotificationsChcked,
                            enabled = permissionStatus.equals(PermissionStatus.GRANTED)
                        ) {
                            autoNotificationsChcked = !autoNotificationsChcked
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            when (permissionStatus) {
                                PermissionStatus.GRANTED -> {
                                    NotificationCard(
                                        null,
                                        "Berechtigung für Benachichtigungen gewährt",
                                        Icons.Outlined.CheckCircleOutline,
                                        Color(0xFF4CAF50)
                                    )
                                }

                                PermissionStatus.DENIED -> {
                                    NotificationCard(
                                        "Beheben",
                                        getString(R.string.push_problem),
                                        Icons.Outlined.WarningAmber,
                                        Color(0xFFFFC107)
                                    ) {
                                        if (requestCount == 0) {
                                            notificationPermissionState?.launchPermissionRequest()
                                            increaseRequestCount(sharedPrefs)
                                        } else {
                                            showPermissionDialog = true
                                        }
                                    }
                                }

                                PermissionStatus.LOCKED -> {
                                    NotificationCard(
                                        "Beheben",
                                        getString(R.string.push_locked),
                                        Icons.Outlined.Block,
                                        Color(0xFFF44336)
                                    ) {}
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    enum class PermissionStatus {
        GRANTED,
        DENIED,
        LOCKED
    }

    private fun increaseRequestCount(sharedPreferences: SharedPreferences) {
        val rq = sharedPreferences.getInt(sharedPrefsRequestCountKey, 0)
        sharedPreferences.edit().putInt(sharedPrefsRequestCountKey, rq + 1).apply()
    }

    @Composable
    fun PermissionDialog(onConfirm: () -> Unit, onDissmiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDissmiss,
            title = { Text("Benachichtigungen erlauben?") },
            icon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Lock icon") },
            text = {
                Text(
                    text = getString(R.string.push_dialog_text)
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Erlauben")
                }
            },
            dismissButton = {
                TextButton(onClick = onDissmiss) {
                    Text("Ablehnen")
                }
            }
        )

    }

    @Composable
    private fun NotificationCard(
        text: String?,
        description: String,
        icon: ImageVector,
        tint: Color,
        onClick: () -> Unit = {}
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon",
                        tint = tint
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(.75f)
                    ) {
                        Text(
                            description,
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (!text.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.padding(4.dp))
                            Button(onClick = onClick) {
                                Text(text)
                            }
                        }
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0x00000000)
                    )
                }
            }
        }
    }

    @Composable
    fun MySwitchPreference(
        title: String,
        description: String? = null,
        icon: ImageVector,
        checked: Boolean,
        enabled: Boolean = true,
        onCheckedChange: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.clickable {
                if (!enabled) return@clickable
                onCheckedChange()
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(.8f)
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Start,
                    )
                    if (description != null) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(
                                vertical = 4.dp
                            ),
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                enabled = enabled,
                onCheckedChange = { onCheckedChange() }
            )
        }
    }


    @ExperimentalMaterial3Api
    @Composable
    fun AppBar(
        scope: CoroutineScope,
        scrollBehavior: TopAppBarScrollBehavior,
    ) {
        Box {
            val activity = LocalContext.current as Activity
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                scrollBehavior = scrollBehavior,
                title = {
                    Text("Benachichtigungen")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { activity.finish() }
                    }
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    }
}
