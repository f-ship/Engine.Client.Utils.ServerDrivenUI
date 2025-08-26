package ship.f.engine.client.utils.serverdrivenui2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.Resource
import ship.f.engine.client.utils.serverdrivenui2.state.*
import ship.f.engine.shared.utils.serverdrivenui2.client.BackStackEntry2
import ship.f.engine.shared.utils.serverdrivenui2.client.Client2
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Id2.StateId2
import ship.f.engine.shared.utils.serverdrivenui2.ext.g2
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Suppress("UNCHECKED_CAST")
class CommonClient2 private constructor(override val projectName: String? = null) : Client2() {
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
            is UnknownState2 -> Unit
        }
    }

    companion object {
        fun create(projectName: String? = null) = ClientHolder2.add(projectName) { CommonClient2(projectName = it) }
    }
}