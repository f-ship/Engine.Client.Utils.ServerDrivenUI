package ship.f.engine.client.utils.serverdrivenui2.state

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
import ship.f.engine.client.utils.serverdrivenui2.Render
import ship.f.engine.client.utils.serverdrivenui2.ext.WithState2
import ship.f.engine.client.utils.serverdrivenui2.ext.toColor2
import ship.f.engine.client.utils.serverdrivenui2.ext.toShape2
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
    // TODO Really bad implementation, but will do for now
    val coroutineScope = rememberCoroutineScope()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageSourceOptionDialog by remember { mutableStateOf(value = false) }
    var launchCamera by remember { mutableStateOf(value = false) }
    var launchGallery by remember { mutableStateOf(value = false) }

    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                val bytes = it?.toByteArray()?.encodeToBase64()
                println("Camera2: $bytes")
                val u = update { copy(encodedBytes = bytes) }
//                C.pathStateMap[path] = u //TODO temporary hack till update is rewritten and fixed
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.Default) {
                val bytes = it?.toByteArray()?.encodeToBase64()
                println("Gallery2: $bytes")
                val u = update { copy(encodedBytes = bytes) }
//                C.pathStateMap[path] = u //TODO temporary hack till update is rewritten and fixed
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
        }
    }

    if (imageSourceOptionDialog) {
        ImageSourceOptionDialog(onDismissRequest = {
            imageSourceOptionDialog = false
        }, onGalleryRequest = {
            imageSourceOptionDialog = false
            launchGallery = true
        }, onCameraRequest = {
            imageSourceOptionDialog = false
            launchCamera = true
        })
    }
    if (launchGallery) {
        galleryManager.launch()
        launchGallery = false
    }
    if (launchCamera) {
        cameraManager.launch()
        launchCamera = false
    }

    val clickableModifier = modifier.clip(shape.toShape2()).clickable { // TODO to not have this stuff hardcoded
        imageSourceOptionDialog = true
    }

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
                Render(state = it, modifier = clickableModifier)
            } ?: Box(
                contentAlignment = Alignment.Center,
                modifier = clickableModifier.background(color.toColor2())) {
//                Text(text = "Edit Photo") // TODO the edit should not be a part of this
            }
        }
    }
}