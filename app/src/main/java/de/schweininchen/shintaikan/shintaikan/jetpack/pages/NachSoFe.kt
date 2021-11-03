package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import de.schweininchen.shintaikan.shintaikan.jetpack.R
import de.schweininchen.shintaikan.shintaikan.jetpack.ui.theme.Typography

@Composable
fun NachSoFe(vm: MyViewModel) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = vm.lazyStateSoFe
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.kaempfer_app),
                contentDescription = "Shintaikan logo",
                Modifier
                    .size(100.dp)
            )
        }
        item {
            Text(text = "Karate Club\nShintaikan e.V.", style = Typography.h1)
        }
        item {
            Text(text = "Neue Trainingspläne nach den Sommerferien", style = Typography.h2)
        }
        item {
            Text(stringResource(R.string.nach_so_fe_1), style = Typography.body2)
        }
        item {
            Bambus()
        }
        item {
            Text(stringResource(R.string.nach_so_fe_2), style = Typography.body2)
        }
        item {
            Bambus()

        }

    }
}