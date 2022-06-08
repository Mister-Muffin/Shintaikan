package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun Trplan(trplanData: Map<String, MutableMap<String, Any>>, updateTrplan: () -> Unit) {
    val days = arrayOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag")
    if (trplanData.isEmpty()) updateTrplan()

    val lazyListState = rememberLazyListState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        state = lazyListState
    ) {
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
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
        item {
            if (trplanData.isEmpty()) {
                CircularProgressIndicator()
            } else {
                for (i in days) {
                    Text(text = i, style = MaterialTheme.typography.headlineMedium)
                    for (j in trplanData.keys) {
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
