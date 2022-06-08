package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun Trplan(lazyListState: LazyListState, trplanData: Map<String, MutableMap<String, Any>>, updateTrplan: () -> Unit) {
    val days = arrayOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")
    if (trplanData.isEmpty()) updateTrplan()

    //Log.d("TAG", "TTrplan: ${firestoreData}")

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = lazyListState
    ) {
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
            Text(text = "Trainingsplan", style = MaterialTheme.typography.headlineMedium)

            Image(
                painter = painterResource(id = R.drawable.bambus),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }*/
        item {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.bambus),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.7f)
                    .height(50.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.kaempfer_app),
                    contentDescription = "Shintaikan logo",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.weight(0.3f)//.fillMaxWidth()//.size(100.dp)
                )
            }
        }
        item {
            if (trplanData.isEmpty()) {
                CircularProgressIndicator()
            } else {
                //Log.d("TAG2", "Trplan: $firestoreData")
                for (i in days) {
                    Text(text = i, style = MaterialTheme.typography.headlineMedium)
                    //Log.d("TAG", "Keys: ${firestoreData.keys}")
                    for (j in trplanData.keys) {
                        //Log.d("TAG", "Keys: ${firestoreData[j]}")
                        //Log.d("TAG", "Index: ${days.indexOf(i).inc()}")
                        if (!trplanData[j].isNullOrEmpty() &&
                            trplanData[j]?.get("key").toString()
                                .startsWith(days.indexOf(i).inc().toString()) &&
                            trplanData[j]?.get("start")
                                .toString().isNotEmpty()
                        ) {
                            if (trplanData[j]?.get("group")
                                    .toString() == "Benutzerdefiniert"
                            ) {
                                Text(
                                    text = "${trplanData[j]?.get("start").toString()} - " +
                                            "${trplanData[j]?.get("end").toString()}: " +
                                            trplanData[j]?.get("customText").toString()
                                )
                            } else {
                                Text(
                                    text = "${trplanData[j]?.get("start").toString()} - " +
                                            "${trplanData[j]?.get("end").toString()}: " +
                                            trplanData[j]?.get("group").toString()
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
