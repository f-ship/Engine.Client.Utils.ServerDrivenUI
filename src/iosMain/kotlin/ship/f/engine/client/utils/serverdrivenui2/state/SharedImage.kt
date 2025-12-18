@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

actual class SharedImage(private val image: UIImage?) {

    actual fun toByteArray(): ByteArray? {
        val data: NSData = image?.let { UIImagePNGRepresentation(it) } ?: run {
            println("toByteArray null")
            return null
        }
        return data.toByteArray()
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val bytes = toByteArray() ?: run {
            println("toImageBitmap null")
            return null
        }
        return Image.makeFromEncoded(bytes).toComposeImageBitmap()
    }
}

private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)

    val out = ByteArray(size)
    val src = bytes ?: return ByteArray(0)

    out.usePinned { pinned ->
        memcpy(pinned.addressOf(0), src, length)
    }
    return out
}
