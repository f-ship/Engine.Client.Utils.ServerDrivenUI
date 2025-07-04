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
import ship.f.engine.client.utils.serverdrivenui.WithComponentState
import ship.f.engine.client.utils.serverdrivenui.generated.resources.Res
import ship.f.engine.client.utils.serverdrivenui.generated.resources.compose_multiplatform
import ship.f.engine.client.utils.serverdrivenui.generated.resources.icon_back
import ship.f.engine.client.utils.serverdrivenui.update
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Component
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction.*
import ship.f.engine.shared.utils.serverdrivenui.state.*

@Composable
fun Space(
    element: MutableState<Component<SpaceState>>,
) = element.WithComponentState {
    Spacer(modifier = Modifier.height(value.dp))
}

@Composable
fun SText(
    element: MutableState<Component<TextState>>,
) = element.WithComponentState {
    val style = when (style) {
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
        text = value,
        style = style,
    )
}

@Composable
fun STextField(
    element: MutableState<Component<FieldState>>,
) = element.WithComponentState {
    // TODO to handle IME action we will need to do a little more work
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val error = isValid(value)
        val updatedElement = element.update {
            copy(
                localState = localState.copy(error = error),
                valid = (error == null),
            )
        }
        updatedElement.trigger<OnFieldUpdateTrigger>()
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
                val updatedElement = element.update {
                    copy(
                        localState = localState.copy(error = error),
                        valid = (error == null),
                        value = it
                    )
                }

                updatedElement.trigger<OnFieldUpdateTrigger>()
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
                        val updatedElement = element.update {
                            copy(
                                localState = localState.copy(hasLostFocus = true),
                            )
                        }
                        updatedElement.trigger<OnFieldUpdateTrigger>()
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
) = element.WithComponentState {
    var toggleModified by remember { mutableStateOf(false) }
    Switch(
        checked = if (toggleModified) value else initialState ?: value,
        onCheckedChange = {
            toggleModified = true
            val updatedElement = element.update {
                copy(
                    value = it
                )
            }

            updatedElement.trigger<OnToggleUpdateTrigger>()
        }
    )
}

@Composable
fun SDropDown(
    element: MutableState<Component<DropDownState>>,
) = element.WithComponentState {
    Text("DropDown")
}

@Composable
fun SRadioList(
    element: MutableState<Component<RadioListState>>,
) = element.WithComponentState {
    Text("RadioList")
}

@Composable
fun STickList(
    element: MutableState<Component<TickListState>>,
) = element.WithComponentState {
    Text("TickList")
}

@Composable
fun SSearch(
    element: MutableState<Component<SearchState>>,
) = element.WithComponentState {
    Text("Search")
}

@Composable
fun SMenu(
    element: MutableState<Component<MenuState>>,
) = element.WithComponentState {
    Text("Menu")
}

@Composable
fun SBottomRow(
    element: MutableState<Component<BottomRowState>>,
) = element.WithComponentState {
    Text("BottomRow")
}

@Composable
fun SImage(
    element: MutableState<Component<ImageState>>,
) = element.WithComponentState {
    Text("Image")
    when (val src = src) {
        is ImageState.Source.Resource -> Icon(
            painter = painterResource(
                when (src.resource) {
                    "icon-back" -> Res.drawable.icon_back
                    else -> Res.drawable.compose_multiplatform
                }
            ),
            contentDescription = accessibilityLabel,
        )

        is ImageState.Source.Url -> {
            AsyncImage(
                model = src.url,
                contentDescription = accessibilityLabel,
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
) = element.WithComponentState {
    Text("Video")
}

@Composable
fun SCustom(
    element: MutableState<Component<CustomState>>,
) = element.WithComponentState {
    Text("Custom")
}

@Composable
fun SButton(
    element: MutableState<Component<ButtonState>>,
) = element.WithComponentState {
    val c = C
    when (buttonType) {
        ButtonState.ButtonType.Primary -> Button(
            onClick = {
                element.value.trigger<OnClickTrigger>()
                element.value.trigger<OnHoldTrigger>()
            },
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 1.dp,
            ),
            enabled = valid ?: true,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Secondary -> Button(
            onClick = {
                element.value.trigger<OnClickTrigger>()
                element.value.trigger<OnHoldTrigger>()
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
            enabled = valid ?: true,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
            )
        }

        ButtonState.ButtonType.Tertiary -> Button(
            onClick = {
                element.value.trigger<OnClickTrigger>()
                element.value.trigger<OnHoldTrigger>()

                //TODO for testing purposes
                c.pushScreen(testConfig)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
            ),
            enabled = valid ?: true,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = W900),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun SIcon(
    element: MutableState<Component<IconState>>,
) = element.WithComponentState {
    Text("Icon")
}

@Composable
fun SLoadingShimmer(
    element: MutableState<Component<LoadingShimmerState>>,
) = element.WithComponentState {
    Text("LoadingShimmer")
}

@Composable
fun SDialog(
    element: MutableState<Component<DialogState>>,
) = element.WithComponentState {
    Text("Dialog")
}

@Composable
fun SSnackBar(
    element: MutableState<Component<SnackBarState>>,
) = element.WithComponentState {
    Text("SnackBar")
}

@Composable
fun SLoader(
    element: MutableState<Component<LoaderState>>,
) = element.WithComponentState {
    Text("Loader")
}

@Composable
fun SUnknownComponent(
    element: MutableState<Component<UnknownComponentState>>,
) = element.WithComponentState {
    Text("Unknown Component")
}
