@file:OptIn(ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui3.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.*
import platform.UIKit.UIImage
import platform.UIKit.UIViewController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberGalleryManager(onResult: (SharedImage?) -> Unit): GalleryManager {
    val host = remember { GalleryHostViewController() }

    // Keep callback current
    LaunchedEffect(onResult) {
        host.onResult = onResult
    }

    // Mount host VC so it can present the picker
    UIKitViewController(factory = { host }, modifier = Modifier)

    return remember(host) {
        GalleryManager(onLaunch = { host.presentGallery() })
    }
}

actual class GalleryManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() = onLaunch()
}

private class GalleryHostViewController : UIViewController(null, null),
    PHPickerViewControllerDelegateProtocol {

    var onResult: (SharedImage?) -> Unit = {}

    fun presentGallery() {
        val config = PHPickerConfiguration(photoLibrary = PHPhotoLibrary.sharedPhotoLibrary()).apply {
            selectionLimit = 1L
            filter = PHPickerFilter.imagesFilter()
        }

        val picker = PHPickerViewController(configuration = config).apply {
            delegate = this@GalleryHostViewController
        }

        presentViewController(picker, animated = true, completion = null)
    }

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult
        val provider = result?.itemProvider
        if (provider == null) {
            dispatch_async(dispatch_get_main_queue()) { onResult(null) }
            return
        }

        // Use a plain UTI string to avoid UTType import hassle
        val typeId = "public.image"

        if (!provider.hasItemConformingToTypeIdentifier(typeId)) {
            dispatch_async(dispatch_get_main_queue()) { onResult(null) }
            return
        }

        provider.loadDataRepresentationForTypeIdentifier(typeId) { data: NSData?, _ ->
            val uiImage = data?.let { UIImage.imageWithData(it) }
            dispatch_async(dispatch_get_main_queue()) {
                onResult(uiImage?.let { SharedImage(it) })
            }
        }
    }
}
