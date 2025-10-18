package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.runtime.Composable

// GalleryManager.kt
@Composable
expect fun rememberGalleryManager(onResult: (SharedImage?) -> Unit): GalleryManager


expect class GalleryManager(
    onLaunch: () -> Unit
) {
    fun launch()
}