package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Home(postsList: List<Array<String>>) {
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
        if (postsList.isEmpty()) {
            item { CircularProgressIndicator() }
        } else {
            items(postsList) { post ->
                Text(style = Typography.h3, text = post[0])
                Html(text = post[1])

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
private fun Html(text: String) {
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