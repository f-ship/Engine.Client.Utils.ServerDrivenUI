package ship.f.engine.client.utils.serverdrivenui3

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui2.ext.BlockingLaunchedEffect2
import ship.f.engine.client.utils.serverdrivenui2.ext.defaultTransitionSpec
import ship.f.engine.client.utils.serverdrivenui3.ext.toMaterialColorScheme
import ship.f.engine.client.utils.serverdrivenui3.ext.toMaterialShapes
import ship.f.engine.client.utils.serverdrivenui3.ext.toMaterialTypography
import ship.f.engine.client.utils.serverdrivenui2.Render
import ship.f.engine.shared.utils.serverdrivenui2.client3.BackStackEntry3
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerDrivenScreen3(
    projectName: String? = null,
    resources: Map<String, Resource> = mapOf(),
    vectors: Map<String, ImageVector> = mapOf(),
    client: Client3 = client3,
    currentScreen: MutableState<BackStackEntry3?> = client.navigationEngine.currentScreen
) {
    BlockingLaunchedEffect2(Unit) {
        client.addResources(resources)
        client.addVectors(vectors)
    }
    (currentScreen.value as? BackStackEntry3.ScreenEntry)?.run {
        MaterialTheme(
            typography = state2.toMaterialTypography(client),
            shapes = state2.toMaterialShapes(client),
            colorScheme = state2.toMaterialColorScheme(isSystemInDarkTheme(), client),
        ) {
            AnimatedContent(
                targetState = this,
                transitionSpec = { defaultTransitionSpec(direction = direction2) }
            ) { targetState ->
                BackHandler(client.navigationEngine.canPop()) { client.navigationEngine.pop() }
                Render(targetState.state2)
            }
        }
    }

}