package ship.f.engine.client.utils.serverdrivenui2

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui2.ext.*
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2
import ship.f.engine.shared.utils.serverdrivenui2.client.CommonClient2
import ship.f.engine.shared.utils.serverdrivenui2.client.CommonClient2.FocusDirection.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerDrivenScreen2(
    projectName: String? = null,
    resources: Map<String, Resource> = mapOf(),
    client: CommonClient2 = ClientHolder2.get(projectName),
    backStack: SnapshotStateList<BackStackEntry2> = client.reactiveBackStack
) {
    BlockingLaunchedEffect2(Unit){ client.addResources(resources) }
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
                Column {
                    // TODO to make a DebugCommonClient using a build variant Ugly Ugly Code
                    if (C.isDev) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Text(text = "Debug Mode")
                            Text(text = "Observing: ${C.focusedState.value?.name}")
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(text = "Up", modifier = Modifier.clickable {
                                    C.changeFocus(Up)
                                })
                                Text(text = "Down", modifier = Modifier.clickable {
                                    C.changeFocus(Down)
                                })
                                Text(text = "before", modifier = Modifier.clickable {
                                    C.changeFocus(Before)
                                })
                                Text(text = "after", modifier = Modifier.clickable {
                                    C.changeFocus(After)
                                })
                            }
                        }
                    }
                    Render(targetState.state)
                }
            }
        }
    } ?: Text("There is nothing to display")
}