package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Pruefungen() {
    val imageSize = 100.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.kaempfer_app),
            contentDescription = "Shintaikan logo",
            Modifier
                .size(100.dp)
                .align(Alignment.End)
        )
        Text(text = "Gürtelprüfungen", style = Typography.h2)
        Text(text = "Derzeit finden keine Prüfungen statt.", style = Typography.body2)

        Image(
            painter = painterResource(id = R.drawable.seerose1),
            contentDescription = null,
            modifier = Modifier
                .size(350.dp)
        )
    }
}