package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun Anfaenger() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Karate Club\nShintaikan e.V.", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Anfänger und Interessenten", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Generell sind 2 Schnupperstunden gratis!", style = MaterialTheme.typography.bodyMedium)

        Bambus()

        Text(text = "Karaminis", style = MaterialTheme.typography.headlineMedium)

        Text(stringResource(R.string.about_1), style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(R.string.about_3), style = MaterialTheme.typography.bodyMedium)

        Bambus()


        Text(text = "Kinder Karate Anfänger", style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.about_4), style = MaterialTheme.typography.bodyMedium)
        Bambus()

        Text(text = "Jugend Karate", style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.about_5), style = MaterialTheme.typography.bodyMedium)
        Bambus()

        Text(text = "Karate Erwachsene", style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.about_6), style = MaterialTheme.typography.bodyMedium)
        Bambus()

        Text(text = "Beiträge und Zahlweise", style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.about_7), style = MaterialTheme.typography.bodyMedium)

    }
}

@Composable
fun Bambus() {
    Image(
        painter = painterResource(id = R.drawable.bambus),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}