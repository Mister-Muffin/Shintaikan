package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@Composable
fun FirebaseDataPage(
    imageResource: Int,
    firestoreData: MutableMap<String, Any>?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    extraComposable: @Composable () -> Unit = {}
) {

    val lazyListState = rememberLazyListState()

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            )
        }

    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            state = lazyListState
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Image(
                        painter = painterResource(id = R.drawable.kaempfer_app),
                        contentDescription = "Decoration image",
                        Modifier
                            .size(100.dp)
                    )
                }
            }

            item {
                if (firestoreData == null || firestoreData.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    Html(html = firestoreData["html"].toString(), LocalTextStyle.current.color)
                }
            }

            item {
                extraComposable()
            }

            item {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Decoration image",
                    Modifier
                        .size(250.dp)
                        .padding(top = 16.dp)
                )
            }

        }
    }
}