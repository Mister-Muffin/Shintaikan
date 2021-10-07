package de.schweininchen.shintaikan.shintaikan.jetpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun drawerContent(onClick: (String) -> Unit): @Composable (ColumnScope.() -> Unit) =
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.pelli),
                contentDescription = "Shintaikan logo",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .size(120.dp)
            )
            Divider()
            Column {
                DrawerItem("Start", Icons.Outlined.Info) { onClick("Home") }
                DrawerItem("Trainingsplan", Icons.Outlined.DateRange) { onClick("Trplan") }
                DrawerItem(
                    "Gürtelprüfungen",
                    Icons.Outlined.NorthEast
                ) { onClick("Pruefungen") }
                DrawerItem("Ferientraining", Icons.Outlined.BeachAccess) { onClick("Ferien") }
                DrawerItem(
                    "Nach den Sommerferien",
                    Icons.Outlined.WbSunny
                ) { onClick("NachSoFe") }
                DrawerItem(
                    "Der Club / Wegbeschreibung",
                    Icons.Outlined.Home
                ) { onClick("ClubWeg") }
                DrawerItem(
                    "Anfänger / Interressenten",
                    Icons.Outlined.DirectionsWalk
                ) { onClick("Anfaenger") }
                DrawerItem(
                    "Vorführungen",
                    Icons.Outlined.RemoveRedEye
                ) { onClick("Vorfuehrungen") }
                DrawerItem(
                    "Lehrgänge + Turniere",
                    Icons.Outlined.People
                ) { onClick("Lehrgaenge") }
            }
        }
        Divider()
    }

@Composable
private fun DrawerItem(
    name: String, icon: ImageVector, onClick: () -> Unit
) {
    val customPadding = 15.dp
    Row(
        Modifier
            .clickable(onClick = onClick)
            .padding(top = customPadding, bottom = customPadding, start = 20.dp)
    ) {
        Icon(imageVector = icon, "Drawer item", tint = Color(0xFF898989))
        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth(), text = name,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}