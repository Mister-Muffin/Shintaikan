package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.text.HtmlCompat
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.activities.NotificationActivity
import de.schweininchen.shintaikan.shintaikan.jetpack.components.home.Today
import de.schweininchen.shintaikan.shintaikan.jetpack.util.getPermissionGranted
import de.schweininchen.shintaikan.shintaikan.jetpack.util.toColorMatrix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    postsList: List<Array<String>>,
    viewModel: MyViewModel
) {
    // Credits to Mr-Pine
    // Source: https://github.com/Mr-Pine/XKCDFeed/tree/9f4b95307822062ed74e251f2ac00d55d6d4d26b
    val colorFilter = ColorFilter.colorMatrix(ColorMatrix(viewModel.matrix.toColorMatrix()))

    val context = LocalContext.current

    val a = context as Activity
    val sharedPreferences = a.getPreferences(Activity.MODE_PRIVATE)

    val imageSize = 100.dp

    val pushCardKey = "discard_card_push_key"
    var showInfoCard by remember {
        // TODO: Enable when push notifications working in Wordpress
        // mutableStateOf(sharedPreferences.getBoolean(pushCardKey, true))
        mutableStateOf(false)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
        state = viewModel.lazyStateStart
    ) {
        item {
            Text(
                text = "Karate Club\nShintaikan e.V.",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            Text(
                text = "Linnéstraße 14, Freiburg West",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.pelli),
                contentDescription = "Shintaikan logo",
                Modifier.size(200.dp)
            )
        }

        item {
            Today(viewModel)
        }

        if (showInfoCard && !getPermissionGranted(Manifest.permission.POST_NOTIFICATIONS, context))
            item {
                Card(
                    colors = CardDefaults.cardColors()
                        .copy(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Pushbenachichtigungen"
                        )
                        Box(Modifier.padding(top = 8.dp)) {
                            Text(
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                text = stringResource(R.string.home_push_card_content)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = {
                                Intent(
                                    context,
                                    NotificationActivity::class.java
                                ).also { context.startActivity(it) }
                            }) {
                                Text(text = "Einstellungen")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = {
                                sharedPreferences.edit() { putBoolean(pushCardKey, false) }
                                showInfoCard = false
                            }) {
                                Text(text = "Verwerfen")
                            }
                        }
                    }
                }
            }

        if (!viewModel.isConnected.value && postsList.isEmpty()) {
            item {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_sad_24dp),
                    contentDescription = "",
                    tint = Color.Red, modifier = Modifier.size(100.dp)
                )
                Text(
                    text = "Wolki ist traurig,\nweil keine Internetverbindung besteht :(",
                    style = TextStyle(fontSize = 15.sp), textAlign = TextAlign.Center
                )
            }
        } else if (postsList.isEmpty()) {
            item {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
            }
        } else {
            items(postsList) { post ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = post[0]
                        )
                        Box(Modifier.padding(top = 8.dp)) {
                            if (post[1].isEmpty()) Html("Kein Inhalt")
                            else Html(post[1].ifEmpty { "Kein Inhalt" })
                        }
                    }
                }
            }
        }
        item {
            Row(Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.icon_karate),
                    contentDescription = null,
                    modifier = Modifier
                        .width(imageSize)
                        .height(imageSize),
                    colorFilter = if (!isSystemInDarkTheme()) null else colorFilter
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_karate_2),
                    contentDescription = null,
                    modifier = Modifier
                        .width(imageSize)
                        .height(imageSize),
                    colorFilter = if (!isSystemInDarkTheme()) null else colorFilter
                )
            }

        }
        item {
            Image(
                painter = painterResource(id = R.drawable.zeichen_kara),
                contentDescription = "kara",
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize),
                colorFilter = if (!isSystemInDarkTheme()) null else colorFilter
            )
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.zeichen_te),
                contentDescription = "te",
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize),
                colorFilter = if (!isSystemInDarkTheme()) null else colorFilter
            )
        }
        item {
            Text("Kontakt:")
            Text("shintaikan@web.de")
        }

    }
}

@Composable
fun Html(text: String) {
    Text(
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim(),
        style = MaterialTheme.typography.bodyMedium
    )
}
