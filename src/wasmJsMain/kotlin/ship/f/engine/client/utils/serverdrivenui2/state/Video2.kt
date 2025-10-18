package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ship.f.engine.client.utils.serverdrivenui2.ext.WithState2
import ship.f.engine.shared.utils.serverdrivenui2.state.VideoState2

@Composable
actual fun Video2(
    s: MutableState<VideoState2>,
    m: Modifier,
) = s.WithState2(m) { modifier ->
    Video2(modifier)
}
@Composable
actual fun VideoState2.Video2(
    modifier: Modifier,
) {
    Text("Video is not supported on this wasm platform")
}
