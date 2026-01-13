@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui3.util

import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.memcpy

actual class SharedImage(private val image: platform.UIKit.UIImage?) {

    actual fun toByteArray(): kotlin.ByteArray? {
        val data: platform.Foundation.NSData = image?.let {
            _root_ide_package_.platform.UIKit.UIImagePNGRepresentation(
                it
            )
        } ?: run {
            _root_ide_package_.kotlin.io.println("toByteArray null")
            return null
        }
        return data.toByteArray()
    }

    actual fun toImageBitmap(): androidx.compose.ui.graphics.ImageBitmap? {
        val bytes = toByteArray() ?: run {
            _root_ide_package_.kotlin.io.println("toImageBitmap null")
            return null
        }
        return _root_ide_package_.org.jetbrains.skia.Image.Companion.makeFromEncoded(bytes).toComposeImageBitmap()
    }
}

private fun platform.Foundation.NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)

    val out = ByteArray(size)
    val src = bytes ?: return ByteArray(0)

    out.usePinned { pinned ->
        memcpy(pinned.addressOf(0), src, length)
    }
    return out
}
