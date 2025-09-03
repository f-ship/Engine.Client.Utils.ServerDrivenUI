package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.*
import ship.f.engine.client.utils.serverdrivenui2.ext.*
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.JsonMeta2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.UIType2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ValidModifier2.Valid2
import ship.f.engine.shared.utils.serverdrivenui2.config.trigger.modifiers.OnToggleModifier2
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Composable
fun Spacer2(
    s: MutableState<SpacerState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Spacer(modifier = modifier)
}

@Composable
fun Text2(
    s: MutableState<TextState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Text(
        text = text,
        style = textStyle.toTextStyle2(fontWeight),
        textAlign = textAlign.toTextAlign2(),
        color = color.toColor2(),
        modifier = modifier,
    )
}

@Composable
fun TextField2(
    s: MutableState<TextFieldState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    remember(this.id) {
        val error = isError(text)
        update { copy(error = error, valid = Valid2(error == null)) }
    }

    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = text,
            visualTransformation = fieldType.toVisualTransformation2(),
            onValueChange = {
                val error = isError(it)
                update {
                    copy(
                        error = error,
                        text = it,
                        valid = Valid2((error == null)),
                    )
                }
            },
            isError = hasLostFocus && isError(text) != null,
            keyboardOptions = fieldType.toKeyboardOptions2(),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (isFocused && !focusState.isFocused && !hasLostFocus) {
                        println("hasLostFocus, $isFocused, ${focusState.isFocused}, $hasLostFocus")
                        update { copy(hasLostFocus = true, isFocused = focusState.isFocused) }
                    } else {
                        println("is Focused: ${focusState.isFocused}, $hasLostFocus")
                        update { copy(isFocused = focusState.isFocused) }
                    }
                },
            shape = shape.toShape2(),
            colors = textFieldDefaults2()
        )
        Spacer(modifier = Modifier.height(4.dp))
        AnimatedVisibility(visible = error != null && hasLostFocus) {
            Text(
                text = error.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
fun Switch2(
    s: MutableState<SwitchState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Switch(
        checked = if (modified) toggle.value else initialToggle ?: toggle.value,
        onCheckedChange = {
            update {
                copy(
                    toggle = OnToggleModifier2.Toggle2(it),
                    modified = true
                )
            }
            onToggleTrigger.trigger()
        },
        modifier = modifier,
    )
}

@Composable
fun CheckBox2(
    s: MutableState<CheckboxState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Checkbox(
        checked = if (modified) toggle.value else initialToggle ?: toggle.value,
        onCheckedChange = {
            onToggleTrigger.trigger(this.copy())
            update {
                copy(
                    toggle = OnToggleModifier2.Toggle2(it),
                    modified = true
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun Image2(
    s: MutableState<ImageState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    ToImage2(addOnClick(modifier = modifier))
}

@Composable
fun Button2(
    s: MutableState<ButtonState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    val leadingIcon: @Composable () -> Unit = {
        leadingIcon?.run {
            ToImage2(Modifier)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

    if (uiType !is UIType2.Tertiary2) {
        Button(
            onClick = { onClickTrigger.trigger() },
            shape = shape.toShape2(),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
            enabled = valid.value,
            colors = uiType.toButtonColors2(),
            modifier = modifier,
        ) {
            leadingIcon()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                text?.let {
                    Text(
                        text = it,
                        style = textStyle.toTextStyle2(fontWeight)
                    )
                }
                if (loading.value) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    } else {
        text?.let {
            Text(
                text = it,
                style = textStyle.toTextStyle2(fontWeight),
                modifier = modifier.clickable(enabled = valid.value, role = Role.Button) {
                    onClickTrigger.trigger()
                }
            )
        }
    }
}

@Composable
fun HorizontalDivider2(
    s: MutableState<HorizontalDividerState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    HorizontalDivider(modifier)
}

@Composable
fun VerticalDivider2(
    s: MutableState<VerticalDividerState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    VerticalDivider(modifier)
}

@Composable
fun DropDown2(
    s: MutableState<DropDownState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Column(
        modifier = modifier
            .height(IntrinsicSize.Max)
    ) {
        IconButton(onClick = { update { copy(isExpanded = !isExpanded) } }, modifier = Modifier.fillMaxWidth()) {
            Text(text = selectedItem?.title ?: items.first().title)
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { update { copy(isExpanded = false) } },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.title) },
                    onClick = { update { copy(selectedItem = it) } },
                )
            }
        }
    }
}

@Composable
fun Unknown2(
    s: MutableState<UnknownState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    // TODO The initialTrigger is what determines it's behaviour
}

@Composable
fun Card2(
    s: MutableState<CardState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Surface(
        shape = shape.toShape2(),
        border = border.toBorder2(),
        color = color.toColor2(),
        modifier = addOnClick(modifier = modifier),
    ) {
        Column(
            modifier = innerPadding.toModifier2(),
            verticalArrangement = arrangement.toVerticalArrangement2(),
            horizontalAlignment = alignment.toHorizontalAlignment2(),
        ) {
            children.forEach {
                C.Render(
                    state = it,
                    modifier = toModifier2(it.weight)
                )
            }
        }
    }
}

@Composable
fun Row2(
    s: MutableState<RowState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Row(
        horizontalArrangement = arrangement.toHorizontalArrangement2(),
        verticalAlignment = alignment.toVerticalAlignment2(),
        modifier = modifier //Look into whether we need this here or not
    ) {
        children.forEach {
            C.Render(
                state = it,
                modifier = toModifier2(it.weight)
            )
        }

    }
}

@Composable
fun Column2(
    s: MutableState<ColumnState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Column(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier.then(Modifier.background(color.toColor2())),
    ) {
        children.forEach {
            C.Render(
                state = it,
                modifier = toModifier2(it.weight)
            )
        }
    }
}

@Composable
fun Box2(
    s: MutableState<BoxState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Box(
        contentAlignment = alignment.toAlignment2(),
        modifier = modifier.then(Modifier.background(color.toColor2())),
    ) {
        children.forEach {
            C.Render(state = it)
        }
    }
}

@Composable
fun LazyRow2(
    s: MutableState<LazyRowState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    LazyRow(
        modifier = modifier,
        horizontalArrangement = arrangement.toHorizontalArrangement2(),
        verticalAlignment = alignment.toVerticalAlignment2(),
    ) {
        items(children) {
            C.Render(state = it)
        }
    }
}

@Composable
fun LazyColumn2(
    s: MutableState<LazyColumnState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    LazyColumn(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier
    ) {
        items(children) {
            C.Render(state = it)
        }
    }
}

@Composable
fun Screen2(
    s: MutableState<ScreenState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Column(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier
    ) {
        children.forEach {
            C.Render(
                state = it,
                modifier = toModifier2(it.weight)
            )
        }
    }
}

@Composable
fun ToField(json: JsonElement, key: String, level: Int = 0) {
    when(json) {
        is JsonArray -> json.forEachIndexed { index, j -> ToField(j, index.toString(), level + 1) }
        is JsonObject -> json.forEach { (key, value) ->
            ToField(value, key, level + 1)
        }
        is JsonPrimitive -> ToTextField(json, key, level)
        is JsonNull -> Unit
    }
}

@Composable
fun ToTextField(json: JsonPrimitive, key: String, level: Int) {
    TextField(
        value = json.content,
        label = { Text(key) },
        onValueChange = { /* Handle value change */ },
        modifier = Modifier.fillMaxWidth().padding(start = level.dp * 16)
    )
}
@Composable
fun Builder2(
    s: MutableState<BuilderState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    (C.get(metaId) as? JsonMeta2)?.json?.let {
        Text("${it.jsonObject["id"]}")
        ToField(it, "root")
    }
}
