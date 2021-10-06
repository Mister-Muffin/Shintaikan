package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.getFirestoreData
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Ferien() {
    val imageSize = 100.dp

    val firestoreData = remember {
        mutableStateOf(mapOf<String, Any>())
    }

    val scrollState = rememberScrollState()

    getFirestoreData() {
        firestoreData.value = it
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy((8 * 3).dp)
    ) {
        Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        Text(text = "Ferientraining", style = Typography.h2)

        if (firestoreData.value.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Html(text = firestoreData.value["html"].toString())
        }

        Image(
            painter = painterResource(id = R.drawable.sakura),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
        )

    }
}