package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Colors(vm: MyViewModel) {
    val refreshState = rememberPullToRefreshState()
    val colors = remember {
        mutableStateListOf<IntArray>()
    }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) { refreshColors(colors = colors) }

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            refreshColors(colors)
            isRefreshing = false
        }
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = vm.lazyStateColors,
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
