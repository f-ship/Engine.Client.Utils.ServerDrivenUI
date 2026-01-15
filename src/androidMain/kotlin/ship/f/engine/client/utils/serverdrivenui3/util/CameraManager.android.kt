package ship.f.engine.client.utils.serverdrivenui3.util

import android.Manifest.permission.*
import android.content.ContentResolver
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.PopulatedSideEffectMeta2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog

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
        requestPermissions(launcher)
    }
}

fun requestPermissions(launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>){
    try {
        launcher.launch(
            arrayOf(
                CAMERA,
                if (SDK_INT > 32) READ_MEDIA_IMAGES else READ_EXTERNAL_STORAGE,
            )
        )
    } catch (e: Exception) {
        sduiLog("Error requesting permissions: ${e.message}", tag = "EngineX > CameraGallery2 > requestPermissions")
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