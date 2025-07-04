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
import ship.f.engine.client.utils.serverdrivenui.C
import ship.f.engine.client.utils.serverdrivenui.WithComponent
import ship.f.engine.client.utils.serverdrivenui.generated.resources.Res
import ship.f.engine.client.utils.serverdrivenui.generated.resources.compose_multiplatform
import ship.f.engine.client.utils.serverdrivenui.generated.resources.icon_back
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction.*
import ship.f.engine.shared.utils.serverdrivenui.state.*

@Composable
fun Space(
    element: MutableState<Component<SpaceState>>,
) {
    Spacer(modifier = Modifier.height(element.value.state.value.dp))
}

@Composable
fun SText(
    element: MutableState<Component<TextState>>,
) {
    val style = when (element.value.state.style) {
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
        text = element.value.state.value,
        style = style,
    )
}

@Composable
fun STextField(
    element: MutableState<Component<FieldState>>,
) = element.WithComponent {
    val c = C
    // TODO to handle IME action we will need to do a little more work
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val error = isValid(value)
        val updatedElement = element.value.update {
            copy(
                localState = localState.copy(error = error),
                valid = (error == null),
            )
        }

//        element.triggerActions.filterIsInstance<OnFieldUpdateTrigger>().forEach { triggerAction ->
//            triggerAction.action.execute(
//                //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
//                element = updatedElement,
//                client = c,
//            )
//        }

        updatedElement.trigger<OnFieldUpdateTrigger>(c)
    }

    val visualTransformation = remember(fieldType) {
        when (fieldType) {
            is FieldState.FieldType.Number, is FieldState.FieldType.Text -> VisualTransformation.None
            is FieldState.FieldType.Password -> PasswordVisualTransformation()
        }
    }

    val keyboardOptions = remember(fieldType) {
        when (fieldType) {
            is FieldState.FieldType.Number -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            is FieldState.FieldType.Text, is FieldState.FieldType.Password -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        }
    }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            visualTransformation = visualTransformation,
            onValueChange = {
                val error = isValid(value)
                val updatedElement = element.value.update {
                    copy(
                        localState = localState.copy(error = error),
                        valid = (error == null),
                        value = it
                    )
                }
//                element.triggerActions.filterIsInstance<OnFieldUpdateTrigger>().forEach { triggerAction ->
//                    //We need to re-enable this but not update current state anymore
//                    triggerAction.action.execute(
//                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
//                        element = updatedElement,
//                        client = c,
//                    )
//                }

                updatedElement.trigger<OnFieldUpdateTrigger>(c)
            },
            isError = if (localState.hasLostFocus) {
                isValid(value) != null
            } else {
                false
            },
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (isFocused && !focusState.isFocused) {
                        println("SDUI LOG ${label} Lost Focused")

                        // Only needs to run if hasLostFocus is false, a minor optimization to make in the future
                        val updatedElement = element.value.copy(
                            state = copy(localState = localState.copy(hasLostFocus = true))
                        )
//                        element.triggerActions.filterIsInstance<OnFieldUpdateTrigger>().forEach { triggerAction ->
//                            //We need to re-enable this but not update current state anymore
//                            triggerAction.action.execute(
//                                //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
//                                element = updatedElement,
//                                client = c,
//                            )
//                        }
                        updatedElement.trigger<OnFieldUpdateTrigger>(c)
                    }
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        println("SDUI LOG ${label} Is Focused")
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
        val error = localState.error
        AnimatedVisibility(visible = error != null && localState.hasLostFocus) {
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
    element: MutableState<Component<ToggleState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    var toggleModified by remember { mutableStateOf(false) }
    val c = C
    Switch(
        checked = if (toggleModified) element.value.state.value else element.value.state.initialState
            ?: element.value.state.value,
        onCheckedChange = {
            toggleModified = true
            val updatedState = element.value.state.copy(value = it)
            element.value = element.value.copy(state = updatedState)
            //TODO this is certainly cumbersome copy and pasting everywhere
            triggerActions.filterIsInstance<OnToggleUpdateTrigger>().forEach { triggerAction ->
                triggerAction.action.execute(
                    //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                    element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                    client = c,
                )
            }
        }
    )
}

@Composable
fun SDropDown(
    element: MutableState<Component<DropDownState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("DropDown")
}

@Composable
fun SRadioList(
    element: MutableState<Component<RadioListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("RadioList")
}

@Composable
fun STickList(
    element: MutableState<Component<TickListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("TickList")
}

@Composable
fun SSearch(
    element: MutableState<Component<SearchState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Search")
}

@Composable
fun SMenu(
    element: MutableState<Component<MenuState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Menu")
}

@Composable
fun SBottomRow(
    element: MutableState<Component<BottomRowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("BottomRow")
}

@Composable
fun SImage(
    element: MutableState<Component<ImageState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Image")
    when (val src = element.value.state.src) {
        is ImageState.Source.Resource -> Icon(
            painter = painterResource(
                when (src.resource) {
                    "icon-back" -> Res.drawable.icon_back
                    else -> Res.drawable.compose_multiplatform
                }
            ),
            contentDescription = element.value.state.accessibilityLabel,
        )

        is ImageState.Source.Url -> {
            AsyncImage(
                model = src.url,
                contentDescription = element.value.state.accessibilityLabel,
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
    element: MutableState<Component<VideoState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Video")
}

@Composable
fun SCustom(
    element: MutableState<Component<CustomState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Custom")
}

@Composable
fun SButton(
    element: MutableState<Component<ButtonState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    val c = C
    when (element.value.state.buttonType) {
        ButtonState.ButtonType.Primary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                        client = c,
                    )
                }

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                        client = c,
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 1.dp,
            ),
            enabled = element.value.state.valid ?: true,
        ) {
            Text(
                text = element.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Secondary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                        client = c,
                    )
                }

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
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
            enabled = element.value.state.valid ?: true,
        ) {
            Text(
                text = element.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Tertiary -> Button(
            onClick = {
                triggerActions.filterIsInstance<OnClickTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                        client = c,
                    )
                }

                //TODO for testing purposes
                c.pushScreen(testConfig)

                triggerActions.filterIsInstance<OnHoldTrigger>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        element = c.elementMap[id] ?: error("Element not found for ID: $id"),
                        client = c,
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            enabled = element.value.state.valid ?: true,
        ) {
            Text(
                text = element.value.state.value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun SIcon(
    element: MutableState<Component<IconState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Icon")
}

@Composable
fun SLoadingShimmer(
    element: MutableState<Component<LoadingShimmerState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("LoadingShimmer")
}

@Composable
fun SDialog(
    element: MutableState<Component<DialogState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Dialog")
}

@Composable
fun SSnackBar(
    element: MutableState<Component<SnackBarState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("SnackBar")
}

@Composable
fun SLoader(
    element: MutableState<Component<LoaderState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
) {
    Text("Loader")
}

@Composable
fun SUnknownComponent(
    element: MutableState<Component<UnknownComponentState>>,
    triggerActions: List<TriggerAction>,
    fallback: ScreenConfig.Fallback,
    id: ID,
) {
    Text("Unknown Component")
}
