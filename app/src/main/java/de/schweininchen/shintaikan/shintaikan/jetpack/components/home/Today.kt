package de.schweininchen.shintaikan.shintaikan.jetpack.components.home

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.schweininchen.shintaikan.shintaikan.jetpack.MyViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun Today(viewModel: MyViewModel) {
    val firestoreData = viewModel.trplanData.value

    val isInTimeZone: Boolean
    val dayWord: String
    val day: Int
    val hour: Int
    val minute: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val target = LocalTime.now()
        isInTimeZone = target.isBefore(LocalTime.parse("22:00:00")) &&
                target.isAfter(LocalTime.parse("06:00:00"))
        hour = target.hour
        minute = target.minute
        val currentDate = LocalDate.now()
        dayWord = currentDate.dayOfWeek.getDisplayName(
            java.time.format.TextStyle.FULL,
            java.util.Locale.getDefault()
        )
        day = currentDate.dayOfWeek.value // Monday = 1 ... Sunday = 7
    } else {
        // Fallback to legacy APIs for lower API levels
        val calendar = java.util.Calendar.getInstance()
        hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        minute = calendar.get(java.util.Calendar.MINUTE)

        isInTimeZone = hour in 6..22

        val sdf = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
        dayWord = sdf.format(calendar.time)

        // Convert Calendar.DAY_OF_WEEK (Sunday=1, Monday=2, ..., Saturday=7) to ISO standard (Monday=1, ..., Sunday=7)
        val legacyDay = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        day = if (legacyDay == java.util.Calendar.SUNDAY) 7 else legacyDay - 1
    }

    // Return early if the current time is outside 06:00-22:00 or if it's the weekend (day > 5)
    if (!isInTimeZone || day > 5) {
        return
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Heute, $dayWord",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
            )
            for (j in firestoreData.keys) {
                val dayData = firestoreData[j]
                if (!dayData.isNullOrEmpty()
                    && dayData["start"].toString().isNotEmpty()
                    && dayData["key"].toString().startsWith(day.toString())
                ) {
                    val groupString =
                        if (dayData["group"].toString() == "Benutzerdefiniert") "customText"
                        else "group"

                    Row(
                        modifier = Modifier.fillMaxWidth(.8f)
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val startString = dayData["start"].toString()
                            val endString = dayData["end"].toString()
                            val iconSize = 16.dp
                            val iconPadding = 4.dp
                            if (LocalTime.of(hour, minute) in
                                LocalTime.parse(startString)..LocalTime.parse(endString).minusMinutes(1)
                            ) {
                                Image(
                                    Icons.Default.RadioButtonChecked,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(iconSize)
                                        .padding(end = iconPadding)
                                )
                            } else if (LocalTime.of(hour, minute) in
                                LocalTime.parse(startString).minusMinutes(15)..LocalTime.parse(startString)
                            ) {
                                Image(
                                    Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(iconSize)
                                        .padding(end = iconPadding)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(iconSize)
                                        .padding(end = iconPadding)
                                )
                            }
                        }
                        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (LocalTime.of(hour, minute) >= LocalTime.parse(dayData["end"].toString())) {
                                Color.Gray
                            } else {
                                Color.Unspecified
                            }
                        } else {
                            Color.Unspecified
                        }
                        Text(
                            text = "${dayData["start"].toString()} - " +
                                    "${dayData["end"].toString()}: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = color
                        )
                        Text(
                            text = dayData[groupString].toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = color
                        )
                    }

                }
            }
        }
    }
}