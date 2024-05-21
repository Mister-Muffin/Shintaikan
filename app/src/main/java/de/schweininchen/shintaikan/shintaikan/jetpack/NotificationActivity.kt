package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.material.icons.outlined.NotificationImportant
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
                            title = "Testtile",
                            description = "Test description",
                            checked = importantNotificationsChcked
                        ) {
                            importantNotificationsChcked = it
                        }
                        HorizontalDivider()
                        MySwitchPreference(
                            title = "Auto Benachrichtigungen",
                            checked = autoNotificationsChcked
                        ) {
                            autoNotificationsChcked = it
                        }
                    }
                }

            }
        }

    }

    @Composable
    fun MySwitchPreference(
        title: String,
        description: String? = null,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.NotificationImportant,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Start,
                    )
                    if (description != null) {
                        Text(
                            text = "Erhalte Benachrichtigungen",
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
                onCheckedChange = onCheckedChange
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
