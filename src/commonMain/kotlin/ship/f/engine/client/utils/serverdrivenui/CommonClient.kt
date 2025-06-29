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
@Suppress("UNCHECKED_CAST")
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
    var currentScreen = backstack.lastOrNull()?.let { mutableStateOf(it) } ?: mutableStateOf(ScreenConfig())
    fun addConfig(config: ScreenConfig) {
        backstack.add(config)
        currentScreen.value = config
    }

    fun pushScreen(config: ScreenConfig) {
        if (initMap[config.id] ?: true) {
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
                c = this,
            )

            is CardState -> SCard(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is ColumnState -> SColumn(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is FlexRowState -> SFlexRow(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is GridState -> SGrid(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is RowState -> SRow(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is FlexGridState -> SFlexGrid(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is FlexColumnState -> SFlexColumn(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
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
                c = this,
            )

            is ButtonState -> SButton(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is CustomState -> SCustom(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is DialogState -> SDialog(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is DropDownState -> SDropDown(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is IconState -> SIcon(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is ImageState -> SImage(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is LoaderState -> SLoader(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is LoadingShimmerState -> SLoadingShimmer(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is MenuState -> SMenu(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is RadioListState -> SRadioList(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is SearchState -> SSearch(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is SnackBarState -> SSnackBar(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is FieldState -> STextField(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is TextState -> SText(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is TickListState -> STickList(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is ToggleState -> SToggle(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is VideoState -> SVideo(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                id = id,
                c = this,
            )

            is SpaceState -> Space(
                state = c.getHolder(id),
            )

            is UnknownComponentState -> SUnknownComponent(
                state = c.getHolder(id),
                triggerActions = triggerActions,
                fallback = fallback,
                id = id,
                c = this,
            )
        }
    }

    @Composable
    fun RenderUI(
        element: ScreenConfig.Element,
    ) {
        val stateHolder = shadowStateMap[element.id]!!.value
        when (val state = stateHolder.state) {
            is WidgetState -> RenderWidget(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                c = this,
            )

            is ComponentState -> RenderComponent(
                state = state,
                triggerActions = element.triggerActions,
                fallback = element.fallback,
                id = element.id,
                c = this,
            )
        }
    }
}