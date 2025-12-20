@file:kotlin.OptIn(InternalResourceApi::class)

package ship.f.engine.client.utils.serverdrivenui3.state

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes
import ship.f.engine.client.utils.serverdrivenui2.ext.WithState2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Source2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
import ship.f.engine.shared.utils.serverdrivenui2.state.VideoState2
import java.io.File

@Composable
actual fun Video2(
    s: MutableState<VideoState2>,
    m: Modifier,
) = s.WithState2(m) { modifier ->
    Video2(modifier)
}

@OptIn(UnstableApi::class, InternalResourceApi::class, InternalResourceApi::class)
@Composable
actual fun VideoState2.Video2(
    modifier: Modifier,
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }

    val contentResolver = LocalContext.current.contentResolver

    LaunchedEffect(src) {
        val uri: Uri = when (src) {
            is Source2.Local2 -> src.location.toUri()
            is Source2.Material2 -> src.location.toUri()
            is Source2.Resource2 -> {
                // TODO to not hard code this, may have to come from the src itself
                val bytes =
                    readResourceBytes("composeResources/projectx.composeapp.generated.resources/drawable/${src.location}.mp4")
                val tmp = File.createTempFile("cmp_video_", ".mp4", context.cacheDir)
                tmp.outputStream().use { it.write(bytes) }
                Uri.fromFile(tmp)
            }

            is Source2.Url2 -> src.location.toUri()
            is Source2.LiveUrl2 -> TODO()
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        exoPlayer.volume = 0f
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
    }

    var visible by remember { mutableStateOf(false) }

    // Smooth alpha transition
    val alpha by animateFloatAsState(
        targetValue = if (visible) 0f else 1f,
        animationSpec = tween(durationMillis = fadeInMillis ?: 0),
        label = "videoFade"
    )

    val playerView = remember { PlayerView(context) }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                sduiLog("Video2", "onRenderedFirstFrame")
                visible = true
//                playerView.animate()
//                    .alpha(1f)
//                    .setDuration(1000L)
//                    .start()
            }
        }

        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    Box(modifier) {
        AndroidView(
            factory = {
                playerView.apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = RESIZE_MODE_ZOOM
                }
            },
            update = { it.player = exoPlayer }
        )
        Box(
            Modifier
                .matchParentSize()
                .alpha(alpha)
                .background(Color.Black)
        )
    }
}
