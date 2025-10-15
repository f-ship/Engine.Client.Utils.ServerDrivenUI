package ship.f.engine.client.utils.serverdrivenui2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ship.f.engine.client.utils.serverdrivenui2.state.*
import ship.f.engine.shared.utils.serverdrivenui2.client.ClientHolder2.get
import ship.f.engine.shared.utils.serverdrivenui2.client.CommonClient2
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Composable
fun Render(
    state: State2,
    modifier: Modifier = Modifier,
    client: CommonClient2 = get()
) {
    client.apply {
        when (state) {
            is BoxState2 -> Box2(s = getReactiveState(state.id), m = modifier)
            is ButtonState2 -> Button2(s = getReactiveState(state.id), m = modifier)
            is CardState2 -> Card2(s = getReactiveState(state.id), m = modifier)
            is CheckboxState2 -> CheckBox2(s = getReactiveState(state.id), m = modifier)
            is ColumnState2 -> Column2(s = getReactiveState(state.id), m = modifier)
            is HorizontalDividerState2 -> HorizontalDivider2(s = getReactiveState(state.id), m = modifier)
            is ImageState2 -> Image2(s = getReactiveState(state.id), m = modifier)
            is VideoState2 -> Video2(s = getReactiveState(state.id), m = modifier)
            is LazyColumnState2 -> LazyColumn2(s = getReactiveState(state.id), m = modifier)
            is LazyRowState2 -> LazyRow2(s = getReactiveState(state.id), m = modifier)
            is RowState2 -> Row2(s = getReactiveState(state.id), m = modifier)
            is FlowRowState2 -> FlowRow2(s = getReactiveState(state.id), m = modifier)
            is ScreenState2 -> Screen2(s = getReactiveState(state.id), m = modifier)
            is ScaffoldState2 -> Scaffold2(s = getReactiveState(state.id), m = modifier)
            is SpacerState2 -> Spacer2(s = getReactiveState(state.id), m = modifier)
            is SwitchState2 -> Switch2(s = getReactiveState(state.id), m = modifier)
            is SearchState2 -> Search2(s = getReactiveState(state.id), m = modifier)
            is TextFieldState2 -> TextField2(s = getReactiveState(state.id), m = modifier)
            is TextState2 -> Text2(s = getReactiveState(state.id), m = modifier)
            is VerticalDividerState2 -> VerticalDivider2(s = getReactiveState(state.id), m = modifier)
            is DropDownState2 -> DropDown2(s = getReactiveState(state.id), m = modifier)
            is BuilderState2 -> Builder2(s = getReactiveState(state.id), m = modifier)
            is FadeInState2 -> FadeIn2(s = getReactiveState(state.id), m = modifier)
            is UnknownState2 -> Unknown2(s = getReactiveState(state.id), m = modifier)
        }
    }
}