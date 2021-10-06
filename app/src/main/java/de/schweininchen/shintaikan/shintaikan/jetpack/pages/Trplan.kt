package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.ShintaikanJetpackTheme
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Trplan() {

    val imageSize = 100.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.kaempfer_app),
            contentDescription = "Shintaikan logo",
            Modifier
                .size(100.dp)
                .align(Alignment.End)
        )
        Text(text = "Trainingsplan", style = Typography.h2)

        Image(
            painter = painterResource(id = R.drawable.bambus),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Text("***FIREBASE***")
        Image(
            painter = painterResource(id = R.drawable.bambus),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ShintaikanJetpackTheme {
        Trplan()
    }
}