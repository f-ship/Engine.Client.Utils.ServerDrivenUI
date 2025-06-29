package ship.f.engine.client.utils.serverdrivenui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import ship.f.engine.client.utils.serverdrivenui.CommonClient
import ship.f.engine.client.utils.serverdrivenui.generated.resources.Res
import ship.f.engine.client.utils.serverdrivenui.generated.resources.compose_multiplatform
import ship.f.engine.client.utils.serverdrivenui.generated.resources.icon_back
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.ID
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction.*
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.action.Subject.Component
import ship.f.engine.shared.utils.serverdrivenui.state.*

@Composable
fun Space(
    state: MutableState<StateHolder<SpaceState>>,
) {
    Spacer(modifier = Modifier.height(state.value.state.value.dp))
}

@Composable
fun SText(
    state: MutableState<StateHolder<TextState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    val style = when (state.value.state.style) {
        is TextState.Style.BodyLarge -> MaterialTheme.typography.bodyLarge
        is TextState.Style.BodyMedium -> MaterialTheme.typography.bodyMedium
        is TextState.Style.BodySmall -> MaterialTheme.typography.bodySmall
        is TextState.Style.DisplayLarge -> MaterialTheme.typography.displayLarge
        is TextState.Style.DisplayMedium -> MaterialTheme.typography.displayMedium
        is TextState.Style.DisplaySmall -> MaterialTheme.typography.displaySmall
        is TextState.Style.HeadlineLarge -> MaterialTheme.typography.headlineLarge
        is TextState.Style.HeadlineMedium -> MaterialTheme.typography.headlineMedium
        is TextState.Style.HeadlineSmall -> MaterialTheme.typography.headlineSmall
        is TextState.Style.LabelLarge -> MaterialTheme.typography.labelLarge
        is TextState.Style.LabelMedium -> MaterialTheme.typography.labelMedium
        is TextState.Style.LabelSmall -> MaterialTheme.typography.labelSmall
        is TextState.Style.TitleLarge -> MaterialTheme.typography.titleLarge
        is TextState.Style.TitleMedium -> MaterialTheme.typography.titleMedium
        is TextState.Style.TitleSmall -> MaterialTheme.typography.titleSmall
    }

    Text(
        text = state.value.state.value,
        style = style,
    )
}

@Composable
fun STextField(
    state: MutableState<StateHolder<FieldState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    // TODO to handle IME action we will need to do a little more work
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val error = state.value.state.isValid(state.value.state.value)
        val updatedState = state.value.state.copy(
            localState = state.value.state.localState.copy(error = error),
            valid = (error == null),
        )
        //TODO this is certainly cumbersome copy and pasting everywhere
        triggerActions.filterIsInstance<OnFieldUpdateTrigger>().forEach { triggerAction ->
            triggerAction.action.execute(
                //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                subject = Component(
                    component = updatedState,
                    id = id
                ),
                client = c,
            )
        }
    }

    val visualTransformation = remember(state.value.state.fieldType) {
        when (state.value.state.fieldType) {
            is FieldState.FieldType.Number, is FieldState.FieldType.Text -> VisualTransformation.None
            is FieldState.FieldType.Password -> PasswordVisualTransformation()
        }
    }

    val keyboardOptions = remember(state.value.state.fieldType) {
        when (state.value.state.fieldType) {
            is FieldState.FieldType.Number -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            is FieldState.FieldType.Text, is FieldState.FieldType.Password -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        }
    }

    Column {
        Text(
            text = state.value.state.label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = state.value.state.value,
            placeholder = {
                Text(
                    text = state.value.state.placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            visualTransformation = visualTransformation,
            onValueChange = {
                val error = state.value.state.isValid(it)
                val updatedState = state.value.state.copy(
                    value = it,
                    localState = state.value.state.localState.copy(error = error),
                    valid = (error == null),
                )

                state.value = state.value.copy(state = updatedState)

                triggerActions.filterIsInstance<OnFieldUpdateTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = updatedState,
                            id = id
                        ),
                        client = c,
                    )
                }
            },
            isError = if (state.value.state.localState.hasLostFocus) {
                state.value.state.isValid(state.value.state.value) != null
            } else {
                false
            },
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (isFocused && !focusState.isFocused) {
                        println("SDUI LOG ${state.value.state.label} Lost Focused")

                        // Only needs to run if hasLostFocus is false, a minor optimization to make in the future
                        val updatedState = state.value.copy(
                            state = state.value.state.copy(localState = state.value.state.localState.copy(hasLostFocus = true))
                        )
                        state.value = updatedState
                    }
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        println("SDUI LOG ${state.value.state.label} Is Focused")
                    }
                },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedPlaceholderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorTextColor = MaterialTheme.colorScheme.error,
                errorContainerColor = Color.Transparent
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))
        val error = state.value.state.localState.error
        AnimatedVisibility(visible = error != null && state.value.state.localState.hasLostFocus) {
            Text(
                text = error.orEmpty(), // This causes animation issues we will need to address later
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun SToggle(
    state: MutableState<StateHolder<ToggleState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    var toggleModified by remember { mutableStateOf(false) }

    Switch(
        checked = if (toggleModified) state.value.state.value else state.value.state.initialState
            ?: state.value.state.value,
        onCheckedChange = {
            toggleModified = true
            val updatedState = state.value.state.copy(value = it)
            //TODO this is certainly cumbersome copy and pasting everywhere
            triggerActions.filterIsInstance<OnToggleUpdateTrigger>().forEach { triggerAction ->
                triggerAction.action.execute(
                    //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                    subject = Component(
                        component = updatedState,
                        id = id
                    ),
                    client = c,
                )
            }
        }
    )
}

@Composable
fun SDropDown(
    state: MutableState<StateHolder<DropDownState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("DropDown")
}

@Composable
fun SRadioList(
    state: MutableState<StateHolder<RadioListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("RadioList")
}

@Composable
fun STickList(
    state: MutableState<StateHolder<TickListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("TickList")
}

@Composable
fun SSearch(
    state: MutableState<StateHolder<SearchState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Search")
}

@Composable
fun SMenu(
    state: MutableState<StateHolder<MenuState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Menu")
}

@Composable
fun SBottomRow(
    state: MutableState<StateHolder<BottomRowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("BottomRow")
}

@Composable
fun SImage(
    state: MutableState<StateHolder<ImageState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Image")
    when (val src = state.value.state.src) {
        is ImageState.Source.Resource -> Icon(
            painter = painterResource(
                when (src.resource) {
                    "icon-back" -> Res.drawable.icon_back
                    else -> Res.drawable.compose_multiplatform
                }
            ),
            contentDescription = state.value.state.accessibilityLabel,
        )

        is ImageState.Source.Url -> {
            AsyncImage(
                model = src.url,
                contentDescription = state.value.state.accessibilityLabel,
                modifier = Modifier.fillMaxWidth(),
                onError = {
                    println("Coil: Image failed to load: ${it.result.throwable}")
                }
            )
        }
    }
}

@Composable
fun SVideo(
    state: MutableState<StateHolder<VideoState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Video")
}

@Composable
fun SCustom(
    state: MutableState<StateHolder<CustomState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Custom")
}

@Composable
fun SButton(
    state: MutableState<StateHolder<ButtonState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    when (state.value.state.buttonType) {
        ButtonState.ButtonType.Primary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 1.dp,
            ),
            enabled = state.value.state.valid ?: true,
        ) {
            Text(
                text = state.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Secondary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 1.dp,
            ),
            enabled = state.value.state.valid ?: true,
        ) {
            Text(
                text = state.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Tertiary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }

                //TODO for testing purposes
                c.pushScreen(testConfig)

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = state.value.state,
                            id = id
                        ),
                        client = c,
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            enabled = state.value.state.valid ?: true,
        ) {
            Text(
                text = state.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun SIcon(
    state: MutableState<StateHolder<IconState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Icon")
}

@Composable
fun SLoadingShimmer(
    state: MutableState<StateHolder<LoadingShimmerState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("LoadingShimmer")
}

@Composable
fun SDialog(
    state: MutableState<StateHolder<DialogState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Dialog")
}

@Composable
fun SSnackBar(
    state: MutableState<StateHolder<SnackBarState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("SnackBar")
}

@Composable
fun SLoader(
    state: MutableState<StateHolder<LoaderState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Loader")
}

@Composable
fun SUnknownComponent(
    state: MutableState<StateHolder<UnknownComponentState>>,
    triggerActions: List<TriggerAction>,
    fallback: ScreenConfig.Fallback,
    id: ID,
    c: CommonClient,
) {
    Text("Unknown Component")
}
