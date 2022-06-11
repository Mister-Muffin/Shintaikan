package de.schweininchen.shintaikan.shintaikan.jetpack.components

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun trimedStringResource(@StringRes id: Int) = stringResource(id = id).split("\n").joinToString("\n") { it.trim() }

@Composable
fun Html(html: String, color: Color) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            this.setTextColor(
                android.graphics.Color.rgb(
                    (color.red * 255).toInt(),
                    (color.green * 255).toInt(),
                    (color.blue * 255).toInt()
                )
            )
            text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        })
}