package ship.f.engine.client.utils.serverdrivenui3.util

import androidx.compose.runtime.Composable

// CameraManager.kt
@Composable
expect fun rememberCameraManager(onResult: (SharedImage?) -> Unit): CameraManager

expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}