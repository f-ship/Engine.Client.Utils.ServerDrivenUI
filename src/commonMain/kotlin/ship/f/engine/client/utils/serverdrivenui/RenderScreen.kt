package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import ship.f.engine.client.utils.serverdrivenui.ext.ClientProvider
import ship.f.engine.client.utils.serverdrivenui.ext.fromColorSchemeState
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.state.State

/**
 * Entry point for rendering the screen using a screenConfig and a client
 */
@Composable
fun RenderScreen(
    screenConfig: MutableState<ScreenConfig>,
    client: CommonClient,
) {
    CompositionLocalProvider(ClientProvider provides client) {
        MaterialTheme(
            colorScheme = if (isSystemInDarkTheme()) {
                screenConfig.value.darkColorScheme?.let {
                    MaterialTheme.colorScheme.fromColorSchemeState(it)
                }
            } else {
                screenConfig.value.lightColorScheme?.let {
                    MaterialTheme.colorScheme.fromColorSchemeState(it)
                }
            } ?: MaterialTheme.colorScheme,
        ) {
            LazyColumn {
                items(screenConfig.value.state) {
                    client.RenderUI(
                        element = client.getElement<State>(it.id).value,
                    )
                }
            }
        }
    }
}
