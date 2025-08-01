package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import ship.f.engine.client.utils.serverdrivenui.CommonClient
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig

@Composable
fun rememberCommonClient(config: ScreenConfig) = remember(config) { CommonClient.getClient().apply { navigate(config) } }

/**
 * Composition Local of CommonClient to provide the client to elements
 */
val ClientProvider = staticCompositionLocalOf { CommonClient.getClient() }

/**
 * Convenience property to access the current client from within a Composable
 */
val C @Composable get() = ClientProvider.current
