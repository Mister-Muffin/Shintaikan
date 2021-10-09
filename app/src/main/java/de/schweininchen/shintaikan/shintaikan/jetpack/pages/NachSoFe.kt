package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
fun NachSoFe() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.kaempfer_app),
            contentDescription = "Shintaikan logo",
            Modifier
                .size(100.dp)
                .align(Alignment.End)
        )
        Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        Text(text = "Neue Trainingspläne nach den Sommerferien", style = Typography.h2)

        Text(stringResource(R.string.nach_so_fe_1), style = Typography.body2)

        Image(
            painter = painterResource(id = R.drawable.bambus),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )
        Text(stringResource(R.string.nach_so_fe_2), style = Typography.body2)
        Image(
            painter = painterResource(id = R.drawable.bambus),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

    }
}