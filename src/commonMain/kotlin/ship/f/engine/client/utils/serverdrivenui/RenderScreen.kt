package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ship.f.engine.client.utils.serverdrivenui.ext.ClientProvider
import ship.f.engine.client.utils.serverdrivenui.ext.fromColorSchemeState
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.state.State

/**
 * Entry point for rendering the screen using a screenConfig and a client
 */
@Composable
fun RenderScreen(
    projectName: String? = null,
    client: CommonClient = CommonClient.getClient(projectName),
    screenConfig: MutableState<ScreenConfig> = client.currentScreen,
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
            Column(modifier = Modifier.fillMaxSize()) {
                screenConfig.value.children.forEach {
                    client.RenderUI(
                        element = client.getElement<State>(it.id).value,
                    )
                }
            }
        }
    }
}
