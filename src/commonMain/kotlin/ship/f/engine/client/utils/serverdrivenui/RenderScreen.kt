package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
            AnimatedContent(
                targetState = screenConfig.value,
                transitionSpec = {
                    // New screen enters from the right; old slides left out.
                    (slideInHorizontally(
                        initialOffsetX = { it },          // start just off the right edge
                        animationSpec = tween(300)
                    ) + fadeIn(tween(150))) togetherWith
                            (slideOutHorizontally(
                                targetOffsetX = { -it / 4 },      // slight parallax
                                animationSpec = tween(300)
                            ) + fadeOut(tween(150)))
                }
            ) { targetState ->
                Column(modifier = Modifier.fillMaxSize()) {
                    targetState.children.forEach {
                        client.RenderUI(
                            element = client.getElement<State>(it.id).value,
                        )
                    }
                }
            }
        }
    }
}
