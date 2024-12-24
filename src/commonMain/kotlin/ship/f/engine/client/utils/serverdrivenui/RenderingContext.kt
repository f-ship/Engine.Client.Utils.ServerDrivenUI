package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ship.f.engine.client.utils.serverdrivenui.RenderingContext.CommonClient
import ship.f.engine.client.utils.serverdrivenui.components.SBottomRow
import ship.f.engine.client.utils.serverdrivenui.components.SBottomSheet
import ship.f.engine.client.utils.serverdrivenui.components.SButton
import ship.f.engine.client.utils.serverdrivenui.components.SCard
import ship.f.engine.client.utils.serverdrivenui.components.SColumn
import ship.f.engine.client.utils.serverdrivenui.components.SCustom
import ship.f.engine.client.utils.serverdrivenui.components.SDialog
import ship.f.engine.client.utils.serverdrivenui.components.SDropDown
import ship.f.engine.client.utils.serverdrivenui.components.SFlexColumn
import ship.f.engine.client.utils.serverdrivenui.components.SFlexGrid
import ship.f.engine.client.utils.serverdrivenui.components.SFlexRow
import ship.f.engine.client.utils.serverdrivenui.components.SGrid
import ship.f.engine.client.utils.serverdrivenui.components.SIcon
import ship.f.engine.client.utils.serverdrivenui.components.SImage
import ship.f.engine.client.utils.serverdrivenui.components.SLoader
import ship.f.engine.client.utils.serverdrivenui.components.SLoadingShimmer
import ship.f.engine.client.utils.serverdrivenui.components.SMenu
import ship.f.engine.client.utils.serverdrivenui.components.SRadioList
import ship.f.engine.client.utils.serverdrivenui.components.SRow
import ship.f.engine.client.utils.serverdrivenui.components.SSearch
import ship.f.engine.client.utils.serverdrivenui.components.SSnackBar
import ship.f.engine.client.utils.serverdrivenui.components.SText
import ship.f.engine.client.utils.serverdrivenui.components.STextField
import ship.f.engine.client.utils.serverdrivenui.components.STickList
import ship.f.engine.client.utils.serverdrivenui.components.SToggle
import ship.f.engine.client.utils.serverdrivenui.components.SUnknownComponent
import ship.f.engine.client.utils.serverdrivenui.components.SUnknownWidget
import ship.f.engine.client.utils.serverdrivenui.components.SVideo
import ship.f.engine.client.utils.serverdrivenui.components.Space
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Fallback
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.ID
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.BottomRowState
import ship.f.engine.shared.utils.serverdrivenui.state.BottomSheetState
import ship.f.engine.shared.utils.serverdrivenui.state.ButtonState
import ship.f.engine.shared.utils.serverdrivenui.state.CardState
import ship.f.engine.shared.utils.serverdrivenui.state.ColumnState
import ship.f.engine.shared.utils.serverdrivenui.state.ComponentState
import ship.f.engine.shared.utils.serverdrivenui.state.CustomState
import ship.f.engine.shared.utils.serverdrivenui.state.DialogState
import ship.f.engine.shared.utils.serverdrivenui.state.DropDownState
import ship.f.engine.shared.utils.serverdrivenui.state.FieldState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexColumnState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexGridState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexRowState
import ship.f.engine.shared.utils.serverdrivenui.state.GridState
import ship.f.engine.shared.utils.serverdrivenui.state.IconState
import ship.f.engine.shared.utils.serverdrivenui.state.ImageState
import ship.f.engine.shared.utils.serverdrivenui.state.LoaderState
import ship.f.engine.shared.utils.serverdrivenui.state.LoadingShimmerState
import ship.f.engine.shared.utils.serverdrivenui.state.MenuState
import ship.f.engine.shared.utils.serverdrivenui.state.RadioListState
import ship.f.engine.shared.utils.serverdrivenui.state.RowState
import ship.f.engine.shared.utils.serverdrivenui.state.SearchState
import ship.f.engine.shared.utils.serverdrivenui.state.SnackBarState
import ship.f.engine.shared.utils.serverdrivenui.state.SpaceState
import ship.f.engine.shared.utils.serverdrivenui.state.State
import ship.f.engine.shared.utils.serverdrivenui.state.TextState
import ship.f.engine.shared.utils.serverdrivenui.state.TickListState
import ship.f.engine.shared.utils.serverdrivenui.state.ToggleState
import ship.f.engine.shared.utils.serverdrivenui.state.UnknownComponentState
import ship.f.engine.shared.utils.serverdrivenui.state.UnknownWidgetState
import ship.f.engine.shared.utils.serverdrivenui.state.VideoState
import ship.f.engine.shared.utils.serverdrivenui.state.WidgetState

@Suppress("UNCHECKED_CAST")
data class RenderingContext(
    val initialConfig: ScreenConfig,
) {

    class CommonClient : Client {
        override val stateMap: MutableMap<ID, StateHolder<State>> = mutableMapOf()
        override val elementMap: MutableMap<ID, ScreenConfig.Element> = mutableMapOf()
        val shadowStateMap: MutableMap<ID, MutableState<StateHolder<State>>> = mutableMapOf()
        override fun postUpdateHook(
            id: ID,
            stateHolder: StateHolder<State>,
        ) {
            shadowStateMap[id]?.value = stateHolder
        }

        var requiresInit: Boolean = true

        fun init(config: ScreenConfig) {
            if (requiresInit){
                config.state.forEach {
                    setState(it)
                    setTriggers(it)
                }
                requiresInit = false
            }
        }

        private fun setState(element: ScreenConfig.Element) {
            val stateHolder = StateHolder(element.state)
            shadowStateMap[element.id] = mutableStateOf(stateHolder)
            stateMap[element.id] = stateHolder
            elementMap[element.id] = element

            when (element) {
                is ScreenConfig.Component -> Unit
                is ScreenConfig.Widget -> element.state.children.forEach { setState(it) }
            }
        }

        private fun setTriggers(element: ScreenConfig.Element) {
            element.triggerActions.filterIsInstance<TriggerAction.OnStateUpdate>().forEach {
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
                is ScreenConfig.Component -> Unit
                is ScreenConfig.Widget -> element.state.children.forEach { setTriggers(it) }
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

//    fun emitAction(action: Action) {
//        when (action) {
//            Action.Copy -> Unit
//            Action.Navigate -> Unit
//            Action.Refresh -> Unit
//            Action.SendState -> Unit
//            Action.UpdateState -> Unit
//            Action.UpdateEnabled -> Unit
//            Action.UpdateFieldState -> Unit
//            Action.UpdateToggled -> Unit
//            Action.UpdateVisibility -> Unit
//        }
//    }
}

@Composable
fun RenderScreen(
    screenConfig: MutableState<ScreenConfig>,
    client: CommonClient,
) {
    val renderingContext = remember { RenderingContext(screenConfig.value) }
    renderingContext.client = client
    Column {
        screenConfig.value.state.forEach {
            renderingContext.RenderUI(
                element = it,
                ctx = renderingContext
            )
        }
    }
}
