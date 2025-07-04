package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import ship.f.engine.client.utils.serverdrivenui.components.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.action.Client
import ship.f.engine.shared.utils.serverdrivenui.action.ClientHolder
import ship.f.engine.shared.utils.serverdrivenui.action.RemoteAction
import ship.f.engine.shared.utils.serverdrivenui.state.*

val ClientProvider = staticCompositionLocalOf { CommonClient.getClient() }
val C @Composable get() = ClientProvider.current

@Composable
fun <S: ComponentState>MutableState<Component<S>>.WithComponentState(block: @Composable S.() -> Unit): Unit {
    block(this.value.state)
}

@Composable
fun <S: WidgetState>MutableState<Widget<S>>.WithWidgetState(block: @Composable S.() -> Unit): Unit {
    block(this.value.state)
}

/**
 * Need to upgrade how the backstack is handled. I wonder if I can make copying more efficient
 */
@Suppress("UNCHECKED_CAST")
class CommonClient private constructor() : Client {
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

    fun <T : State> getElement(id: ID) = shadowElementMap[id] as MutableState<Element<T>>
    fun <T : State> getComponent(id: ID) = shadowElementMap[id] as MutableState<Component<T>>
    fun <T : State> getWidget(id: ID) = shadowElementMap[id] as MutableState<Widget<T>>

    @Composable
    fun RenderWidget(
        widget: Widget<out WidgetState>
    ) {
        when (widget.state) {
            is BottomSheetState -> SBottomSheet(
                element = getWidget(widget.id),
            )

            is CardState -> SCard(
                element = getWidget(widget.id),
            )

            is ColumnState -> SColumn(
                element = getWidget(widget.id),
            )

            is FlexRowState -> SFlexRow(
                element = getWidget(widget.id),
            )

            is GridState -> SGrid(
                element = getWidget(widget.id),
            )

            is RowState -> SRow(
                element = getWidget(widget.id),
            )

            is FlexGridState -> SFlexGrid(
                element = getWidget(widget.id),
            )

            is FlexColumnState -> SFlexColumn(
                element = getWidget(widget.id),
            )

            is UnknownWidgetState -> SUnknownWidget(
                element = getWidget(widget.id),
            )
        }
    }

    @Composable
    fun RenderComponent(
        component: Component<out ComponentState>,
    ) {
        when (component.state) {
            is BottomRowState -> SBottomRow(
                element = getComponent(component.id),
            )

            is ButtonState -> SButton(
                element = getComponent(component.id),
            )

            is CustomState -> SCustom(
                element = getComponent(component.id),
            )

            is DialogState -> SDialog(
                element = getComponent(component.id),
            )

            is DropDownState -> SDropDown(
                element = getComponent(component.id),
            )

            is IconState -> SIcon(
                element = getComponent(component.id),
            )

            is ImageState -> SImage(
                element = getComponent(component.id),
            )

            is LoaderState -> SLoader(
                element = getComponent(component.id),
            )

            is LoadingShimmerState -> SLoadingShimmer(
                element = getComponent(component.id),
            )

            is MenuState -> SMenu(
                element = getComponent(component.id),
            )

            is RadioListState -> SRadioList(
                element = getComponent(component.id),
            )

            is SearchState -> SSearch(
                element = getComponent(component.id),
            )

            is SnackBarState -> SSnackBar(
                element = getComponent(component.id),
            )

            is FieldState -> STextField(
                element = getComponent(component.id),
            )

            is TextState -> SText(
                element = getComponent(component.id),
            )

            is TickListState -> STickList(
                element = getComponent(component.id),
            )

            is ToggleState -> SToggle(
                element = getComponent(component.id),
            )

            is VideoState -> SVideo(
                element = getComponent(component.id),
            )

            is SpaceState -> Space(
                element = getComponent(component.id),
            )

            is UnknownComponentState -> SUnknownComponent(
                element = getComponent(component.id),
            )
        }
    }

    @Composable
    fun RenderUI(
        element: Element<out State>,
    ) {
        when (element.state) {
            is WidgetState -> RenderWidget(
                widget = element as Widget<out WidgetState>,
            )

            is ComponentState -> RenderComponent(
                component = element as Component<out ComponentState>,
            )
        }
    }

    companion object {
        private val clients = mutableListOf<CommonClient>()
        private fun createClient() = CommonClient().also {
            clients.add(it)
            ClientHolder.addClient(it)
        }
        fun getClient() = clients.lastOrNull() ?: createClient()
    }
}