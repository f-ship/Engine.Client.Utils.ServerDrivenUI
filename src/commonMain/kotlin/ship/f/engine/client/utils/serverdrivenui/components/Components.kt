package ship.f.engine.client.utils.serverdrivenui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ship.f.engine.client.utils.serverdrivenui.RenderingContext
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.ID
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction.OnFieldUpdate
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
    ctx: RenderingContext,
) {
    Text(state.value.state.value)
}

@Composable
fun STextField(
    state: MutableState<StateHolder<FieldState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    // TODO to handle IME action we will need to do a little more work
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val updatedState = state.value.copy(
            state = state.value.state.copyLocalState(
                v = state.value.state.localState.copy(error = state.value.state.isValid(state.value.state.value))
            )
        )

        triggerActions.filterIsInstance<OnFieldUpdate>().forEach { triggerAction ->
            triggerAction.action.execute(
                //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                subject = Component(
                    component = updatedState.state,
                    id = id
                ),
                client = ctx.client,
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
        when(state.value.state.fieldType){
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
                val updatedState = state.value.copy(
                    state = state.value.state.copyValue(
                        v = it,
                    ).copyLocalState(
                        v = state.value.state.localState.copy(error = state.value.state.isValid(it))
                    )
                )
                state.value.state.value
                state.value = updatedState

                triggerActions.filterIsInstance<OnFieldUpdate>().forEach { triggerAction ->
                    triggerAction.action.execute(
                        //TODO We need to pull target out into the data structure for the triggers, or maybe onto the action
                        subject = Component(
                            component = updatedState.state,
                            id = id
                        ),
                        client = ctx.client,
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
                unfocusedTextColor = Color.Black, // TODO need to make a proper serializable theme
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
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
    ctx: RenderingContext,
) {
    Text("Toggle")
}

@Composable
fun SDropDown(
    state: MutableState<StateHolder<DropDownState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("DropDown")
}

@Composable
fun SRadioList(
    state: MutableState<StateHolder<RadioListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("RadioList")
}

@Composable
fun STickList(
    state: MutableState<StateHolder<TickListState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("TickList")
}

@Composable
fun SSearch(
    state: MutableState<StateHolder<SearchState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Search")
}

@Composable
fun SMenu(
    state: MutableState<StateHolder<MenuState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Menu")
}

@Composable
fun SBottomRow(
    state: MutableState<StateHolder<BottomRowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("BottomRow")
}

@Composable
fun SImage(
    state: MutableState<StateHolder<ImageState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Image")
}

@Composable
fun SVideo(
    state: MutableState<StateHolder<VideoState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Video")
}

@Composable
fun SCustom(
    state: MutableState<StateHolder<CustomState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Custom")
}

@Composable
fun SButton(
    state: MutableState<StateHolder<ButtonState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Button")
}

@Composable
fun SIcon(
    state: MutableState<StateHolder<IconState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Icon")
}

@Composable
fun SLoadingShimmer(
    state: MutableState<StateHolder<LoadingShimmerState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("LoadingShimmer")
}

@Composable
fun SDialog(
    state: MutableState<StateHolder<DialogState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Dialog")
}

@Composable
fun SSnackBar(
    state: MutableState<StateHolder<SnackBarState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("SnackBar")
}

@Composable
fun SLoader(
    state: MutableState<StateHolder<LoaderState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Loader")
}

@Composable
fun SUnknownComponent(
    state: MutableState<StateHolder<UnknownComponentState>>,
    triggerActions: List<TriggerAction>,
    fallback: ScreenConfig.Fallback,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Unknown Component")
}
