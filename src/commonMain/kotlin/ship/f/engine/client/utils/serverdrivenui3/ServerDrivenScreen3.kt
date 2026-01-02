package ship.f.engine.client.utils.serverdrivenui3

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui3.ext.*
import ship.f.engine.shared.utils.serverdrivenui2.client3.BackStackEntry3.ScreenEntry
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ServerDrivenScreen3(
    projectName: String? = null,
    resources: Map<String, Resource> = mapOf(),
    vectors: Map<String, ImageVector> = mapOf(),
    toast: ServerToastEvent? = null,
    client: Client3 = client3,
    currentScreen: MutableState<ScreenEntry?> = client.navigationEngine.currentScreen,
    canPop: MutableState<Boolean> = client.navigationEngine.canPopState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(toast) {
        toast?.let { toastEvent ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = toastEvent.message,
                    actionLabel = toastEvent.actionText,
                    duration = when {
                        toastEvent.durationMs == null -> SnackbarDuration.Indefinite
                        toastEvent.durationMs < 3000 -> SnackbarDuration.Short
                        else -> SnackbarDuration.Long
                    },
                )
            }
        }
    }

    BlockingLaunchedEffect2(Unit) {
        client.addResources(resources)
        client.addVectors(vectors)
    }
    (currentScreen.value)?.run {
        MaterialTheme(
            typography = state2.toMaterialTypography(client),
            shapes = state2.toMaterialShapes(client),
            colorScheme = state2.toMaterialColorScheme(isSystemInDarkTheme(), client),
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = when(toast?.toastType){
                                ServerToastEvent.ToastType.Error -> Color.Red
                                ServerToastEvent.ToastType.Success -> Color.Green
                                ServerToastEvent.ToastType.Warning -> Color.Gray
                                null -> Color.Gray
                            },
                            contentColor = Color.White,
                            actionColor = Color.White,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                    }
                }
            ) { padding ->
                AnimatedContent(
                    targetState = this,
                    transitionSpec = { defaultTransitionSpec(direction = direction2) },
                    modifier = Modifier.fillMaxSize(),
                ) { targetState ->
                    BackHandler(canPop.value) { client.navigationEngine.pop() }
                    Render(targetState.state2)
                }
            }
        }
    }

}