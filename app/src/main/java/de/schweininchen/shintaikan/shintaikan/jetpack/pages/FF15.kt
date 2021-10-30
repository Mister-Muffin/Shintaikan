package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun FirebaseDataPage(
    title: String,
    firestoreData: Map<String, Any>?,
    imageResource: Int,
    vm: MyViewModel,
    onRefresh: () -> Unit,
    extraComposable: @Composable () -> Unit = {}
) {
    val isRefreshing by vm.isRefreshing.collectAsState()

    val refreshState = rememberSwipeRefreshState(false)

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
                backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            )
        }

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.kaempfer_app),
                contentDescription = "Shintaikan logo",
                Modifier
                    .size(100.dp)
                    .align(Alignment.End)
            )

            Text(text = title, style = Typography.h1)

            if (firestoreData == null || firestoreData.isEmpty()) {
                CircularProgressIndicator()
            } else {
                Html(text = firestoreData["html"].toString())
            }

            extraComposable()

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Bonsai",
                Modifier
                    .size(250.dp)
            )

        }
    }
}