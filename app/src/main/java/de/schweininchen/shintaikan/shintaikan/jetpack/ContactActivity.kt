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

                    val emailText = remember { mutableStateOf("") }
                    val subjectText = remember { mutableStateOf("") }
                    val messageText = remember { mutableStateOf("") }

                    val sendSuccesful = remember { mutableStateOf(false) }
                    val sendPending = remember { mutableStateOf(false) }
                    val sendFailed = remember { mutableStateOf(false) }
                    val sendErrorCode = remember { mutableStateOf("") }
                    val sendErrorDetails = remember { mutableStateOf("") }

                    val appBarTitle = "Kontakt & Feedback"
                    val scrollBehavior =
                        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())


                    Scaffold(
                        topBar = {
                            ContactAppBar(
                                appBarTitle,
                                scope,
                                scrollBehavior = scrollBehavior
                            ) {
                                sendPending.value = true
                                sendSuccesful.value = false
                                sendFailed.value = false
                                sendMessage(emailText.value, subjectText.value, messageText.value)
                                    .addOnCompleteListener { task ->
                                        sendPending.value = false
                                        sendSuccesful.value = task.isSuccessful
                                        if (!task.isSuccessful) {
                                            sendFailed.value = true
                                            val e = task.exception
                                            if (e is FirebaseFunctionsException) {
                                                sendErrorCode.value = e.code.toString()
                                                sendErrorDetails.value = e.details.toString()
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
                                value = emailText.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                label = { Text("E-Mail") },
                                enabled = !sendPending.value,
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
                                enabled = !sendPending.value,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Subject,
                                        contentDescription = "Subject Icon"
                                    )
                                },
                                singleLine = true,
                                onValueChange = { text -> subjectText.value = text }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                value = messageText.value,
                                label = { Text("Nachricht") },
                                enabled = !sendPending.value,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Message Icon"
                                    )
                                },
                                onValueChange = { text -> messageText.value = text }
                            )
                            if (sendPending.value && !sendSuccesful.value && !sendFailed.value) { // Message sending
                                CircularProgressIndicator()
                            } else if (sendSuccesful.value && !sendFailed.value) { // Message sent successfully
                                Icon(
                                    imageVector = Icons.Outlined.Done,
                                    contentDescription = "Done Icon"
                                )
                            } else if (!sendSuccesful.value && sendFailed.value) { // Message send failed
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = "Error Icon",
                                    tint = Color.Red,
                                )
                                Text(sendErrorCode.value)
                                Text(sendErrorDetails.value)
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
            "message" to message
        )

        return functions
            .getHttpsCallable("sendEmail")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

    @ExperimentalMaterial3Api
    @Composable
    fun ContactAppBar(
        appBarTitle: String,
        scope: CoroutineScope,
        scrollBehavior: TopAppBarScrollBehavior,
        send: () -> Unit,
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
                    IconButton(onClick = { send() }) {
                        Icon(Icons.Outlined.Send, contentDescription = "Send")
                    }
                }
            )
        }
    }
}
