package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.compose.resources.ExperimentalResourceApi
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.*
import ship.f.engine.client.utils.serverdrivenui.generated.resources.Res
import ship.f.engine.client.utils.serverdrivenui2.ext.WithState2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Source2
import ship.f.engine.shared.utils.serverdrivenui2.state.VideoState2

@Composable
actual fun Video2(
    s: MutableState<VideoState2>,
    m: Modifier,
) = s.WithState2(m) { modifier ->
    Video2(modifier)
}

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class)
@Composable
actual fun VideoState2.Video2(
    modifier: Modifier,
) {
    val controller = remember { AVPlayerViewController() }
    val player = remember { AVPlayer() }

    DisposableEffect(Unit) {
        controller.player = player
        onDispose { player.pause(); controller.player = null }
    }

    fun byteArrayToNSData(bytes: ByteArray): NSData =
        bytes.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
        }

    fun cachePathFor(name: String) = NSTemporaryDirectory() + name

    LaunchedEffect(src) {
        val url: NSURL? = when (src) {
            is Source2.Local2 -> NSURL.fileURLWithPath(src.location)
            is Source2.Material2 -> NSURL.fileURLWithPath(src.location)
            is Source2.Resource2 -> {
                val bytes = Res.readBytes(src.location)
                val fileName = "cmp_video_${src.location.replace('/', '_')}"
                val path = cachePathFor(fileName)
                val nsData = byteArrayToNSData(bytes)
                nsData.writeToFile(path, atomically = true)
                NSURL.fileURLWithPath(path)
            }
            is Source2.Url2 -> NSURL.URLWithString(src.location)
        }
        url?.let {
            val item = AVPlayerItem(uRL = it)
            player.replaceCurrentItemWithPlayerItem(item)
        }
    }


    LaunchedEffect(src) {
        player.play()
        player.volume = 0f
    }

    UIKitView(
        modifier = modifier,
        factory = {
            controller.showsPlaybackControls = true
            controller.view
        },
        update = { if (controller.player != player) controller.player = player }
    )
}
