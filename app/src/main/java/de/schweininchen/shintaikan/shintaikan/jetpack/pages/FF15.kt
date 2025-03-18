package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseDataPage(
    title: String,
    document: String,
    imageResource: Int,
    vm: MyViewModel,
    onRefresh: (endRefresh: () -> Unit) -> Unit
) {
    val refreshState = rememberPullToRefreshState()
    val firestoreData = vm.firestoreData[document]

    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh { isRefreshing = false } }
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
                contentDescription = "Decoration image",
                Modifier
                    .size(100.dp)
                    .align(Alignment.End)
            )

            Text(text = title, style = MaterialTheme.typography.headlineLarge)

            if (firestoreData.isNullOrEmpty()) {
                CircularProgressIndicator()
            } else {
                Html(text = firestoreData["html"].toString())
            }

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
