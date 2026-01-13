package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeRange
import platform.CoreMedia.CMTimeRangeMake
import platform.CoreMedia.kCMTimeZero
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIModalPresentationFullScreen
import ship.f.engine.client.utils.serverdrivenui3.ext.WithState2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Source2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
import ship.f.engine.shared.utils.serverdrivenui2.state.VideoState2
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@Composable
actual fun Video2(
    s: MutableState<VideoState2>,
    m: Modifier,
) = s.WithState2(m) { modifier ->
    Video2(modifier)
}

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class, InternalResourceApi::class)
@Composable
actual fun VideoState2.Video2(
    modifier: Modifier,
) {
    val controller = remember { AVPlayerViewController() }
    val player = remember { AVQueuePlayer() }
    val looper = remember { mutableStateOf<AVPlayerLooper?>(null) }


    DisposableEffect(Unit) {
        controller.player = player
        controller.showsPlaybackControls = false
        controller.videoGravity = AVLayerVideoGravityResizeAspectFill
        controller.view.insetsLayoutMarginsFromSafeArea = false
        controller.modalPresentationStyle = UIModalPresentationFullScreen
        controller.view.layoutMargins = UIEdgeInsetsMake(0.0, 0.0, 0.0, 0.0)
        onDispose {
            player.pause()
            player.replaceCurrentItemWithPlayerItem(null)
            controller.player = null
        }
    }

    fun debugUrl(url: NSURL?) {
        println("url = $url")
        println("absoluteString = ${url?.absoluteString}")
        println("path = ${url?.path}")
        val path = url?.path
        if (path != null) {
            val exists = NSFileManager.defaultManager.fileExistsAtPath(path)
            println("fileExistsAtPath($path) = $exists")
        }
    }

    fun mp4UrlFromComposeResources(nameNoExt: String): NSURL {
        // Ensure it matches where you keep it (drawable/ is common, but you can use other dirs too)
//        val uri = Res.getUri("drawable/$nameNoExt.mp4")  // returns "file:///..."
        val uri =
            NSBundle.mainBundle.resourcePath + "/compose-resources/composeResources/projectx.composeapp.generated.resources/drawable/$nameNoExt.mp4"
        return NSURL.fileURLWithPath(uri)
    }

    LaunchedEffect(src) {
        val url: NSURL? = when (val s = src) {
            is Source2.Local2 -> NSURL.fileURLWithPath(s.location)
            is Source2.Material2 -> NSURL.fileURLWithPath(s.location)
            is Source2.Resource2 -> mp4UrlFromComposeResources(s.location)
            is Source2.Url2 -> NSURL.URLWithString(s.location)
            is Source2.LiveUrl2 -> null
        }
        sduiLog(url, tag = "EngineX > Video2 > iOS")

        url?.let {
            sduiLog(it.path, tag = "EngineX > Video2 > iOS")
            debugUrl(it)
            val item = AVPlayerItem(uRL = it)
            looper.value = AVPlayerLooper(player = player, templateItem = item, fullVideoRange(item))
            player.replaceCurrentItemWithPlayerItem(item)
            logPlayerState(player)
            waitUntilReadyThenPlay(player)
            logPlayerState(player)
            player.volume = 0f
        }
    }

    UIKitViewController(
        modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets(0)),
        factory = { controller },
        update = { vc ->
            if (vc.player !== player) vc.player = player
        })
}

@OptIn(ExperimentalForeignApi::class)
fun logPlayerState(player: AVPlayer) {
    val item = player.currentItem
    println("=== AVPlayer DEBUG ===")
    println("player.status = ${player.status}")
    println("player.rate = ${player.rate}")
    println("player.timeControlStatus = ${player.timeControlStatus}")
    println("player.error = ${player.error?.localizedDescription}")

    if (item != null) {
        println("item.status = ${item.status}")
        println("item.error = ${item.error?.localizedDescription}")
        println("item.asset = ${item.asset}")
        println("item.asset.playable = ${(item.asset as? AVURLAsset)?.isPlayable()}")
        println("item.asset.duration = ${item.asset.duration}")
    }
    println("======================")
}

private fun Any?.asIntOrNull(): Int? = when (this) {
    is Int -> this
    is Long -> this.toInt()
    is Number -> this.toInt()
    else -> null
}

private suspend fun waitUntilReadyThenPlay(
    player: AVPlayer,
    timeoutMs: Long = 5_000,
) {
    val start = TimeSource.Monotonic.markNow()
    val timeout = timeoutMs.milliseconds

    while (start.elapsedNow() < timeout) {
        val item = player.currentItem

        val itemStatus = item?.status   // 0 unknown, 1 ready, 2 failed
        val playerStatus = player.status

        // Failed?
        if (itemStatus == 2L) {
            println("AVPlayerItem failed: ${item.error?.localizedDescription}")
            return
        }
        if (playerStatus == 2L) {
            println("AVPlayer failed: ${player.error?.localizedDescription}")
            return
        }

        // Ready?
        if (itemStatus == 1L && playerStatus == 1L) {
            println("AVPlayerItem and AVPlayer are ready")
            player.play()
            return
        }

        delay(50)
    }

    println("Timed out waiting for AVPlayer/Item to become ready")
}

@OptIn(ExperimentalForeignApi::class)
fun fullVideoRange(item: AVPlayerItem): CValue<CMTimeRange> {
    val asset = item.asset
    val duration = asset.duration   // CMTime
    return CMTimeRangeMake(
        start = kCMTimeZero.toCValue(),
        duration = duration
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun CMTime.toCValue() = cValue<CMTime> {
    value = this@toCValue.value
    timescale = this@toCValue.timescale
    flags = this@toCValue.flags
    epoch = this@toCValue.epoch
}
