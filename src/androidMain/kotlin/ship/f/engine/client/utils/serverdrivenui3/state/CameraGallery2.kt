package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ship.f.engine.client.utils.serverdrivenui3.RenderStatic
import ship.f.engine.client.utils.serverdrivenui3.ext.WithState2
import ship.f.engine.client.utils.serverdrivenui3.ext.toColor2
import ship.f.engine.client.utils.serverdrivenui3.ext.toShape2
import ship.f.engine.client.utils.serverdrivenui3.util.ImageSourceOptionDialog
import ship.f.engine.client.utils.serverdrivenui3.util.RequestCameraAndGalleryPermissions
import ship.f.engine.client.utils.serverdrivenui3.util.rememberCameraManager
import ship.f.engine.client.utils.serverdrivenui3.util.rememberGalleryManager
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.PopulatedSideEffectMeta2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
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
    var isLoading by remember { mutableStateOf(false) }

    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                isLoading = true
            }
            val bitmap = withContext(Dispatchers.Default) {
                val bytes = it?.toByteArray()
                bytes?.let { b -> update { copy(bytes = b) } }
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
            withContext(Dispatchers.Main) {
                isLoading = false
            }
        }
    }

    val galleryManager = rememberGalleryManager {
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                isLoading = true
            }
            val bitmap = withContext(Dispatchers.Default) {
                val bytes = it?.toByteArray()
                bytes?.let { b -> update { copy(bytes = b) } }
                it?.toImageBitmap()
            }
            imageBitmap = bitmap
            withContext(Dispatchers.Main) {
                isLoading = false
            }
        }
    }

    RequestCameraAndGalleryPermissions { granted ->
        if (granted) {
            sduiLog("Permissions granted, can use camera and gallery", tag = "EngineX > CameraGallery2 > RequestCameraAndGalleryPermissions")
        } else {
            client3.emitSideEffect(
                PopulatedSideEffectMeta2(
                    metaId = Id2.MetaId2("%SDUIToast%", "Permission Denied"),
                )
            )
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

    Box(
        contentAlignment = Alignment.Center
    ) {
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
                modifier = clickableModifier.background(color.toColor2())) {
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp),
                strokeWidth = 5.dp,
            )
        }
    }
}