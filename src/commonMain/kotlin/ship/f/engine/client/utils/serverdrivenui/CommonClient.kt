package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import ship.f.engine.client.utils.serverdrivenui.components.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.RemoteAction
import ship.f.engine.shared.utils.serverdrivenui.state.*

val ClientProvider = staticCompositionLocalOf { CommonClient() }
val C @Composable get() = ClientProvider.current

@Composable
fun <S: ComponentState>MutableState<Component<S>>.WithComponent(block: @Composable S.() -> Unit): Unit {
    block(this.value.state)
}

@Composable
fun <S: WidgetState>MutableState<Widget<S>>.WithWidget(block: @Composable S.() -> Unit): Unit {
    block(this.value.state)
}

/**
 * Need to upgrade how the backstack is handled. I wonder if I can make copying more efficient
 */
@Suppress("UNCHECKED_CAST")
class CommonClient : Client {
    override val stateMap: MutableMap<ScreenConfig.ID, Client.StateHolder<out State>> = mutableMapOf()
    override val elementMap: MutableMap<ScreenConfig.ID, Element<out State>> = mutableMapOf()
    override var banner: ScreenConfig.Fallback? = null
    val shadowStateMap: MutableMap<ScreenConfig.ID, MutableState<Client.StateHolder<out State>>> = mutableMapOf()
    val shadowElementMap: MutableMap<ScreenConfig.ID, MutableState<Element<out State>>> = mutableMapOf()

    override fun postUpdateHook(
        id: ScreenConfig.ID,
        stateHolder: Client.StateHolder<out State>,
    ) {
        shadowStateMap[id]?.value = stateHolder
    }

    override fun postElementUpdateHook(element: Element<out State>) {
        shadowElementMap[element.id]?.value = element
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
                setState(it as Element<State>)
                setTriggers(it)
            }
            initMap[config.id] = false
        }
        addConfig(config)
    }

    private fun setState(element: Element<out State>) {
        val stateHolder = Client.StateHolder(element.state)
        shadowStateMap[element.id] = mutableStateOf(stateHolder)
        stateMap[element.id] = stateHolder
        if (elementMap[element.id] != null && elementMap[element.id] != element) {
            error("Duplicate ID: ${element.id}")
        }
        shadowElementMap[element.id] = mutableStateOf(element)
        elementMap[element.id] = element


        when (element) {
            is Component<*> -> Unit
            is Widget<*> -> element.state.children.forEach { setState(it) }
        }
    }

    private fun setTriggers(element: Element<out State>) {
        element.triggerActions.filterIsInstance<ScreenConfig.TriggerAction.OnStateUpdateTrigger>().forEach {
            it.action.targetIds.forEach { target ->
                val stateHolder = shadowStateMap[target.id]!!.value
                val updatedStateHolder = stateHolder.copy(
                    listeners = stateHolder.listeners + RemoteAction(
                        action = it.action,
                        id = element.id
                    )
                )
                val targetElement = elementMap[target.id] ?: error("Target element not found for ID: ${target.id}")

                val updatedElement = when (targetElement) {
                    is Component<*> -> targetElement.copy(
                        listeners = targetElement.listeners + RemoteAction(
                            action = it.action,
                            id = element.id
                        )
                    )

                    is Widget<*> -> targetElement.copy(
                        listeners = targetElement.listeners + RemoteAction(
                            action = it.action,
                            id = element.id
                        )
                    )
                }
                elementMap[target.id] = updatedElement
                shadowElementMap[target.id] = mutableStateOf(updatedElement)

                shadowStateMap[target.id] = mutableStateOf(updatedStateHolder)
                stateMap[target.id] = updatedStateHolder
                println(shadowStateMap[target.id]?.value)
            }
        }

        when (element) {
            is Component<*> -> Unit
            is Widget<*> -> element.state.children.forEach { setTriggers(it) }
        }
    }

    fun <T : State> getHolder(id: ID) = shadowStateMap[id] as MutableState<Client.StateHolder<T>>

    fun <T : State> getElement(id: ID) = shadowElementMap[id] as MutableState<Element<T>>
    fun <T : State> getComponent(id: ID) = shadowElementMap[id] as MutableState<Component<T>>
    fun <T : State> getWidget(id: ID) = shadowElementMap[id] as MutableState<Widget<T>>

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
                element = getComponent(id),
            )

            is ButtonState -> SButton(
                element = getComponent(id),
            )

            is CustomState -> SCustom(
                element = getComponent(id),
            )

            is DialogState -> SDialog(
                element = getComponent(id),
            )

            is DropDownState -> SDropDown(
                element = getComponent(id),
            )

            is IconState -> SIcon(
                element = getComponent(id),
            )

            is ImageState -> SImage(
                element = getComponent(id),
            )

            is LoaderState -> SLoader(
                element = getComponent(id),
            )

            is LoadingShimmerState -> SLoadingShimmer(
                element = getComponent(id),
            )

            is MenuState -> SMenu(
                element = getComponent(id),
            )

            is RadioListState -> SRadioList(
                element = getComponent(id),
            )

            is SearchState -> SSearch(
                element = getComponent(id),
            )

            is SnackBarState -> SSnackBar(
                element = getComponent(id),
            )

            is FieldState -> STextField(
                element = getComponent(id),
            )

            is TextState -> SText(
                element = getComponent(id),
            )

            is TickListState -> STickList(
                element = getComponent(id),
            )

            is ToggleState -> SToggle(
                element = getComponent(id),
            )

            is VideoState -> SVideo(
                element = getComponent(id),
            )

            is SpaceState -> Space(
                element = getComponent(id),
            )

            is UnknownComponentState -> SUnknownComponent(
                element = getComponent(id),
            )
        }
    }

    @Composable
    fun RenderUI(
        element: Element<out State>,
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