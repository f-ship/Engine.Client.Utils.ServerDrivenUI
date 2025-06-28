package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import ship.f.engine.client.utils.serverdrivenui.RenderingContext.CommonClient
import ship.f.engine.client.utils.serverdrivenui.components.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction.OnStateUpdateTrigger
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.*

@Suppress("UNCHECKED_CAST")
data class RenderingContext(
    val initialConfig: ScreenConfig,
) {

    class CommonClient : Client {
        override val stateMap: MutableMap<ID, StateHolder<State>> = mutableMapOf()
        override val elementMap: MutableMap<ID, Element> = mutableMapOf()
        val shadowStateMap: MutableMap<ID, MutableState<StateHolder<State>>> = mutableMapOf()
        override fun postUpdateHook(
            id: ID,
            stateHolder: StateHolder<State>,
        ) {
            shadowStateMap[id]?.value = stateHolder
        }

        var requiresInit: Boolean = true

        fun init(config: ScreenConfig) {
            if (requiresInit) {
                config.state.forEach {
                    setState(it)
                    setTriggers(it)
                }
                requiresInit = false
            }
        }

        private fun setState(element: Element) {
            val stateHolder = StateHolder(element.state)
            shadowStateMap[element.id] = mutableStateOf(stateHolder)
            stateMap[element.id] = stateHolder
            elementMap[element.id] = element

            when (element) {
                is Component -> Unit
                is Widget -> element.state.children.forEach { setState(it) }
            }
        }

        private fun setTriggers(element: Element) {
            element.triggerActions.filterIsInstance<OnStateUpdateTrigger>().forEach {
                it.action.targetIds.forEach { target ->
                    val stateHolder = shadowStateMap[target.id]!!.value
                    val updatedStateHolder = stateHolder.copy(
                        listeners = stateHolder.listeners + StateHolder.RemoteAction(
                            action = it.action,
                            id = element.id
                        )
                    )
                    shadowStateMap[target.id] = mutableStateOf(updatedStateHolder)
                    stateMap[target.id] = updatedStateHolder
                    println(shadowStateMap[target.id]?.value)
                }
            }

            when (element) {
                is Component -> Unit
                is Widget -> element.state.children.forEach { setTriggers(it) }
            }
        }

        fun <T : State> getHolder(id: ID) = shadowStateMap[id] as MutableState<StateHolder<T>>
    }

    var config = initialConfig

    var banner: Fallback? = null
    lateinit var client: CommonClient


    @Composable
    fun RenderWidget(
        state: WidgetState,
        triggerActions: List<TriggerAction>,
        fallback: Fallback,
        id: ID,
        ctx: RenderingContext,
    ) {
        when (state) {
            is BottomSheetState -> SBottomSheet(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is CardState -> SCard(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is ColumnState -> SColumn(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is FlexRowState -> SFlexRow(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is GridState -> SGrid(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is RowState -> SRow(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is FlexGridState -> SFlexGrid(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is FlexColumnState -> SFlexColumn(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is UnknownWidgetState -> SUnknownWidget(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                fallback = fallback,
                id = id,
                ctx = ctx
            )
        }
    }

    @Composable
    fun RenderComponent(
        state: ComponentState,
        triggerActions: List<TriggerAction>,
        fallback: Fallback,
        id: ID,
        ctx: RenderingContext,
    ) {
        when (state) {
            is BottomRowState -> SBottomRow(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is ButtonState -> SButton(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is CustomState -> SCustom(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is DialogState -> SDialog(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is DropDownState -> SDropDown(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is IconState -> SIcon(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is ImageState -> SImage(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is LoaderState -> SLoader(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is LoadingShimmerState -> SLoadingShimmer(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is MenuState -> SMenu(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is RadioListState -> SRadioList(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is SearchState -> SSearch(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is SnackBarState -> SSnackBar(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is FieldState -> STextField(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is TextState -> SText(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is TickListState -> STickList(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is ToggleState -> SToggle(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is VideoState -> SVideo(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                ctx = ctx,
            )

            is SpaceState -> Space(
                state = ctx.client.getHolder(id),
            )

            is UnknownComponentState -> SUnknownComponent(
                state = ctx.client.getHolder(id),
                triggerActions = triggerActions,
                fallback = fallback,
                id = id,
                ctx = ctx,
            )
        }
    }

    @Composable
    fun RenderUI(
        element: ScreenConfig.Element,
        ctx: RenderingContext,
    ) {
        val stateHolder = ctx.client.shadowStateMap[element.id]!!.value
        when (val state = stateHolder.state) {
            is WidgetState -> RenderWidget(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                ctx = ctx,
            )

            is ComponentState -> RenderComponent(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                ctx = ctx,
            )
        }
    }
}

@Composable
fun RenderScreen(
    screenConfig: MutableState<ScreenConfig>,
    client: CommonClient,
) {
    val renderingContext = remember { RenderingContext(screenConfig.value) }
    renderingContext.client = client

    //Text run experiment to see if Material theme can easily be overridden
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            screenConfig.value.darkColorScheme?.let {
                MaterialTheme.colorScheme.fromColorSchemeState(it)
            }
        } else {
            screenConfig.value.lightColorScheme?.let {
                MaterialTheme.colorScheme.fromColorSchemeState(it)
            }
        }?: MaterialTheme.colorScheme,
    ) {
        LazyColumn {
            items(screenConfig.value.state) {
                renderingContext.RenderUI(
                    element = it,
                    ctx = renderingContext,
                )
            }
        }
    }
}

private fun ColorScheme.fromColorSchemeState(colorSchemeState: ColorSchemeState) = copy(
    primary = Color(colorSchemeState.primary),
    onPrimary = Color(colorSchemeState.onPrimary),
    onSecondaryContainer = Color(colorSchemeState.onSecondaryContainer),
    secondaryContainer = Color(colorSchemeState.secondaryContainer),
    background = Color(colorSchemeState.background),
    onBackground = Color(colorSchemeState.onBackground),
    surface = Color(colorSchemeState.surface),
    onSurface = Color(colorSchemeState.onSurface),
    surfaceVariant = Color(colorSchemeState.surfaceVariant),
    onSurfaceVariant = Color(colorSchemeState.onSurfaceVariant),
    outline = Color(colorSchemeState.outline),
)
