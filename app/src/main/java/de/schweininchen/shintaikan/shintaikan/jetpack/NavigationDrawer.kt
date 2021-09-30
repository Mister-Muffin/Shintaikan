package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun drawerContent(onClick: (String) -> Unit): @Composable() (ColumnScope.() -> Unit) =
    {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.pelli),
                    contentDescription = "Shintaikan logo",
                    Modifier
                        .size(150.dp)
                        .align(Alignment.CenterHorizontally)
                )
                DrawerItem("Start", Icons.Outlined.Info) { onClick("Home") }
                DrawerItem("Trainingsplan", Icons.Outlined.DateRange) { onClick("Trplan") }
            }

        }
        Divider()
    }

@Composable
private fun DrawerItem(
    name: String, icon: ImageVector, onClick: () -> Unit) {
    val customPadding = 15.dp
    Row(
        Modifier
            .clickable(onClick = onClick)
            .padding(top = customPadding, bottom = customPadding, start = 20.dp)
    ) {
        Icon(icon, contentDescription = "Drawer Icon")
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(), text = name
        )
    }
}