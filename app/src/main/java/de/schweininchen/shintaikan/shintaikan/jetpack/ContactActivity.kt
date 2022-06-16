package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

                    val emailText = remember { mutableStateOf("") }
                    val subjectText = remember { mutableStateOf("") }
                    val messageText = remember { mutableStateOf("") }

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
                                .padding(innerPadding)
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            OutlinedTextField(
                                value = emailText.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                label = { Text("E-Mail") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.AlternateEmail,
                                        contentDescription = "Email Icon"
                                    )
                                },
                                isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.value)
                                    .matches(),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xff00c600)
                                ),
                                onValueChange = { text -> emailText.value = text }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                value = subjectText.value,
                                label = { Text("Betreff") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Subject,
                                        contentDescription = "Subject Icon"
                                    )
                                },
                                onValueChange = { text -> subjectText.value = text }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                value = messageText.value,
                                label = { Text("Nachricht") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Message Icon"
                                    )
                                },
                                onValueChange = { text -> messageText.value = text }
                            )
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