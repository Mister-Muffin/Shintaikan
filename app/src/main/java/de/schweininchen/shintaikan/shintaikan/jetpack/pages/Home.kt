package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography
import java.time.LocalDate
import java.time.LocalTime

// TODO: Fix terrible Font

@ExperimentalMaterial3Api
@Composable
fun Home(
    postsList: List<Array<String>>,
    lazyListState: LazyListState,
    firestoreDataNotEmpty: Boolean,
    trplanData: Map<String, MutableMap<String, Any>>,
    isConnected: Boolean
) {
    val imageSize = 100.dp

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth(),
        state = lazyListState
    ) {
        item {
            Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        }
        item {
            Text(text = "Linnéstraße 14, Freiburg West", style = Typography.h2)
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.pelli),
                contentDescription = "Shintaikan logo",
                Modifier.size(200.dp)
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && firestoreDataNotEmpty) {
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Today(trplanData)
                }
            }
        }

        if (!isConnected && postsList.isEmpty()) {
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
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(8.dp)) {
                        Text(
                            style = Typography.h3,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = post[0]
                        )
                        Box(Modifier.padding(top = 8.dp)) {
                            Html(text = post[1])
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
                        .height(imageSize)
                )
                Image(
                    painter = painterResource(id = R.drawable.icon_karate_2),
                    contentDescription = null,
                    modifier = Modifier
                        .width(imageSize)
                        .height(imageSize)
                )
            }

        }
        item {
            Image(
                painter = painterResource(id = R.drawable.zeichen_kara),
                contentDescription = "kara",
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize)
            )
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.zeichen_te),
                contentDescription = "te",
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize)
            )
        }
        item {
            Text("Kontakt:")
            val uriHandler = LocalUriHandler.current
            Text("shintaikan@web.de", modifier = Modifier.clickable { uriHandler.openUri("mailto:shintaikan@web.de") })
        }

    }
}

@Composable
fun Html(text: String) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            this.setTextColor(
                android.graphics.Color.rgb(
                    (Typography.h3.color.red * 255).toInt(),
                    (Typography.h3.color.green * 255).toInt(),
                    (Typography.h3.color.blue * 255).toInt()
                )
            )
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
        }
    },
        update = {
            it.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Today(trplanData: Map<String, MutableMap<String, Any>>) {

    val target: LocalTime = LocalTime.now()
    val targetInZone = (target.isBefore(LocalTime.parse("20:00:00"))
            &&
            target.isAfter(LocalTime.parse("06:00:00")))

    val dayWord = LocalDate.now().dayOfWeek.getDisplayName(
        java.time.format.TextStyle.FULL,
        java.util.Locale.getDefault()
    )
    val day = LocalDate.now().dayOfWeek.value

    if (!targetInZone || day > 5) { // return if in between times
        return
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Heute, $dayWord",
            style = Typography.h2,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        for (j in trplanData.keys) {
            //Log.d("TAG", "Today: $j")
            if (!trplanData[j].isNullOrEmpty() &&
                trplanData[j]?.get("start")
                    .toString().isNotEmpty() &&
                trplanData[j]?.get("key").toString()
                    .startsWith(day.toString())
            ) {
                if (trplanData[j]?.get("group")
                        .toString() == "Benutzerdefiniert"
                ) {
                    Text(
                        text = "${trplanData[j]?.get("start").toString()} - " +
                                "${trplanData[j]?.get("end").toString()}: " +
                                trplanData[j]?.get("customText").toString(),
                        style = Typography.body2
                    )
                } else {
                    Text(
                        text = "${trplanData[j]?.get("start").toString()} - " +
                                "${trplanData[j]?.get("end").toString()}: " +
                                trplanData[j]?.get("group").toString(),
                        style = Typography.body2
                    )
                }
            }
        }
    }
}