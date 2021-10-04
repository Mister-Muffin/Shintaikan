package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Home() {
    val imageSize = 100.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        Text(text = "Linnéstraße 14, Freiburg West", style = Typography.h2)
        Image(
            painter = painterResource(id = R.drawable.pelli),
            contentDescription = "Shintaikan logo",
            Modifier.size(200.dp)
        )
        Text(style = Typography.h3, text = "***WORDPRESS***")
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
        Divider(color = Color(0x1AFFFFFF))
        Image(
            painter = painterResource(id = R.drawable.zeichen_kara),
            contentDescription = "kara",
            modifier = Modifier
                .width(imageSize)
                .height(imageSize)
        )
        Image(
            painter = painterResource(id = R.drawable.zeichen_te),
            contentDescription = "te",
            modifier = Modifier
                .width(imageSize)
                .height(imageSize)
        )
        Text("Kontakt:")
        Text("shintaikan@web.de")

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ShintaikanJetpackTheme {
        Home()
    }
}