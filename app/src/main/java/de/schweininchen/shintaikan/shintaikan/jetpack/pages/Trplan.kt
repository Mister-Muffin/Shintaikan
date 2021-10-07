package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import de.schweininchen.shintaikan.shintaikan.jetpack.getFirestoreTrplan
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun Trplan() {
    val days = arrayOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")

    val firestoreData = remember {
        mutableStateOf(mapOf<String, MutableMap<String, Any>>())
    }
    if (firestoreData.value.toString().length == 2) {
        getFirestoreTrplan {
            firestoreData.value = it
        }
    }
    Log.d("TAG", "TTrplan: ${firestoreData.value}")
    
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
    ) {
        item {
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.kaempfer_app),
                    contentDescription = "Shintaikan logo",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        item {
            Text(text = "Trainingsplan", style = Typography.h2)

            Image(
                painter = painterResource(id = R.drawable.bambus),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
        item {
            if (firestoreData.value.isEmpty()) {
                CircularProgressIndicator()
            } else {
                Log.d("TAG2", "Trplan: ${firestoreData.value}")
                for (i in days) {
                    Text(text = i, style = Typography.h2)
                    Log.d("TAG", "Keys: ${firestoreData.value.keys}")
                    for (j in firestoreData.value.keys) {
                        Log.d("TAG", "Keys: ${firestoreData.value[j]}")
                        Log.d("TAG", "Index: ${days.indexOf(i).inc()}")
                        if (!firestoreData.value[j].isNullOrEmpty() &&
                            firestoreData.value[j]?.get("key").toString()
                                .startsWith(days.indexOf(i).inc().toString()) &&
                            firestoreData.value[j]?.get("start")
                                .toString().isNotEmpty()
                        ) {
                            if (firestoreData.value[j]?.get("group")
                                    .toString() == "Benutzerdefiniert"
                            ) {
                                Text(
                                    text = "${firestoreData.value[j]?.get("start").toString()} - " +
                                            "${firestoreData.value[j]?.get("end").toString()}: " +
                                            firestoreData.value[j]?.get("customText").toString(),
                                    style = Typography.body2
                                )
                            } else {
                                Text(
                                    text = "${firestoreData.value[j]?.get("start").toString()} - " +
                                            "${firestoreData.value[j]?.get("end").toString()}: " +
                                            firestoreData.value[j]?.get("group").toString(),
                                    style = Typography.body2
                                )
                            }
                        }
                    }
                    Bambus()
                }
            }
        }
    }
}
