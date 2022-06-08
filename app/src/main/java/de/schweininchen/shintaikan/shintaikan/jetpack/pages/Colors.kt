package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import kotlin.random.Random

@Composable
fun Colors() {
    val lazyListState = rememberLazyListState()

    val refreshState = rememberSwipeRefreshState(false)
    val colors = remember {
        mutableStateListOf<IntArray>()
    }

    LaunchedEffect(key1 = true) { refreshColors(colors = colors) }

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing = refreshState.isRefreshing),
        onRefresh = { refreshColors(colors) },
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
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
        ) {
            itemsIndexed(colors) { index: Int, _ ->
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(
                            Color(
                                android.graphics.Color.rgb(
                                    colors[index][0],
                                    colors[index][1],
                                    colors[index][2]
                                )
                            )
                        )
                        .padding(8.dp)
                )
            }
        }
    }
}

private fun refreshColors(colors: SnapshotStateList<IntArray>) {
    colors.clear()
    for (i in 0 until 10) {
        colors.add(
            intArrayOf(
                Random.nextInt(0, 255),
                Random.nextInt(0, 255),
                Random.nextInt(0, 255)
            )
        )
    }
}
