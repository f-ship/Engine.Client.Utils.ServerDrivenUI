@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation

internal fun UIImage.toJpegBytes(quality: Double = 0.92): ByteArray? {
    val data = UIImageJPEGRepresentation(this, quality) ?: return null
    return data.toByteArray()
}

internal fun NSData.toByteArray(): ByteArray {
    val lengthInt = length.toInt()
    if (lengthInt == 0) return ByteArray(0)

    val out = ByteArray(lengthInt)
    memScoped {
        val bytes = bytes ?: return ByteArray(0)
        out.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), bytes, length)
        }
    }
    return out
}

internal fun UIImage.toImageBitmapOrNull(): ImageBitmap? {
    val data = UIImagePNGRepresentation(this) ?: return null
    val bytes = data.toByteArray()
    val skia = Image.makeFromEncoded(bytes)
    return skia.toComposeImageBitmap()
}