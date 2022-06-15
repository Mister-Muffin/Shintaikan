package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                val scope = rememberCoroutineScope()

                ShintaikanJetpackTheme {

                    val appBarTitle = "Kontakt & Feedback"
                    val scrollBehavior =
                        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

                    Scaffold(
                        topBar = {
                            ContactAppBar(
                                appBarTitle,
                                scope,
                                scrollBehavior = scrollBehavior
                            )
                        },
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .navigationBarsWithImePadding()
                                .padding(innerPadding),
                        ) {

                        }
                    }

                }
            }
        }

    }
}

@ExperimentalMaterial3Api
@Composable
fun ContactAppBar(
    appBarTitle: String,
    scope: CoroutineScope,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    Box(
        modifier = Modifier.background(backgroundColors.containerColor(scrollFraction = scrollBehavior.scrollFraction).value)
    ) {
        val activity = (LocalContext.current as? Activity)
        CenterAlignedTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    appBarTitle, fontSize = 20.sp //TODO: Make global style?
                )
            },

            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        activity?.finish() //
                    }
                }
                ) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Outlined.Send, contentDescription = "Send")
                }
            }
        )
    }
}