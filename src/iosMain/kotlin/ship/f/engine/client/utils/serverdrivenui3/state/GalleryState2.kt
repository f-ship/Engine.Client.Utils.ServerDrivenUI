@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ship.f.engine.client.utils.serverdrivenui3.RenderStatic
import ship.f.engine.client.utils.serverdrivenui3.ext.WithState2
import ship.f.engine.client.utils.serverdrivenui3.ext.toColor2
import ship.f.engine.client.utils.serverdrivenui3.ext.toShape2
import ship.f.engine.shared.utils.serverdrivenui2.ext.Base64.encodeToBase64
import ship.f.engine.shared.utils.serverdrivenui2.state.CameraGalleryState2

@Composable
actual fun CameraGallery2(
    s: MutableState<CameraGalleryState2>,
    m: Modifier,
) = s.WithState2(m) { modifier ->
    CameraGallery2(modifier = modifier)
}

@Composable
actual fun CameraGalleryState2.CameraGallery2(
    modifier: Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(false) }

    // --- iOS pickers (gallery + camera) ---
    val presentPicker = rememberIosImagePickerPresenter(
        onImagePicked = { uiImage ->
            coroutineScope.launch {
                val (bitmap, base64) = withContext(Dispatchers.Default) {
                    val bytes = uiImage?.toJpegBytes(quality = 0.92)
                    val b64 = bytes?.encodeToBase64()
                    val bmp = uiImage?.toImageBitmapOrNull()
                    bmp to b64
                }

                // Update your SDUI state (same as Android)
                update { copy(encodedBytes = base64) }

                imageBitmap = bitmap
            }
        }
    )

    if (imageSourceOptionDialog) {
        ImageSourceOptionDialog(
            onDismissRequest = { imageSourceOptionDialog = false },
            onGalleryRequest = {
                imageSourceOptionDialog = false
                launchGallery = true
            },
            onCameraRequest = {
                imageSourceOptionDialog = false
                launchCamera = true
            }
        )
    }

    if (launchGallery) {
        presentPicker.presentGallery()
        launchGallery = false
    }

    if (launchCamera) {
        presentPicker.presentCamera()
        launchCamera = false
    }

    val clickableModifier =
        modifier
            .clip(shape.toShape2())
            .clickable { imageSourceOptionDialog = true }

    Box {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "Profile",
                modifier = clickableModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            defaultImage?.let {
                RenderStatic(state = it, modifier = clickableModifier)
            } ?: Box(
                contentAlignment = Alignment.Center,
                modifier = clickableModifier.background(color.toColor2())
            ) {
                // optional placeholder content
            }
        }
    }
}
