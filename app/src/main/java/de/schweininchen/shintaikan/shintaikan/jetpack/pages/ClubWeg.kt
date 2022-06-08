package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
fun ClubWeg() {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
    ) {
        item {
            Text(text = "Karate Club\nShintaikan e.V.", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "GPS: 48°00’57.7″N 7°48’47.8″E"
            )
        }
        item {
            Text(
                text = "Der Club",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {

            Text(stringResource(R.string.club_1))

        }
        item {

            Text(text = "Wegbeschreibung", style = MaterialTheme.typography.titleLarge)

        }
        item {
            Text(stringResource(R.string.club_2))

        }
        item {
            Image(
                painter = painterResource(id = R.drawable.map02),
                contentDescription = "map",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
        }
    }
}