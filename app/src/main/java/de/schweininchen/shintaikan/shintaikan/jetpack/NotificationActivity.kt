package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.NotificationImportant
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.WarningAmber
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
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class NotificationActivity : AppCompatActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val context = LocalContext.current

            val scope = rememberCoroutineScope()

            val scrollBehavior =
                TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

            var importantNotificationsChcked by remember { mutableStateOf(false) }
            var autoNotificationsChcked by remember { mutableStateOf(false) }

            var permissionGranted by remember { mutableStateOf(getPermissionGranted(context)) }

            ShintaikanJetpackTheme {
                Scaffold(
                    topBar = {
                        AppBar(scope, scrollBehavior)
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) { innerPadding ->
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
                            enabled = permissionGranted
                        ) {
                            importantNotificationsChcked = !importantNotificationsChcked
                        }
                        HorizontalDivider()
                        MySwitchPreference(
                            title = "Auto Benachrichtigungen",
                            description = "Benachrichtigungen, die automatisch durch neue Posts auf der Homepage gesendet werden",
                            Icons.Outlined.Notifications,
                            checked = autoNotificationsChcked,
                            enabled = permissionGranted
                        ) {
                            autoNotificationsChcked = !autoNotificationsChcked
                        }
                        if (permissionGranted) {
                            NotificationCard(
                                null,
                                "Berechtigung für Benachichtigungen gewährt",
                                Icons.Outlined.CheckCircleOutline,
                                Color(0xFF4CAF50)
                            )
                        } else {
                            NotificationCard(
                                "Beheben",
                                getString(R.string.push_problem),
                                Icons.Outlined.WarningAmber,
                                Color(0xFFFFC107)
                            ) {
                                askNotificationPermission(requestPermissionLauncher, context)
                            }
                        }
                    }
                }

            }
        }

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
