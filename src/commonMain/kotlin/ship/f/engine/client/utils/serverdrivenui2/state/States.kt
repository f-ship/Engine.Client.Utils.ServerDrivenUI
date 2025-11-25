package ship.f.engine.client.utils.serverdrivenui2.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*
import ship.f.engine.client.utils.serverdrivenui2.Render
import ship.f.engine.client.utils.serverdrivenui2.ext.*
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.*
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.ZoneViewModel2.Property.IntProperty
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.ColorScheme2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.ColorScheme2.Color2.Primary
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.IMEType2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2.StateId2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.LiveValue2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.LiveValue2.ConditionalLiveValue2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.LiveValue2.Ref2.ZoneRef2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.LiveValue2.ReferenceableLiveValue2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.UIType2
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
    val value = liveText?.let { C.computeLiveText(it) } ?: text
    Text(
        text = value,
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
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        OutlinedTextField(
            value = text,
            visualTransformation = fieldType.toVisualTransformation2(),
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { { it.ToImage2(it.toModifier2()) } },
            trailingIcon = trailingIcon?.let { { it.ToImage2(it.toModifier2()) } },
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
            keyboardOptions = fieldType.toKeyboardOptions2().copy(
                imeAction = if (imeType is IMEType2.Next2) ImeAction.Next else ImeAction.Unspecified
            ),
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
    //TODO temporary hard coding on frontend
    var randomId by remember { mutableStateOf(getRandomString()) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = text,
            leadingIcon = leadingIcon?.let { { it.ToImage2(it.toModifier2()) } },
            visualTransformation = fieldType.toVisualTransformation2(),
            placeholder = { Text(placeholder) },
            onValueChange = { update { copy(text = it) } },
            keyboardOptions = fieldType.toKeyboardOptions2(),
            shape = shape.toShape2(),
            colors = textFieldDefaults2()
        )
        Spacer(Modifier.width(16.dp))
        // TODO to not use trailing Icon like this
        trailingIcon?.let {
            it.ToImage2(it.toModifier2().clickable(enabled = true, role = Role.Button) {
                it.onClickTrigger.trigger()
            })
        }
        Spacer(Modifier.width(8.dp))
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
        colors = CheckboxDefaults.colors().copy(
            uncheckedBoxColor = ColorScheme2.Color2.OnPrimary.toColor2(),
            uncheckedBorderColor = ColorScheme2.Color2.Unspecified.toColor2(),
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
    var showWebView by mutableStateOf(true)
    if (showWebView) {
        Box(modifier = modifier.background(Color.White).fillMaxSize()) {
            sduiLog(config.url, tag = "LinkedinLog")
            val state = rememberWebViewState(config.url)
            val navigator = rememberWebViewNavigator(
                requestInterceptor = object : RequestInterceptor {
                    override fun onInterceptUrlRequest(
                        request: WebRequest,
                        navigator: WebViewNavigator
                    ): WebRequestInterceptResult {
                        val navigation = config.whitelist.firstOrNull { navigation -> request.url.startsWith("https://" + navigation.url) }
                        return if (navigation != null) {
                            val allParams = request.url.split("?").getOrNull(1)?.split("&")?.associate {
                                it.split("=").let { (key, value) -> key to value }
                            }

                            if (allParams != null && navigation.params.isNotEmpty()) {
                                val map = mutableMapOf<String, DataMeta2.DataMetaType2>()
                                C.emitSideEffect(
                                    PopulatedSideEffectMeta2(
                                        metaId = navigation.metaId,
                                        metas = listOf(
                                            DataMeta2(
                                                data = allParams.mapValuesTo(map) { DataMeta2.DataMetaType2.StringData(it.value) }
                                            )
                                        )
                                    )
                                )
                                sduiLog("allParams", map, tag = "LinkedinLog")
                            }
                            navigation.destination?.config?.let { C.navigate(it) }
                            sduiLog("Allow > navigation", request.url, allParams, tag = "LinkedinLog", header = "Start", footer = "End")
                            WebRequestInterceptResult.Allow
                        } else {
                            sduiLog("Reject", request.url, config.url, tag = "LinkedinLog")
                            WebRequestInterceptResult.Reject.also { showWebView = false } // TODO I might need to include url first
                        }
                    }
                }
            )
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator( // TODO this can definitely be improved as it's not very clear
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(
                state = state,
                navigator = navigator
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
            modifier = modifier,
        ) {
            leadingIcon()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth() // TODO hardcoded to ensure column fills button
            ) {
                if (loading.value) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    text?.let {
                        Text(
                            text = it,
                            style = textStyle.toTextStyle2(fontWeight)
                        )
                    }
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
    HorizontalDivider(modifier)
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

@Composable
fun DropDownState2.DropDown2(
    modifier: Modifier = Modifier,
) {
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
//            children.forEach {
//                Render(
//                    state = it,
//                    modifier = toModifier2(it.weight)
//                )
//            }
            (C.childrenMap[path] ?: children).forEach {
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
        modifier = modifier.then(addOnClick(modifier)),
    ) {
        (C.childrenMap[path] ?: children).forEach {
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
        (C.childrenMap[path] ?: children).forEach {
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
    sduiLog(path, tag = "filtered index > Column") { id.name == "testZone" }
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
        sduiLog(path, tag = "filtered index > ColumnState > Column") { id.name == "testZone" }
        val filterChildren = if (filter != null) mutableListOf<StateId2>() else null
        val childrenViewModels = (C.childrenMap[path] ?: children).mapNotNull { child ->
            child.metas.filterIsInstance<ZoneViewModel2>().firstOrNull()?.let { zvm -> Pair(zvm,child.id) }
        }

        filter?.let{ filterGroup ->
            childrenViewModels.map { vm ->
                val show = filterGroup.map { filter ->
                    val value1 = (filter.value1 as? ReferenceableLiveValue2)?.let {
                        if (it.ref is ZoneRef2) {
                            it.copyRef(
                                ZoneRef2(
                                    vm = vm.first.metaId,
                                    property = (it.ref as ZoneRef2).property
                                )
                            )
                        } else filter.value1
                    } ?: filter.value1

                    val value2 = (filter.value2 as? ReferenceableLiveValue2)?.let {
                        if (it.ref is ZoneRef2) {
                            it.copyRef(
                                ZoneRef2(
                                    vm = vm.first.metaId,
                                    property = (it.ref as ZoneRef2).property
                                )
                            )
                        } else filter.value2
                    } ?: filter.value2
                    C.computeConditionalLive(ConditionalLiveValue2(value1, filter.condition, value2))
                }.all { it }
                sduiLog(show, tag = "filters result")
               if (show) filterChildren?.add(vm.second) else filterChildren?.remove(vm.second)
            }
            sduiLog( filterChildren, childrenViewModels, children.map { it.metas }, tag = "filters")
        }

        (C.childrenMap[path] ?: children).forEachIndexed { index, child ->
            if (filterChildren?.contains(child.id) == false) return@forEachIndexed
            val updatedIndex = filterChildren?.indexOf(child.id) ?: index
            val childViewModel = childrenViewModels.firstOrNull { it.second == child.id }?.first
            childViewModel?.map["!index"] = IntProperty(updatedIndex) // don't need to do any recompositions here, so don't update
            Render(
                state = child,
                modifier = toModifier2(child.weight)
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
//        C.reactiveBackStackMap[id]?.lastOrNull()?.run {
//            println("Rendering Nav Container $id")
//            println(this)
//            println(C.reactiveBackStackMap[id]!!.map { it.state.id })
//            AnimatedContent(
//                targetState = this,
//                transitionSpec = { defaultTransitionSpec(direction) },
//            ) { targetState ->
//                BackHandler(C.canPop(id)) { C.pop(id) }
//                Render(state = targetState.state)
//            }
//        } ?: children.forEach {
//            Render(state = it)
//        }
//        var state by remember { mutableStateOf(this@Box2) }
//        state = this@Box2
//        AnimatedContent(
//            targetState = state,
//            transitionSpec = { defaultTransitionSpec(BackStackEntry2.Direction2.Forward2) }
//        ) {
//            state.children.forEach {
//                Render(state = it)
//            }
//        }

        (C.childrenMap[path] ?: children).forEach {
            Render(
                state = it,
            )
        }
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
        items(C.childrenMap[path] ?: children) {
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
    var timer: Job? by remember { mutableStateOf(null) }

    var iteration by remember { mutableStateOf(0) }

    var filterChildren = if (filter != null) mutableListOf<StateId2>() else null
    var childrenViewModels = (C.childrenMap[path] ?: children).mapNotNull { child ->
        child.metas.filterIsInstance<ZoneViewModel2>().firstOrNull()?.let { zvm -> Pair(zvm,child.id) }
    }

    val c = mutableStateOf((C.childrenMap[path] ?: children).map { C.getReactivePathStateOrNull<State2>(it.path, it)?.value ?: it })

    LaunchedEffect(jumpTo, iteration) {
        jumpTo?.let { jT ->
            launch {
                jT.firstOrNull { it.value1 as? LiveValue2.InstantNowLiveValue2 != null || it.value2 as? LiveValue2.InstantNowLiveValue2 != null }?.let {
                    (it.value1 as? LiveValue2.InstantNowLiveValue2 ?: it.value2 as? LiveValue2.InstantNowLiveValue2)?.refreshSeconds
                }?.let { seconds ->
                    timer?.cancel()
                    timer = createTimer(
                        intervalSeconds = seconds,
                        func = {
                            sduiLog("timer running", tag = "filtered index > Items > Timer > Func")
                            iteration++
                            reset().let{ true }
                        }
                    )
                    sduiLog("timer created", tag = "filtered index > Items > Timer")
                }

                val cjT = childrenViewModels.firstOrNull { childVm ->
                    jT.map { condition ->
                        val value1 = when(val v1 = condition.value1){
                            is LiveValue2.IntLiveValue2 -> {
                                when(val ref = v1.ref) {
                                    is ZoneRef2 -> v1.copyRef(
                                        ref = ZoneRef2(
                                            vm = childVm.first.metaId,
                                            property = ref.property
                                        )
                                    )
                                    else -> TODO()
                                }
                            }
                            is LiveValue2.InstantNowLiveValue2 -> v1
                            else -> TODO()
                        }

                        val value2 = when(val v2 = condition.value2){
                            is LiveValue2.IntLiveValue2 -> {
                                when(val ref = v2.ref) {
                                    is ZoneRef2 -> v2.copyRef(
                                        ref = ZoneRef2(
                                            vm = childVm.first.metaId,
                                            property = ref.property
                                        )
                                    )
                                    else -> TODO()
                                }
                            }
                            is LiveValue2.InstantNowLiveValue2 -> v2
                            else -> TODO()
                        }
                        C.computeConditionalLive(ConditionalLiveValue2(value1, condition.condition, value2)).also {
                            sduiLog(it, iteration, childVm.second.scope, tag = "filtered index > Items > Timer > Condition > Result") { listOf("1","2","3").contains(childVm.second.scope) }
                        }
                    }.let { it.isNotEmpty() && it.all { r -> r } }
                }
                sduiLog(cjT, iteration, tag = "filtered index > Items > Timer > Condition!", header = "start", footer = "end")

                cjT?.let { childVm ->
                    val index = filterChildren?.indexOf(childVm.second) ?: c.value.indexOfFirst { childVm.second == it.id }
                    if (index > -1) {
                        sduiLog("updated Index", index, tag = "filtered index > updated index")
                        childrenViewModels.forEach {
                            it.first.map["!focus"] = IntProperty(index)
                        }
                        c.value.forEach { it.update { it.reset() } }
                        reset()
                        listState.animateScrollToItem(index)
                    }
                }
            }
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
        filter?.let{ filterGroup ->
            childrenViewModels.map { vm ->
                val show = filterGroup.map { filter ->
                    val value1 = (filter.value1 as? ReferenceableLiveValue2)?.let {
                        if (it.ref is ZoneRef2) {
                            it.copyRef(
                                ZoneRef2(
                                    vm = vm.first.metaId,
                                    property = (it.ref as ZoneRef2).property
                                )
                            )
                        } else filter.value1
                    } ?: filter.value1

                    val value2 = (filter.value2 as? ReferenceableLiveValue2)?.let {
                        if (it.ref is ZoneRef2) {
                            it.copyRef(
                                ZoneRef2(
                                    vm = vm.first.metaId,
                                    property = (it.ref as ZoneRef2).property
                                )
                            )
                        } else filter.value2
                    } ?: filter.value2
                    C.computeConditionalLive(ConditionalLiveValue2(value1, filter.condition, value2))
                }.all { it }
                sduiLog(show, tag = "filters result")
                if (show) filterChildren?.add(vm.second) else filterChildren?.remove(vm.second)
            }
            sduiLog( filterChildren, childrenViewModels, children.map { it.metas }, tag = "filters")
        }

        sort?.let { sort ->
            (sort as? ReferenceableLiveValue2)?.let { referenceableLiveValue2 ->
                val order = childrenViewModels.map { vmP ->
                    val a = when(referenceableLiveValue2){
                        is LiveValue2.IntLiveValue2 -> when(val ref = referenceableLiveValue2.ref) {
                            is ZoneRef2 -> vmP.first.map[ref.property]
                            else -> TODO()
                        }
                        else -> TODO()
                    }
                    when(a){
                        is IntProperty -> Pair(a.value, vmP.second)
                        else -> TODO()
                    }
                }

                val sorted = order.sortedBy { it.first }
                c.value = sorted.mapNotNull { vmP ->
                    c.value.find { it.id == vmP.second }
                }

                filterChildren = sorted.mapNotNull { vmP ->
                    filterChildren?.find { it == vmP.second }
                }.toMutableList()
            }
        }

        sduiLog(c.value.map { it.path }, tag = "filtered index > LazyColumn")
//        sduiLog(C.childrenMap[path], children, C.childrenMap[path]?.map { C.getReactivePathState<State2>(it.path, it).value }, tag = "filtered index > LazyColumn > Children", header = "header", footer = "footer")

        c.value.forEach { child ->
            var realChild = C.getReactivePathStateOrNull<State2>(child.path, child)?.value ?: child // TODO insanely difficult bug to track down!
            sduiLog(child.path, tag = "filtered index > LazyColumn > Item")
            if (filterChildren?.contains(child.id) == false) return@forEach
            val updatedIndex = filterChildren?.indexOf(child.id) ?: c.value.indexOf(child)
//            sduiLog(path, filter, filterChildren, updatedIndex, child.id, childrenViewModels, tag = "filtered index", header = "")
            val childViewModel = childrenViewModels.firstOrNull { it.second == child.id }?.first
            if (childViewModel?.map["!index"] != IntProperty(updatedIndex)) {
                childViewModel?.map["!index"] = IntProperty(updatedIndex) // don't need to do any recompositions here, so don't update
                realChild.update { reset() }
            }
        }

        items(items = c.value) { child ->
            val realChild = C.getReactivePathStateOrNull<State2>(child.path, child)?.value ?: child // TODO insanely difficult bug to track down!
            sduiLog(child.path, tag = "filtered index > LazyColumn > Item")
            if (filterChildren?.contains(child.id) == false) return@items
//            val updatedIndex = filterChildren?.indexOf(child.id) ?: c.value.indexOf(child)
//            sduiLog(path, filter, filterChildren, updatedIndex, child.id, childrenViewModels, tag = "filtered index", header = "")
//            val childViewModel = childrenViewModels.firstOrNull { it.second == child.id }?.first
//            if (childViewModel?.map["!index"] != IntProperty(updatedIndex)) {
//                childViewModel?.map["!index"] = IntProperty(updatedIndex) // don't need to do any recompositions here, so don't update
//                realChild = realChild.update { reset() }
//            }
//            sduiLog(child.path, childViewModel?.map["!index"], C.getReactivePathState<State2>(child.path, child).value, tag = "filtered index > Items > Pre", header = "")

//            val updatedChild = realChild.update { reset() }
//            C.reactiveUpdate(updatedChild)
//            sduiLog(updatedChild.path, updatedChild.counter, childViewModel?.map["!index"], C.getReactivePathState<State2>(child.path, child), tag = "filtered index > Items > Post", header = "")
            Render(
                state = realChild,
            )
        }
//        items(C.childrenMap[path] ?: children) {
//            Render(state = it)
//        }
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
        items(C.childrenMap[path] ?: children) {
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
        println("Rendering Screen2")
        println(path)
        println("Children: remote children ${C.childrenMap[path]?.map { it.path }}")
        println("Children: local children ${children.map { it.path }}")
        if (C.childrenMap[path]?.isEmpty() == true) println("I found the bug")
        if (C.childrenMap[path]?.isEmpty() == false) println("I am stumped")
        (C.childrenMap[path] ?: children).forEach {
            println("rendering in screenState children ${it.path}")
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
    (C.get(metaId) as? JsonMeta2)?.json?.let {
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
        (C.childrenMap[path] ?: children).forEach {
            Render(
                state = it,
            )
        }
    }
}
