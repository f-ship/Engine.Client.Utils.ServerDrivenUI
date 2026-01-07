package ship.f.engine.client.utils.serverdrivenui3.util

import android.Manifest
import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.PopulatedSideEffectMeta2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2

// CameraManager.android.kt
@Composable
actual fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult.invoke(SharedImage(BitmapUtils.getBitmapFromUri(tempPhotoUri, contentResolver)))
            }
        }
    )
    return remember {
        CameraManager(
            onLaunch = {
                tempPhotoUri = SDUIFileProvider.Companion.getImageUri(context)
                cameraLauncher.launch(tempPhotoUri)
            }
        )
    }
}

@Composable
fun RequestCameraAndGalleryPermissions(
    onResult: (granted: Boolean) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        onResult(result.values.all { it })
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        )
    }
}

actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        try {
            onLaunch()
        } catch(e: Exception){
            client3.emitSideEffect(
                PopulatedSideEffectMeta2(
                    metaId = Id2.MetaId2("%SDUIToast%", "Camera Permission Denied"),
                )
            )
        }
    }
}