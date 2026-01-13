package ship.f.engine.client.utils.serverdrivenui3.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*
import ship.f.engine.client.utils.serverdrivenui3.Render
import ship.f.engine.client.utils.serverdrivenui3.ext.*
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.config.action.models.UpdateZoneModel3
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.JsonMeta2
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.ZoneViewModel3
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.ColorScheme2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.FontWeight2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.IMEType2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.UIType2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.LiveValue3
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.Ref3
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.value.ListValue
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.value.SingleConditionalValue
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.value.StringValue
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ValidModifier2.Valid2
import ship.f.engine.shared.utils.serverdrivenui2.config.trigger.modifiers.OnToggleModifier2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Composable
fun Spacer2(
    s: MutableState<SpacerState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Spacer2(modifier = modifier)
}

@Composable
fun SpacerState2.Spacer2(
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier)
}

@Composable
fun Text2(
    s: MutableState<TextState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Text2(modifier = modifier)
}

@Composable
fun TextState2.Text2(
    modifier: Modifier = Modifier,
) {
    var value by remember(text) {
        mutableStateOf(
            liveText3?.let {
                val liveValue = if (overrideScope && it is LiveValue3.ReferenceableLiveValue3) {
                    (it.ref as? Ref3.VmRef3)
                        ?.let { ref ->  ref.copy(vm = ref.vm.copy(scope = id.scope)) }
                        ?.let { ref -> LiveValue3.ReferenceableLiveValue3(ref) }
                        ?: it
                } else it
                client3.computationEngine.getValue(liveValue) as? StringValue
            }?.value ?: text
        )
    }
    var showMore by remember { mutableStateOf(false) }
    var showingMore by remember { mutableStateOf(false) }
    if (!showingMore) {
        limit?.let {
            value = if (it > value.length) value else value.take(it) + "..."
            showMore = true
        }
    }

    if (spans != null) {
        val spanText = buildAnnotatedString {
            var cursor = 0
            spans!!.forEach { span ->
                addStyle(
                    style = SpanStyle(
                        color = span.color.toColor2(),
                        textDecoration = if (span.underline) TextDecoration.Underline else TextDecoration.None,
                        fontSize = span.fontSize.sp,
                        fontWeight = span.fontWeight.toFontWeight2(),
                    ),
                    start = cursor,
                    end = cursor + span.text.length
                )
                append(span.text)
                cursor += span.text.length
            }
        }

        Text(
            text = spanText,
            textAlign = textAlign.toTextAlign2(),
            modifier = modifier,
        )

    } else {
        Text(
            text = value,
            style = textStyle.toTextStyle2(fontWeight),
            textAlign = textAlign.toTextAlign2(),
            color = color.toColor2(),
            textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None,
            modifier = modifier,
        )

        if (showMore && !showingMore) {
            AnimatedVisibility(visible = showMore) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Read More",
                    style = textStyle.toTextStyle2(fontWeight),
                    textAlign = textAlign.toTextAlign2(),
                    textDecoration =  TextDecoration.Underline,
                    color = ColorScheme2.Color2.Primary.toColor2(),
                    modifier = padding.toModifier2().fillMaxWidth().clickable(enabled = true, role = Role.Button) {
                        showingMore = !showingMore
                        value = text
                    }
                )
            }
        }
    }
}

@Composable
fun TextField2(
    s: MutableState<TextFieldState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    TextField2(modifier = modifier)
}

@Composable
fun TextFieldState2.TextField2(
    modifier: Modifier = Modifier,
) {
    remember(this.id) {
        val error = isError(text)
        update { copy(error = error, valid = Valid2(error == null)) }
    }

    Column(modifier) {
        if (label.isNotEmpty()) {
            Row {
                Text(
                    text = label,
                    style = labelTextStyle.toTextStyle2(FontWeight2.Medium2),
                    color = labelColor.toColor2(),
                )
                if (validations.any { it.isRequired} ) {
                    Text(
                        text = "*",
                        color = ColorScheme2.Color2.Primary.toColor2(),
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        var isModified by remember { mutableStateOf(false) }
        var text by remember(text) { mutableStateOf(text) }
        var value by remember(text) {
            mutableStateOf(
                if (isModified) text else liveText3?.let { client3.computationEngine.getValue(it) as? StringValue }?.value ?: text
            )
        }

        OutlinedTextField(
            value = value,
            visualTransformation = fieldType.toVisualTransformation2(),
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { { it.ToImage2(it.toModifier2()) } },
            trailingIcon = trailingIcon?.let { { it.ToImage2(it.toModifier2()) } },
            onValueChange = {
                text = it
                val error = isError(it)
                isModified = true
                updateCommit {
                    copy(
                        error = error,
                        text = it,
                        valid = Valid2((error == null)),
                    )
                }
                if (liveText3 != null) {
                    (liveText3 as? LiveValue3.ReferenceableLiveValue3)?.let { referenceable ->
                        (referenceable.ref as? Ref3.VmRef3)?.let { vmRef ->
                            UpdateZoneModel3(
                                targetMetaId = vmRef.vm,
                                liveValue = LiveValue3.StaticLiveValue3(StringValue(it)),
                                operation = UpdateZoneModel3.Operation2.Set(vmRef.property)
                            ).run3(this@TextField2, client3)
                        }
                    }
                }
            },
            isError = hasLostFocus && isError(text) != null,
            keyboardOptions = fieldType.toKeyboardOptions2().copy(
                imeAction = if (imeType is IMEType2.Next2) ImeAction.Next else ImeAction.Unspecified
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (isFocused && !focusState.isFocused && !hasLostFocus) {
                        println("hasLostFocus, $isFocused, ${focusState.isFocused}, $hasLostFocus")
                        updateCommit { copy(hasLostFocus = true, isFocused = focusState.isFocused) }
                    } else {
                        println("is Focused: ${focusState.isFocused}, $hasLostFocus")
                        updateCommit { copy(isFocused = focusState.isFocused) }
                    }
                },
            shape = shape.toShape2(),
            colors = textFieldDefaults2().copy(
                unfocusedIndicatorColor = borderColor.toColor2(),
                unfocusedPlaceholderColor = placeholderColor.toColor2(),
                unfocusedTextColor = textColor.toColor2(),
                unfocusedContainerColor = containerColor.toColor2(),
                focusedContainerColor = containerColor.toColor2(),
                focusedTextColor = focusedTextColor.toColor2(),
            ),
            textStyle = textStyle.toTextStyle2(FontWeight2.Regular2),
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
fun Search2(
    s: MutableState<SearchState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Search2(modifier)
}

@Composable
fun SearchState2.Search2(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var text by remember(text) { mutableStateOf(text) }
        OutlinedTextField(
            value = text,
            leadingIcon = leadingIcon?.let { { it.ToImage2(it.toModifier2()) } },
            visualTransformation = fieldType.toVisualTransformation2(),
            placeholder = { Text(placeholder) },
            onValueChange = {
                text = it
                updateCommit { copy(text = it) }
                onFieldUpdateTrigger.trigger()
            },
            keyboardOptions = fieldType.toKeyboardOptions2(),
            shape = shape.toShape2(),
            colors = textFieldDefaults2().copy(
                unfocusedIndicatorColor = borderColor.toColor2(),
                unfocusedPlaceholderColor = placeholderColor.toColor2(),
                unfocusedTextColor = textColor.toColor2(),
                unfocusedContainerColor = containerColor.toColor2(),
                focusedContainerColor = containerColor.toColor2(),
                focusedTextColor = focusedTextColor.toColor2(),
            ),
            textStyle = textStyle.toTextStyle2(FontWeight2.Regular2),
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(24.dp))
        // TODO to not use trailing Icon like this
        trailingIcon?.let {
            it.ToImage2(it.toModifier2().clickable(enabled = true, role = Role.Button) {
                it.onClickTrigger.trigger()
                if (clearTextOnClick) updateCommit { copy(text = "") }
            })
            Spacer(Modifier.width(8.dp))
        }
        clearIcon?.let {
            if (text.isNotEmpty()) {
                it.ToImage2(it.toModifier2().clickable(enabled = true, role = Role.Button) {
                    it.onClickTrigger.trigger()
                })
                Spacer(Modifier.width(8.dp))
            }
        }
    }

}

@Composable
fun Switch2(
    s: MutableState<SwitchState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Switch2(modifier)
}

@Composable
fun SwitchState2.Switch2(
    modifier: Modifier = Modifier,
) {
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

// Need to upgrade to a custom implementation to enable rounding
@Composable
fun CheckBox2(
    s: MutableState<CheckboxState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    CheckBox2(modifier)
}

@Composable
fun CheckboxState2.CheckBox2(
    modifier: Modifier = Modifier,
) {
//    sduiLog(modified, toggle.value, initialToggle, path3, id, client3.idPaths[id], header = "", tag = "CheckBox2")
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
            client3.commit()
        },
        modifier = modifier,
        colors = CheckboxDefaults.colors().copy(
            uncheckedBoxColor = uncheckedBoxColor.toColor2(),
            uncheckedBorderColor = uncheckedBorderColor.toColor2(),
        ),
    )
}

@Composable
fun Image2(
    s: MutableState<ImageState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Image2(modifier)
}

@Composable
fun ImageState2.Image2(
    modifier: Modifier = Modifier,
) {
    ToImage2(addOnClick(modifier = modifier))
}

@Composable
expect fun Video2(
    s: MutableState<VideoState2>,
    m: Modifier = Modifier,
)

@Composable
expect fun VideoState2.Video2(
    modifier: Modifier = Modifier,
)

@Composable
expect fun CameraGallery2(
    s: MutableState<CameraGalleryState2>,
    m: Modifier = Modifier,
)

@Composable
expect fun CameraGalleryState2.CameraGallery2(
    modifier: Modifier = Modifier,
)

@Composable
fun WebView2(
    s: MutableState<WebViewState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    WebView2(modifier)
}

@Composable
fun WebViewState2.WebView2(
    modifier: Modifier = Modifier,
) {
    var showWebView by remember { mutableStateOf(true) }
    if (showWebView) {
        Box(modifier = modifier.background(Color.White).fillMaxSize()) {
            val state = rememberWebViewState(url = config.url) {
                androidWebSettings.domStorageEnabled = true
            }
            val requestInterceptor = remember {
                object : RequestInterceptor {
                    override fun onInterceptUrlRequest(
                        request: WebRequest,
                        navigator: WebViewNavigator
                    ): WebRequestInterceptResult {
                        val navigation =
                            config.whitelist.firstOrNull { navigation -> request.url.startsWith("https://" + navigation.url) }
                        return if (navigation != null) {
                            val allParams = request.url.split("?").getOrNull(1)?.split("&")?.associate {
                                it.split("=").let { (key, value) -> key to value }
                            }

                            if (allParams != null && navigation.params.isNotEmpty()) {
                                // TODO send something to the backend
                            }
                            navigation.destination?.config?.let { client3.navigationEngine.navigate(it.operation) }
                            WebRequestInterceptResult.Allow
                        } else if (config.whitelist.isEmpty()) {
                            WebRequestInterceptResult.Allow
                        } else {
                            WebRequestInterceptResult.Reject.also {
                                showWebView = false
                            } // TODO I might need to include url first
                            // TODO need to also close out of the webview if this happens
                        }
                    }
                }
            }

            val navigator = rememberWebViewNavigator(requestInterceptor = requestInterceptor)

            DisposableEffect(Unit) {
                onDispose {
                    try {
                        navigator.stopLoading()
                    } catch (_: Throwable) { /* ignore */ }
                }
            }

            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator( // TODO this can definitely be improved as it's not very clear
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            sduiLog(state.errorsForCurrentRequest, tag = "EngineX > WebView2")
            WebView(
                state = state,
                navigator = navigator,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun Button2(
    s: MutableState<ButtonState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Button2(modifier)
}

@Composable
fun ButtonState2.Button2(
    modifier: Modifier = Modifier,
) {
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
            border = buttonBorderColor?.let { BorderStroke(1.dp, color = it.toColor2()) },
            modifier = modifier,
        ) {
            leadingIcon()
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // TODO hardcoded to ensure column fills button
            ) {
                text?.let {
                    Text(
                        text = it,
                        color = textColor?.toColor2() ?: Color.Unspecified,
                        style = textStyle.toTextStyle2(fontWeight)
                    )
                }
                if (loading.value) {
                    Spacer(modifier = Modifier.width(8.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 3.dp,
                    )
                }
            }
        }
    } else {
        text?.let {
            Text(
                text = it,
                style = textStyle.toTextStyle2(fontWeight),
                color = MaterialTheme.colorScheme.primary,
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
    HorizontalDivider2(modifier)
}

@Composable
fun HorizontalDividerState2.HorizontalDivider2(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier,
        color = color.toColor2(),
        pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
    )
}
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
    pathEffect: PathEffect? = null
) =
    Canvas(modifier.fillMaxWidth().height(thickness)) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            start = Offset(0f, thickness.toPx() / 2),
            end = Offset(size.width, thickness.toPx() / 2),
            pathEffect = pathEffect,
        )
    }


@Composable
fun VerticalDivider2(
    s: MutableState<VerticalDividerState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    VerticalDivider2(modifier)
}

@Composable
fun VerticalDividerState2.VerticalDivider2(
    modifier: Modifier = Modifier,
) {
    VerticalDivider(modifier)
}

@Composable
fun DropDown2(
    s: MutableState<DropDownState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    DropDown2(modifier)
}

@Suppress("UNCHECKED_CAST")
@Composable
fun DropDownState2.DropDown2(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Max)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable(true) { updateCommit { copy(isExpanded = !isExpanded) } }
        ) {
            val selectedItems = mutableStateOf(
                liveSelectedTitle?.let {
                    (client3.computationEngine.getValue(it) as? ListValue<StringValue>)
                }?.value
            )

            Text(
                text = selectedItems.value?.joinToString(", ") { sv -> sv.value }?.ifEmpty { null }
                    ?: liveTitle?.let { (client3.computationEngine.getValue(it) as? StringValue)?.value }
                    ?: title,
                color = titleColor.toColor2(),
                style = titleTextStyle.toTextStyle2(FontWeight2.Regular2),
                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp).weight(1f) //TODO may be unnecessary
            )
            if (isExpanded) arrowDropUpIcon?.ToImage2(Modifier) else {
                if (selectedItems.value?.isNotEmpty() == true) arrowDropDownIcon?.copy(color = ColorScheme2.Color2.Primary)
                    ?.ToImage2(Modifier)
                else arrowDropDownIcon?.ToImage2(Modifier)
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { updateCommit { copy(isExpanded = false) } },
            modifier = Modifier
                .fillMaxWidth()
                .crop(vertical = 8.dp),
            containerColor = menuColor2.toColor2(),
        ) {
            children.forEach { Render(it) }
        }
    }
}

@Composable
fun Unknown2(
    s: MutableState<UnknownState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Unknown2(modifier)
}

@Composable
fun UnknownState2.Unknown2(
    modifier: Modifier = Modifier,
) {
    // TODO The initialTrigger is what determines it's behaviour
}

@Composable
fun Card2(
    s: MutableState<CardState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Card2(modifier)
}

@Composable
fun CardState2.Card2(
    modifier: Modifier = Modifier,
) {
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
                Render(
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
    Row2(modifier)
}

@Composable
fun RowState2.Row2(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = arrangement.toHorizontalArrangement2(),
        verticalAlignment = alignment.toVerticalAlignment2(),
        modifier = modifier.then(addOnClick(modifier).then(Modifier.background(color.toColor2()))),
    ) {
        children.forEach {
            Render(
                state = it,
                modifier = toModifier2(it.weight)
            )
        }
    }
}

@Composable
fun FlowRow2(
    s: MutableState<FlowRowState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    FlowRow2(modifier)
}

@Composable
fun FlowRowState2.FlowRow2(
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = arrangement.toHorizontalArrangement2(),
        modifier = modifier
    ) {
        children.forEach {
            Render(
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
    Column2(modifier)
}

@Composable
fun ColumnState2.Column2(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier
            .then(Modifier.background(color.toColor2()))
            .then(addOnClick(modifier)),
    ) {
        children.forEach {
            Render(
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
    Box2(modifier)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxState2.Box2(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = alignment.to2DAlignment2(),
        modifier = modifier.then(Modifier.background(color = color.toColor2(), shape = shape.toShape2()))
    ) {
        children.forEach {
            Render(
                state = it,
            )
        }
    }
}

@Composable
fun Variant2(
    s: MutableState<VariantState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Variant2(modifier)
}

@Composable
fun VariantState2.Variant2(
    modifier: Modifier = Modifier,
) {
    val defaultVariant by remember {
        mutableStateOf((client3.computationEngine.getValue(defaultVariant) as? StringValue)?.value)
    }

    // TODO need to make this recursive
    val variantValue by remember(counter) {
        mutableStateOf(
            when (val value = client3.computationEngine.getValue(liveValue = variant, state2 = this@Variant2)) {
                is SingleConditionalValue -> {
                    val a = client3.computationEngine.computeConditionalValue(value, state2 = this@Variant2)
                    a as? StringValue
                }
                is StringValue -> value
                else -> null
            }
        )
    }
    Box(
        modifier = modifier,
        contentAlignment = alignment.to2DAlignment2()
    ) {
        children.find { it.id.name == variantValue?.value || it.id.alias == variantValue?.value }?.let {
            Render(state = it)
        } ?: children.find { it.id.name == defaultVariant || it.id.alias == defaultVariant }?.let {
            Render(state = it)
        } ?: Text("No Variant Found") // TODO this should only be here for debugging purposes
    }
}

@Composable
fun LazyRow2(
    s: MutableState<LazyRowState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    LazyRow2(modifier)
}

@Composable
fun LazyRowState2.LazyRow2(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = arrangement.toHorizontalArrangement2(),
        verticalAlignment = alignment.toVerticalAlignment2(),
    ) {
        items(children) {
            Render(state = it)
        }
    }
}

@Composable
fun LazyColumn2(
    s: MutableState<LazyColumnState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    LazyColumn2(modifier)
}

@Composable
fun LazyColumnState2.LazyColumn2(
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(focus) {
        focus?.let {
            listState.animateScrollToItem(it.value)
        }
    }

    LaunchedEffect(key1 = children, key2 = filteredChildren) {
        sduiLog( children.size, tag = "EngineX > LazyColumn") { id.name == "ChatMessageContainer"}
        sduiLog( children.map { it.id }, tag = "EngineX > LazyColumn") { id.name == "ChatMessageContainer"}
        sduiLog(children.map { (it.metas.firstOrNull() as? ZoneViewModel3)?.map["message"] }, tag = "EngineX > LazyColumn") { id.name == "ChatMessageContainer"}
        // TODO my theory for this bug is that I am using a mutable map so I just need to copy the map and I'll be scot free
        // TODO However what is this issue with only retrieving 40 messages
        // TODO restarting the server seemed to update how many chats we can get, this must be due to overly caching
        scrollToBottom?.let {
            listState.animateScrollToItem((filteredChildren ?: children).lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = innerPadding.start.dp,
            end = innerPadding.end.dp,
            top = innerPadding.top.dp,
            bottom = innerPadding.bottom.dp
        ),
    ) {
        items(items = filteredChildren ?: children) { child ->
            Render(state = child)
        }
    }
}

@Composable
fun LazyGrid2(
    s: MutableState<LazyGridState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    LazyGrid2(modifier)
}

@Composable
fun LazyGridState2.LazyGrid2(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = innerPadding.start.dp,
            end = innerPadding.end.dp,
            top = innerPadding.top.dp,
            bottom = innerPadding.bottom.dp
        ),
        columns = GridCells.Adaptive(64.dp), // TODO this shouldn't be hardcoded
    ) {
        items(children) {
            Render(state = it)
        }
    }
}

@Composable
fun Dialog2(
    s: MutableState<DialogState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Dialog2(modifier)
}

@Composable
fun DialogState2.Dialog2(
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = { onDismissTrigger.trigger() }) {
        LazyColumn(modifier) {
            items(children) {
                Render(state = it)
            }
        }
    }
}

@Composable
fun Screen2(
    s: MutableState<ScreenState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Screen2(modifier)
}

@Composable
fun ScreenState2.Screen2(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = arrangement.toVerticalArrangement2(),
        horizontalAlignment = alignment.toHorizontalAlignment2(),
        modifier = modifier.background(Color.Transparent),
    ) {
        children.forEach {
            Render(
                state = it,
                modifier = toModifier2(it.weight)
            )
        }
    }
}

@Composable
fun Scaffold2(
    s: MutableState<ScaffoldState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    Scaffold2(modifier)
}

@Composable
fun ScaffoldState2.Scaffold2(
    modifier: Modifier = Modifier,
) {
    if (children.size != 3) throw IllegalStateException("Scaffold must have exactly 3 children")
    Scaffold(
        topBar = {
            Render(state = children[0])
        },
        bottomBar = {
            Render(state = children[2])
        },
        containerColor = color.toColor2(),
    ) { paddingValues ->
        Column(
            verticalArrangement = arrangement.toVerticalArrangement2(),
            horizontalAlignment = alignment.toHorizontalAlignment2(),
            modifier = modifier.padding(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                top = paddingValues.calculateTopPadding(),
            )
        ) {
            Render(state = children[1])
        }
    }
}

@Composable
fun ToField(json: JsonElement, key: String, level: Int = 0) {
    when (json) {
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
    Builder2(modifier)
}

@Composable
fun BuilderState2.Builder2(
    modifier: Modifier = Modifier,
) {
    (client3.get(metaId) as? JsonMeta2)?.json?.let {
        Text("Observing ${(it.jsonObject["id"] as? JsonObject)?.getValue("name")}")
        ToField(it, "root")
    }
}

@Composable
fun FadeIn2(
    s: MutableState<FadeInState2>,
    m: Modifier = Modifier,
) = s.WithState2(m) { modifier ->
    FadeIn2(modifier)
}

@Composable
fun FadeInState2.FadeIn2(
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        delay(delay.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
    ) {
        children.forEach {
            Render(
                state = it,
            )
        }
    }
}
