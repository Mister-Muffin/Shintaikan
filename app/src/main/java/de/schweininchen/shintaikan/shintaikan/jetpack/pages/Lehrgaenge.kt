package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.schweininchen.shintaikan.shintaikan.jetpack.getFirestoreData
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Lehrgaenge() {

    val firestoreData = remember {
        mutableStateOf(mapOf<String, Any>())
    }

    getFirestoreData() {
        firestoreData.value = it
    }

    FirebaseDataPage("Lehrgänge & Turniere", firestoreData = firestoreData.value) {
        Text(text = "Die Ausschreibungen hängen auch im Dojo!", style = Typography.h3)
    }
}