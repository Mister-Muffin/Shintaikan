package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Ferien() {
    val imageSize = 100.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        Text(text = "Ferientraining", style = Typography.h2)

        Text("***FIREBASE***")

        Image(
            painter = painterResource(id = R.drawable.sakura),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

    }
}