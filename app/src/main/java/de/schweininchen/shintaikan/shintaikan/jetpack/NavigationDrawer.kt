package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                DrawerItem("Start", R.drawable.ic_outline_info) { onClick("Home") }
                DrawerItem("Trainingsplan", R.drawable.ic_outline_date_range) { onClick("Trplan") }
                DrawerItem("Gürtelprüfungen", R.drawable.ic_outline_north_east) { onClick("Pruefungen") }
                DrawerItem("Ferientraining", R.drawable.ic_outline_beach_access) { onClick("Ferien") }
                DrawerItem("Nach den Sommerferien", R.drawable.ic_outline_wb_sunny) { onClick("NachSoFe") }
                DrawerItem("Der Club / Wegbeschreibung", R.drawable.ic_outline_home) { onClick("ClubWeg") }
                DrawerItem("Anfänger / Interressenten", R.drawable.ic_outline_directions_walk) { onClick("Anfaenger") }
                DrawerItem("Vorführungen", R.drawable.ic_outline_remove_red_eye) { onClick("Vorfuehrungen") }
                DrawerItem("Lehrgänge + Turniere", R.drawable.ic_outline_people) { onClick("Lehrgaenge") }
            }

        }
        Divider()
    }

@Composable
private fun DrawerItem(
    name: String, icon: Int, onClick: () -> Unit) {
    val customPadding = 15.dp
    Row(
        Modifier
            .clickable(onClick = onClick)
            .padding(top = customPadding, bottom = customPadding, start = 20.dp)
    ) {
        Icon(painterResource(id = icon), contentDescription = "Drawer Icon")
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(), text = name
        )
    }
}