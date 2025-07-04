package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.state.ColorSchemeState
import ship.f.engine.shared.utils.serverdrivenui.state.State

@Composable
fun RenderScreen(
    screenConfig: MutableState<ScreenConfig>,
    client: CommonClient,
) {
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
        Screen(
            screenConfig = screenConfig,
            client = client,
        )
    }
}

@Composable
fun Screen(
    screenConfig: MutableState<ScreenConfig>,
    client: CommonClient,
) {
    LazyColumn {
        items(screenConfig.value.state) {
            client.RenderUI(
                element = client.getElement<State>(it.id).value,
            )
        }
    }
}

private fun ColorScheme.fromColorSchemeState(colorSchemeState: ColorSchemeState) = copy(
    primary = Color(colorSchemeState.primary),
    onPrimary = Color(colorSchemeState.onPrimary),
    onSecondaryContainer = Color(colorSchemeState.onSecondaryContainer),
    secondaryContainer = Color(colorSchemeState.secondaryContainer),
    background = Color(colorSchemeState.background),
    onBackground = Color(colorSchemeState.onBackground),
    surface = Color(colorSchemeState.surface),
    onSurface = Color(colorSchemeState.onSurface),
    surfaceVariant = Color(colorSchemeState.surfaceVariant),
    onSurfaceVariant = Color(colorSchemeState.onSurfaceVariant),
    outline = Color(colorSchemeState.outline),
)
