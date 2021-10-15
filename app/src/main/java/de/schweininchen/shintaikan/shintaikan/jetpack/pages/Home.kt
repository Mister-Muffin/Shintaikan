package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun Home(postsList: List<Array<String>>, viewModel: MyViewModel) {
    val imageSize = 100.dp

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
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
            item { CircularProgressIndicator() }
        } else {
            items(postsList) { post ->
                Card(elevation = 2.dp) {
                    Column() {
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
            Row {
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
            Text("shintaikan@web.de")
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
    })
}