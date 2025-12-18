@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import kotlinx.cinterop.*
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager {
    // A tiny host UIViewController mounted into the Compose hierarchy.
    val host = remember { CameraHostViewController() }

    // Keep callback fresh across recompositions
    LaunchedEffect(onResult) {
        host.onResult = onResult
    }

    // Mount host into the view hierarchy so it can present UIImagePickerController
    UIKitViewController(factory = { host }, modifier = Modifier)

    return remember(host) {
        CameraManager(
            onLaunch = { host.presentCamera() }
        )
    }
}

actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() = onLaunch()
}

/**
 * Presents the iOS Camera UI and forwards the result as SharedImage(UIImage).
 */
private class CameraHostViewController : UIViewController(null, null),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {

    var onResult: (SharedImage?) -> Unit = {}

    fun presentCamera() {
        // Simulator / devices without camera
        if (!UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            dispatch_async(dispatch_get_main_queue()) { onResult(null) }
            return
        }

        val picker = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            allowsEditing = false
            delegate = this@CameraHostViewController
        }

        presentViewController(picker, animated = true, completion = null)
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

        picker.dismissViewControllerAnimated(true) {
            // Ensure callback on main
            dispatch_async(dispatch_get_main_queue()) {
                onResult(image?.let { SharedImage(it) })
            }
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true) {
            dispatch_async(dispatch_get_main_queue()) { onResult(null) }
        }
    }
}