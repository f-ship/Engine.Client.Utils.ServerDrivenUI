package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ship.f.engine.client.utils.serverdrivenui.CommonClient
import ship.f.engine.shared.utils.serverdrivenui.state.ColorSchemeState

/**
 * Composition Local of CommonClient to provide the client to elements
 */
val ClientProvider = staticCompositionLocalOf { CommonClient.getClient() }

/**
 * Convenience property to access the current client from within a Composable
 */
val C @Composable get() = ClientProvider.current

/**
 * Convenience method to convert a ColorSchemeState to a Material ColorScheme
 */
fun ColorScheme.fromColorSchemeState(colorSchemeState: ColorSchemeState) = copy(
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