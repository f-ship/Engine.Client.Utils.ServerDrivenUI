@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import platform.Foundation.NSData
import platform.Photos.PHPhotoLibrary
import platform.PhotosUI.*
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
internal fun rememberIosImagePickerPresenter(
    onImagePicked: (UIImage?) -> Unit
): IosImagePickerPresenter {
    // Host VC (Compose will render it offscreen; we just use it to present pickers)
    val host = remember {
        PickerHostViewController().apply {
            this.onImagePicked = onImagePicked
        }
    }

    // Keep callback current across recompositions
    LaunchedEffect(onImagePicked) {
        host.onImagePicked = onImagePicked
    }

    // Mount the host VC into the hierarchy
    UIKitViewController(factory = { host }, modifier = Modifier)

    return remember(host) { IosImagePickerPresenter(host) }
}

internal class IosImagePickerPresenter(
    private val host: PickerHostViewController
) {
    fun presentGallery() = host.presentGallery()
    fun presentCamera() = host.presentCamera()
}

/**
 * A tiny view controller that can present PHPicker (gallery) or UIImagePicker (camera),
 * and forwards the resulting UIImage to onImagePicked.
 */
internal class PickerHostViewController : UIViewController(null, null),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol,
    PHPickerViewControllerDelegateProtocol {

    var onImagePicked: (UIImage?) -> Unit = {}

    fun presentGallery() {
        val config = PHPickerConfiguration(photoLibrary = PHPhotoLibrary.sharedPhotoLibrary()).apply {
            selectionLimit = 1L
            filter = PHPickerFilter.imagesFilter()
        }

        val picker = PHPickerViewController(configuration = config)
        picker.delegate = this
        presentViewController(picker, animated = true, completion = null)
    }

    fun presentCamera() {
        if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            // No camera (simulator), fallback to gallery
            presentGallery()
            return
        }

        val picker = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            delegate = this@PickerHostViewController
            allowsEditing = false
        }
        presentViewController(picker, animated = true, completion = null)
    }

    // --- UIImagePicker (camera) ---
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        picker.dismissViewControllerAnimated(true) {
            onImagePicked(image)
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true) {
            onImagePicked(null)
        }
    }

    // --- PHPicker (gallery) ---
    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult
        val provider = result?.itemProvider
        if (provider == null) {
            onImagePicked(null)
            return
        }

        // You can also use "public.image" if you don’t want UTType imports.
        val typeId = "public.image"

        if (!provider.hasItemConformingToTypeIdentifier(typeId)) {
            onImagePicked(null)
            return
        }

        provider.loadDataRepresentationForTypeIdentifier(typeId) { data: NSData?, error ->
            val image = data?.let { UIImage.imageWithData(it) }

            // Ensure callback back on main thread
            dispatch_async(dispatch_get_main_queue()) {
                onImagePicked(image)
            }
        }
    }
}
