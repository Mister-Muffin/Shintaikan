package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun Anfaenger() {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
        state = lazyListState
    ) {
        item {
            Text(text = "Karate Club\nShintaikan e.V.", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Anfänger und Interessenten", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Generell sind 2 Schnupperstunden gratis!")

            Bambus()

            Text(text = "Karaminis", style = MaterialTheme.typography.titleMedium)

            Text(stringResource(R.string.about_1))
            Text(stringResource(R.string.about_3))

            Bambus()


            Text(text = "Kinder Karate Anfänger", style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.about_4))
            Bambus()

            Text(text = "Jugend Karate", style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.about_5))
            Bambus()

            Text(text = "Karate Erwachsene", style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.about_6))
            Bambus()

            Text(text = "Beiträge und Zahlweise", style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.about_7))

        }
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