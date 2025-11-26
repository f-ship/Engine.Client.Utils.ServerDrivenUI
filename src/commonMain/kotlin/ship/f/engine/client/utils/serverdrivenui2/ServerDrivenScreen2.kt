package ship.f.engine.client.utils.serverdrivenui2

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui2.ext.*
import ship.f.engine.client.utils.serverdrivenui3.Render
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2
import ship.f.engine.shared.utils.serverdrivenui2.client.CommonClient2

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerDrivenScreen2(
    projectName: String? = null,
    resources: Map<String, Resource> = mapOf(),
    vectors: Map<String, ImageVector> = mapOf(),
    client: CommonClient2 = ClientHolder2.get(projectName),
    backStack: SnapshotStateList<BackStackEntry2> = client.reactiveBackStack
) {
    BlockingLaunchedEffect2(Unit){
        client.addResources(resources)
        client.addVectors(vectors)
    }
    backStack.lastOrNull()?.run {
        MaterialTheme(
            typography = state.toMaterialTypography(
                client = client,
            ),
            shapes = state.toMaterialShapes(
                client = client,
            ),
            colorScheme = state.toMaterialColorScheme(
                isSystemInDarkTheme = isSystemInDarkTheme(),
                client = client,
            ),
        ) {
            AnimatedContent(
                targetState = this,
                transitionSpec = { defaultTransitionSpec(direction) }
            ) { targetState ->
                BackHandler(client.canPop()) { client.pop() }
                Render(targetState.state)
            }
        }
    } ?: Text("There is nothing to display")
}