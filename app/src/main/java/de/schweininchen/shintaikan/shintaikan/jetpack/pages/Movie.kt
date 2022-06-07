package de.schweininchen.shintaikan.shintaikan.jetpack.pages

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

//Hoffnungslos verloren

@Composable
fun ExoVideoPlayer(exoPlayer: SimpleExoPlayer) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        factory = { context1 ->
            PlayerView(context1).apply {
                player = exoPlayer
            }
        },
    )
}

fun getSimpleExoPlayer(context: Context, uri: String): SimpleExoPlayer {
    return SimpleExoPlayer.Builder(context).build().apply {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context.packageName)
        )
        // streaming from internet
        val internetVideoItem = MediaItem.fromUri(uri)
        val internetVideoSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(internetVideoItem)
        this.addMediaSource(internetVideoSource)
        // init
        this.prepare()
    }
}