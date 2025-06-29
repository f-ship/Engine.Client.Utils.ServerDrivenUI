package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ship.f.engine.client.utils.serverdrivenui.components.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.state.*

/**
 * Need to upgrade how the backstack is handled. I wonder if I can make copying more efficient
 */
class CommonClient : Client {
    override val stateMap: MutableMap<ScreenConfig.ID, Client.StateHolder<State>> = mutableMapOf()
    override val elementMap: MutableMap<ScreenConfig.ID, ScreenConfig.Element> = mutableMapOf()
    override var banner: ScreenConfig.Fallback? = null
    val shadowStateMap: MutableMap<ScreenConfig.ID, MutableState<Client.StateHolder<State>>> = mutableMapOf()
    override fun postUpdateHook(
        id: ScreenConfig.ID,
        stateHolder: Client.StateHolder<State>,
    ) {
        shadowStateMap[id]?.value = stateHolder
    }

    var initMap: MutableMap<ScreenConfig.ID, Boolean> = mutableMapOf()

    val backstack: MutableList<ScreenConfig> = mutableListOf()
    // Make this have a default empty screen config instead of making this nullable
    var currentScreen = backstack.lastOrNull()?.let { mutableStateOf(it) }
    fun addConfig(config: ScreenConfig) {
        backstack.add(config)
        if (currentScreen == null) {
            currentScreen = mutableStateOf(config)
        } else {
            currentScreen?.value = config
        }
    }

    fun pushScreen(config: ScreenConfig) {
        val requiresInit = initMap[config.id] ?: true
        if (requiresInit) {
            config.state.forEach {
                setState(it)
                setTriggers(it)
            }
            initMap[config.id] = false
        }
        addConfig(config)
    }

    private fun setState(element: ScreenConfig.Element) {
        val stateHolder = Client.StateHolder(element.state)
        shadowStateMap[element.id] = mutableStateOf(stateHolder)
        stateMap[element.id] = stateHolder
        elementMap[element.id] = element

        when (element) {
            is ScreenConfig.Component -> Unit
            is ScreenConfig.Widget -> element.state.children.forEach { setState(it) }
        }
    }

    private fun setTriggers(element: ScreenConfig.Element) {
        element.triggerActions.filterIsInstance<ScreenConfig.TriggerAction.OnStateUpdateTrigger>().forEach {
            it.action.targetIds.forEach { target ->
                val stateHolder = shadowStateMap[target.id]!!.value
                val updatedStateHolder = stateHolder.copy(
                    listeners = stateHolder.listeners + Client.StateHolder.RemoteAction(
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

    fun <T : State> getHolder(id: ScreenConfig.ID) = shadowStateMap[id] as MutableState<Client.StateHolder<T>>

    @Composable
    fun RenderWidget(
        state: WidgetState,
        triggerActions: List<ScreenConfig.TriggerAction>,
        fallback: ScreenConfig.Fallback,
        id: ScreenConfig.ID,
        c: CommonClient,
    ) {
        when (state) {
            is BottomSheetState -> SBottomSheet(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is CardState -> SCard(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is ColumnState -> SColumn(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is FlexRowState -> SFlexRow(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is GridState -> SGrid(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is RowState -> SRow(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is FlexGridState -> SFlexGrid(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is FlexColumnState -> SFlexColumn(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is UnknownWidgetState -> SUnknownWidget(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                fallback = fallback,
                id = id,
                c = c
            )
        }
    }

    @Composable
    fun RenderComponent(
        state: ComponentState,
        triggerActions: List<ScreenConfig.TriggerAction>,
        fallback: ScreenConfig.Fallback,
        id: ScreenConfig.ID,
        c: CommonClient,
    ) {
        when (state) {
            is BottomRowState -> SBottomRow(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is ButtonState -> SButton(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is CustomState -> SCustom(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is DialogState -> SDialog(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is DropDownState -> SDropDown(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is IconState -> SIcon(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is ImageState -> SImage(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is LoaderState -> SLoader(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is LoadingShimmerState -> SLoadingShimmer(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is MenuState -> SMenu(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is RadioListState -> SRadioList(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is SearchState -> SSearch(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is SnackBarState -> SSnackBar(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is FieldState -> STextField(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is TextState -> SText(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is TickListState -> STickList(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is ToggleState -> SToggle(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is VideoState -> SVideo(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = c,
            )

            is SpaceState -> Space(
                state = c.getHolder(id),
            )

            is UnknownComponentState -> SUnknownComponent(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                fallback = fallback,
                id = id,
                c = c,
            )
        }
    }

    @Composable
    fun RenderUI(
        element: ScreenConfig.Element,
        c: CommonClient,
    ) {
        val stateHolder = c.shadowStateMap[element.id]!!.value
        when (val state = stateHolder.state) {
            is WidgetState -> RenderWidget(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                c = c,
            )

            is ComponentState -> RenderComponent(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                c = c,
            )
        }
    }
}