package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun drawerContent(): @Composable() (ColumnScope.() -> Unit) =
    {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            // Add a single item
            item {
                DrawerItem("Bob", Icons.Filled.Face)
                DrawerItem("Hans", Icons.Filled.Face)
                DrawerItem("Otto", Icons.Filled.Face)
                DrawerItem("Gsund", Icons.Filled.Favorite)
            }

        }
        Divider()

    }

@Composable
private fun DrawerItem(name: String, icon: ImageVector) {
    Row(
        Modifier
            .clickable { }
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp)
    ) {
        Icon(icon, contentDescription = "Drawer Icon")
        Text(modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth(), text = name)
    }
}