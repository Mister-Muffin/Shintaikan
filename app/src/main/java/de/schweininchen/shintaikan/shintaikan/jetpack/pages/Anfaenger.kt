package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Anfaenger() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        Text(text = "Anfänger und Interessenten", style = Typography.h2)
        Text(text = "Generell sind 2 Schnupperstunden gratis!", style = Typography.body2)

        Bambus()

        Text(text = "Karaminis", style = Typography.h2)

        Text(stringResource(R.string.about_1), style = Typography.body2)
        Text(stringResource(R.string.about_3), style = Typography.body2)

        Bambus()


        Text(text = "Kinder Karate Anfänger", style = Typography.h2)
        Text(stringResource(R.string.about_4), style = Typography.body2)
        Bambus()

        Text(text = "Jugend Karate", style = Typography.h2)
        Text(stringResource(R.string.about_5), style = Typography.body2)
        Bambus()

        Text(text = "Karate Erwachsene", style = Typography.h2)
        Text(stringResource(R.string.about_6), style = Typography.body2)
        Bambus()

        Text(text = "Beiträge und Zahlweise", style = Typography.h2)
        Text(stringResource(R.string.about_7), style = Typography.body2)

    }
}

@Composable
private fun Bambus() {
    Image(
        painter = painterResource(id = R.drawable.bambus),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}