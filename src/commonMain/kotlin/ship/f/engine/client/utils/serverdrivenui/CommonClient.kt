package ship.f.engine.client.utils.serverdrivenui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import ship.f.engine.client.utils.serverdrivenui.elements.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.action.RemoteAction
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger
import ship.f.engine.shared.utils.serverdrivenui.client.Client
import ship.f.engine.shared.utils.serverdrivenui.client.ClientHolder
import ship.f.engine.shared.utils.serverdrivenui.ext.fGet
import ship.f.engine.shared.utils.serverdrivenui.state.*

/**
 * Main Client, in which much of its functionality should probably be pushed into the main client
 */
@Suppress("UNCHECKED_CAST")
class CommonClient private constructor() : Client() {
    /**
     * The map of elements that the common client keeps track of using mutable states
     */
    val reactiveElementMap: MutableMap<ID, MutableState<Element<out State>>> = mutableMapOf()

    /**
     * Navigable backstack of screenConfigs
     */
    var currentScreen = backstack.lastOrNull()?.let { mutableStateOf(it) } ?: mutableStateOf(ScreenConfig())

    /**
     * Get a mutable state of an element by its ID
     */

    fun <T : State> getElement(id: ID) = reactiveElementMap[id] as MutableState<Element<T>>
    /**
     * Get a mutable state of a component by its ID
     */

    fun <T : State> getComponent(id: ID) = reactiveElementMap[id] as MutableState<Component<T>>

    /**
     * Get a mutable state of a widget by its ID
     */
    fun <T : State> getWidget(id: ID) = reactiveElementMap[id] as MutableState<Widget<T>>

    /**
     * Push a new screenConfig onto the backstack and set the state of the screenConfig
     */
    fun pushScreen(config: ScreenConfig) {
        if (initScreenConfigMap[config.id] ?: true) {
            config.state.forEach {
                setState(it as Element<State>)
                setTriggers(it)
            }
            initScreenConfigMap[config.id] = false
        }
        addConfig(config)
    }

    /**
     * Used to implement a reactive update of the state of an element.
     */
    override fun postReactiveUpdate(element: Element<out State>) {
        reactiveElementMap[element.id]?.value = element
    }

    /**
     * Add a screenConfig to the backstack
     */
    private fun addConfig(config: ScreenConfig) {
        backstack.add(config)
        currentScreen.value = config
    }

    /**
     * Used to recursively add elements from the screenConfig to the client's element map
     */
    private fun setState(element: Element<out State>) {
        if (elementMap[element.id] != null && elementMap[element.id] != element) {
            error("Duplicate ID: ${element.id}")
        }
        reactiveElementMap[element.id] = mutableStateOf(element)
        elementMap[element.id] = element

        when (element) {
            is Component<*> -> Unit
            is Widget<*> -> element.state.children.forEach { setState(it) }
        }
    }

    /**
     * Used to recursively add triggers to the client's element map
     */
    private fun setTriggers(element: Element<out State>) {
        element.triggers.filterIsInstance<Trigger.OnStateUpdateTrigger>().forEach {
            it.action.targetIds.forEach { target ->
                val targetElement = elementMap.fGet(target.id)
                val updatedElement = targetElement.updateElement(
                    listeners = targetElement.listeners + RemoteAction(
                        action = it.action,
                        id = element.id,
                    )
                )

                elementMap[target.id] = updatedElement
                reactiveElementMap[target.id] = mutableStateOf(updatedElement)
            }
        }

        when (element) {
            is Component<*> -> Unit
            is Widget<*> -> element.state.children.forEach { setTriggers(it) }
        }
    }

    /**
     * Render either a component or widget
     * May remove the distinction between components and widgets in the future
     */
    @Composable
    fun RenderUI(
        element: Element<out State>,
    ) {
        when (element.state) {
            is WidgetState -> RenderWidget(widget = element as Widget<out WidgetState>)
            is ComponentState -> RenderComponent(component = element as Component<out ComponentState>)
        }
    }

    /**
     * Entry point used for rendering widgets
     */
    @Composable
    fun RenderWidget(
        widget: Widget<out WidgetState>
    ) {
        when (widget.state) {
            is BottomSheetState -> SBottomSheet(element = getWidget(widget.id))
            is CardState -> SCard(element = getWidget(widget.id))
            is ColumnState -> SColumn(element = getWidget(widget.id))
            is FlexRowState -> SFlexRow(element = getWidget(widget.id))
            is GridState -> SGrid(element = getWidget(widget.id))
            is RowState -> SRow(element = getWidget(widget.id))
            is FlexGridState -> SFlexGrid(element = getWidget(widget.id),)
            is FlexColumnState -> SFlexColumn(element = getWidget(widget.id))
            is UnknownWidgetState -> SUnknownWidget(element = getWidget(widget.id))
        }
    }

    /**
     * Entry point used for rendering components
     */
    @Composable
    fun RenderComponent(
        component: Component<out ComponentState>,
    ) {
        when (component.state) {
            is BottomRowState -> SBottomRow(element = getComponent(component.id))
            is ButtonState -> SButton(element = getComponent(component.id))
            is CustomState -> SCustom(element = getComponent(component.id))
            is DialogState -> SDialog(element = getComponent(component.id))
            is DropDownState -> SDropDown(element = getComponent(component.id))
            is IconState -> SIcon(element = getComponent(component.id))
            is ImageState -> SImage(element = getComponent(component.id))
            is LoaderState -> SLoader(element = getComponent(component.id))
            is LoadingShimmerState -> SLoadingShimmer(element = getComponent(component.id))
            is MenuState -> SMenu(element = getComponent(component.id))
            is RadioListState -> SRadioList(element = getComponent(component.id))
            is SearchState -> SSearch(element = getComponent(component.id))
            is SnackBarState -> SSnackBar(element = getComponent(component.id))
            is FieldState -> STextField(element = getComponent(component.id))
            is TextState -> SText(element = getComponent(component.id))
            is TickListState -> STickList(element = getComponent(component.id))
            is ToggleState -> SToggle(element = getComponent(component.id))
            is VideoState -> SVideo(element = getComponent(component.id))
            is SpaceState -> Space(element = getComponent(component.id))
            is UnknownComponentState -> SUnknownComponent(element = getComponent(component.id))
        }
    }

    companion object {
        /**
         * List of CommonClients created by the application
         */
        private val clients = mutableListOf<CommonClient>()

        /**
         * Method used to create a new CommonClient and add it to the list of common clients and general clients used in shared modules
         */
        private fun createClient() = CommonClient().also {
            clients.add(it)
            ClientHolder.addClient(it)
        }

        /**
         * CommonClient Provider used to ensure multiple clients are not accidentally created simultaneously
         */
        fun getClient() = clients.lastOrNull() ?: createClient()
    }
}