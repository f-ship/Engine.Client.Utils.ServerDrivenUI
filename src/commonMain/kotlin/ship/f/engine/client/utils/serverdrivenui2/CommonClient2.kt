package ship.f.engine.client.utils.serverdrivenui2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui2.CommonClient2.FocusDirection.*
import ship.f.engine.client.utils.serverdrivenui2.ext.Debug
import ship.f.engine.client.utils.serverdrivenui2.state.*
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2
import ship.f.engine.shared.utils.serverdrivenui2.client.Client2
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2
import ship.f.engine.shared.utils.serverdrivenui2.config.action.models.ToJsonAction2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2.MetaId2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2.StateId2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ChildrenModifier2
import ship.f.engine.shared.utils.serverdrivenui2.ext.g2
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Suppress("UNCHECKED_CAST")
open class CommonClient2 protected constructor(override val projectName: String? = null) : Client2() {

    @Debug
    val focusedState: MutableState<StateId2?> = mutableStateOf(StateId2("agenda"))

    @Debug
    var isDebug: Boolean = true

    @Debug
    fun changeFocus(direction: FocusDirection) { //TODO rewrite ugly debugging code with build variant
        when(direction) {
            After -> {
                focusedState.value?.let { focused ->
                    val id = reverseIdMap[focusedState.value]
                    if (id == null) return
                    (get<State2>(id) as? ChildrenModifier2<State2>)?.let { parent ->
                        val index = parent.children.indexOfFirst { it.id == focused }
                        if (index + 1 < parent.children.size) {
                            val focus = parent.children[index + 1].id
                            focusedState.value = focus
                            get<State2>(focus).apply {
                                update{ reset() }
                            }
                            ToJsonAction2(
                                targetStateId = focus,
                                targetMetaId = MetaId2("json-meta")
                            ).run(
                                state = get<State2>(focus),
                                client = this@CommonClient2
                            )
                        }
                    }
                }
            }
            Before -> {
                focusedState.value?.let { focused ->
                    val id = reverseIdMap[focusedState.value]
                    if (id == null) return
                    (get<State2>(id) as? ChildrenModifier2<State2>)?.let { parent ->
                        val index = parent.children.indexOfFirst { it.id == focused }
                        if (index - 1 >= 0) {
                            val focus = parent.children[index - 1].id
                            focusedState.value = focus
                            get<State2>(focus).apply {
                                update{ reset() }
                            }
                            ToJsonAction2(
                                targetStateId = focus,
                                targetMetaId = MetaId2("json-meta")
                            ).run(
                                state = get<State2>(focus),
                                client = this@CommonClient2
                            )
                        }
                    }
                }
            }
            Down -> {
                focusedState.value?.let {
                    (get<State2>(it) as? ChildrenModifier2<State2>)?.let { parent ->
                        val focus = parent.children.firstOrNull()?.id
                        if (focus == null) return
                        focusedState.value = focus
                        get<State2>(focus).apply {
                            update{ reset() }
                        }
                        ToJsonAction2(
                            targetStateId = focus,
                            targetMetaId = MetaId2("json-meta")
                        ).run(
                            state = get<State2>(focus),
                            client = this@CommonClient2
                        )
                    }
                }
            }
            Up -> {
                val focus = reverseIdMap[focusedState.value]
                if (focus == null) return
                focusedState.value = focus
                get<State2>(focus).apply {
                    update{ reset() }
                }
                ToJsonAction2(
                    targetStateId = focus,
                    targetMetaId = MetaId2("json-meta")
                ).run(
                    state = get<State2>(focus),
                    client = this@CommonClient2
                )
            }
        }
    }

    @Debug
    sealed class FocusDirection {
        data object Up : FocusDirection()
        data object Down : FocusDirection()
        data object Before : FocusDirection()
        data object After : FocusDirection()
    }

    val reactiveStateMap: MutableMap<Id2, MutableState<State2>> = mutableMapOf()
    val reactiveBackStack: SnapshotStateList<BackStackEntry2> = mutableStateListOf()

    val resourceMap: MutableMap<String, Resource> = mutableMapOf()
    fun addResource(id: String, resource: Resource) = resourceMap.put(id, resource)
    fun addResources(resources: Map<String, Resource>) = resourceMap.putAll(resources)
    inline fun <reified R : Resource> getResource(id: String) = resourceMap.g2(id) as R

    fun <T : State2> getReactiveState(id: StateId2) = reactiveStateMap.g2(id) as MutableState<T>

    override fun reactiveUpdate(state: State2) {
        if (reactiveStateMap[state.id] == null) {
            reactiveStateMap[state.id] = mutableStateOf(state)
        }
        getReactiveState<State2>(state.id).value = state
    }
    override fun reactivePush() {
        reactiveBackStack.add(backstack.last())
    }
    override fun reactivePop() {
        reactiveBackStack.apply {
            clear()
            addAll(backstack)
        }
    }


    @Composable
    fun Render(
        state: State2,
        modifier: Modifier = Modifier,
    ) {
        when (state) {
            is BoxState2 -> Box2(s = getReactiveState(state.id), m = modifier)
            is ButtonState2 -> Button2(s = getReactiveState(state.id), m = modifier)
            is CardState2 -> Card2(s = getReactiveState(state.id), m = modifier)
            is CheckboxState2 -> CheckBox2(s = getReactiveState(state.id), m = modifier)
            is ColumnState2 -> Column2(s = getReactiveState(state.id), m = modifier)
            is HorizontalDividerState2 -> HorizontalDivider2(s = getReactiveState(state.id), m = modifier)
            is ImageState2 -> Image2(s = getReactiveState(state.id), m = modifier)
            is LazyColumnState2 -> LazyColumn2(s = getReactiveState(state.id), m = modifier)
            is LazyRowState2 -> LazyRow2(s = getReactiveState(state.id), m = modifier)
            is RowState2 -> Row2(s = getReactiveState(state.id), m = modifier)
            is ScreenState2 -> Screen2(s = getReactiveState(state.id), m = modifier)
            is SpacerState2 -> Spacer2(s = getReactiveState(state.id), m = modifier)
            is SwitchState2 -> Switch2(s = getReactiveState(state.id), m = modifier)
            is TextFieldState2 -> TextField2(s = getReactiveState(state.id), m = modifier)
            is TextState2 -> Text2(s = getReactiveState(state.id), m = modifier)
            is VerticalDividerState2 -> VerticalDivider2(s = getReactiveState(state.id), m = modifier)
            is DropDownState2 -> DropDown2(s = getReactiveState(state.id), m = modifier)
            is BuilderState2 -> Builder2(s = getReactiveState(state.id), m = modifier)
            is UnknownState2 -> Unknown2(s = getReactiveState(state.id), m = modifier)
        }
    }

    companion object {
        fun create(projectName: String? = null) = ClientHolder2.add(projectName) { CommonClient2(projectName = it) }
    }
}