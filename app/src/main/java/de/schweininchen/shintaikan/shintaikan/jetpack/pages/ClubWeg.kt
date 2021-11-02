package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun ClubWeg(
    viewModel: MyViewModel
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = viewModel.lazyState,
    ) {
        item {
            Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        }
        item {
            Text(
                text = "GPS: 48°00’57.7″N 7°48’47.8″E",
                style = Typography.body2,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Der Club",
                style = Typography.h2,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(stringResource(R.string.club_1), style = Typography.body2)


            Text(text = "Wegbeschreibung", style = Typography.h2)

            Text(stringResource(R.string.club_2), style = Typography.body2)

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