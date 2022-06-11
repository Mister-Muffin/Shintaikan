package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun Trplan(vm: MyViewModel) {
    val firestoreData = vm.trplanData.value
    val days = arrayOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")
    if (firestoreData.isEmpty()) vm.updateTrplan()

    //Log.d("TAG", "TTrplan: ${firestoreData}")

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = vm.lazyStateTrplan
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(
                        Color(
                            android.graphics.Color.rgb(
                                Random.nextInt(0, 255),
                                Random.nextInt(0, 255),
                                Random.nextInt(0, 255)
                            )
                        )
                    )
                    .padding(8.dp)
            )
        }
        /*item {
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
            Text(text = "Trainingsplan", style = MaterialTheme.typography.headlineLarge)

            Image(
                painter = painterResource(id = R.drawable.bambus),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
        item {
            if (firestoreData.isEmpty()) {
                CircularProgressIndicator()
            } else {
                //Log.d("TAG2", "Trplan: $firestoreData")
                for (i in days) {
                    Text(text = i, style = MaterialTheme.typography.headlineMedium)
                    //Log.d("TAG", "Keys: ${firestoreData.keys}")
                    for (j in firestoreData.keys) {
                        //Log.d("TAG", "Keys: ${firestoreData[j]}")
                        //Log.d("TAG", "Index: ${days.indexOf(i).inc()}")
                        if (!firestoreData[j].isNullOrEmpty() &&
                            firestoreData[j]?.get("key").toString()
                                .startsWith(days.indexOf(i).inc().toString()) &&
                            firestoreData[j]?.get("start")
                                .toString().isNotEmpty()
                        ) {
                            if (firestoreData[j]?.get("group")
                                    .toString() == "Benutzerdefiniert"
                            ) {
                                Text(
                                    text = "${firestoreData[j]?.get("start").toString()} - " +
                                            "${firestoreData[j]?.get("end").toString()}: " +
                                            firestoreData[j]?.get("customText").toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "${firestoreData[j]?.get("start").toString()} - " +
                                            "${firestoreData[j]?.get("end").toString()}: " +
                                            firestoreData[j]?.get("group").toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Bambus()
                }
            }
        }*/
    }
}
