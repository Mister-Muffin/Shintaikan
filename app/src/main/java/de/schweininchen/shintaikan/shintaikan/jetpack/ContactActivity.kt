package de.schweininchen.shintaikan.shintaikan.jetpack

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class ContactActivity : AppCompatActivity() {
    private lateinit var functions: FirebaseFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                val scope = rememberCoroutineScope()

                ShintaikanJetpackTheme {

                    functions = Firebase.functions("europe-west1")

                    var emailText by remember { mutableStateOf("") }
                    var subjectText by remember { mutableStateOf("") }
                    var messageText by remember { mutableStateOf("") }

                    var sendSuccesful by remember { mutableStateOf(false) }
                    var sendPending by remember { mutableStateOf(false) }
                    var sendFailed by remember { mutableStateOf(false) }
                    var sendErrorCode by remember { mutableStateOf("") }
                    var sendErrorDetails by remember { mutableStateOf("") }

                    var networkConnected by remember { mutableStateOf(true) }

                    val appBarTitle = "Kontakt & Feedback"
                    val scrollBehavior =
                        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

                    abc(LocalContext.current) { isConnected ->
                        networkConnected = isConnected
                    }

                    Scaffold(
                        topBar = {
                            ContactAppBar(
                                appBarTitle,
                                scope,
                                scrollBehavior = scrollBehavior,
                                networkConnected = networkConnected
                            ) {
                                sendPending = true
                                sendSuccesful = false
                                sendFailed = false
                                sendMessage(emailText, subjectText, messageText)
                                    .addOnCompleteListener { task ->
                                        sendPending = false
                                        sendSuccesful = task.isSuccessful
                                        if (!task.isSuccessful) {
                                            sendFailed = true
                                            val e = task.exception
                                            if (e is FirebaseFunctionsException) {
                                                sendErrorCode = e.code.toString()
                                                sendErrorDetails = e.details.toString()
                                            }
                                        }
                                    }
                            }
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
                                value = emailText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                label = { Text("E-Mail") },
                                enabled = !sendPending,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.AlternateEmail,
                                        contentDescription = "Email Icon"
                                    )
                                },
                                isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText)
                                    .matches(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xff00c600),
                                ),
                                onValueChange = { text -> emailText = text }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                value = subjectText,
                                label = { Text("Betreff") },
                                enabled = !sendPending,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Title,
                                        contentDescription = "Subject Icon"
                                    )
                                },
                                singleLine = true,
                                onValueChange = { text -> subjectText = text }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                value = messageText,
                                label = { Text("Nachricht") },
                                enabled = !sendPending,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Message Icon"
                                    )
                                },
                                onValueChange = { text -> messageText = text }
                            )
                            if (sendPending && !sendSuccesful && !sendFailed) { // Message sending
                                CircularProgressIndicator()
                            } else if (sendSuccesful && !sendFailed) { // Message sent successfully
                                Icon(
                                    imageVector = Icons.Outlined.Done,
                                    contentDescription = "Done Icon",
                                    tint = Color.Green,
                                    modifier = Modifier.size(50.dp)
                                )
                                Text("Die Nachicht wurde erfolgreich versendet!")
                            } else if (!sendSuccesful && sendFailed) { // Message send failed
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = "Error Icon",
                                    tint = Color.Red,
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(sendErrorCode)
                                Text(sendErrorDetails)
                            }
                        }
                    }

                }
            }
        }

    }

    private fun sendMessage(email: String, subject: String, message: String): Task<String> {
        val data = hashMapOf(
            "email" to email,
            "subject" to subject,
            "message" to message,
            "debug" to (BuildConfig.BUILD_TYPE == "debug")
        )

        return functions
            .getHttpsCallable("sendEmail")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data.toString()
                result
            }
    }

    private fun abc(context: Context, updateNetworkStatus: (Boolean) -> Unit) {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(
                mNetworkCallback(
                    updateNetworkStatus
                )
            )
            updateNetworkStatus(cm.activeNetwork !== null)
        }

    }

    private fun mNetworkCallback(
        updateNetworkStatus: (Boolean) -> Unit
    ): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateNetworkStatus(true)
            }

            override fun onLost(network: Network) {
                updateNetworkStatus(false)
            }
        }
    }


    @ExperimentalMaterial3Api
    @Composable
    fun ContactAppBar(
        appBarTitle: String,
        scope: CoroutineScope,
        scrollBehavior: TopAppBarScrollBehavior,
        networkConnected: Boolean,
        send: () -> Unit,
    ) {
        val backgroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors()
        Box(
            // TODO: modifier = Modifier.background(backgroundColors.containerColor(scrollFraction = scrollBehavior.scrollFraction).value)
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
                    IconButton(onClick = { send() }, enabled = networkConnected) {
                        Icon(Icons.Outlined.Send, contentDescription = "Send")
                    }
                }
            )
        }
    }
}
