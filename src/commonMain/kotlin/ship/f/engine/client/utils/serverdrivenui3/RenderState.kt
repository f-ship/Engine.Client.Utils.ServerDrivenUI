package ship.f.engine.client.utils.serverdrivenui3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ship.f.engine.client.utils.serverdrivenui3.ext.WithState2
import ship.f.engine.client.utils.serverdrivenui3.state.*
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.client3.Path3
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
import ship.f.engine.shared.utils.serverdrivenui2.state.*

@Composable
fun Render(
    state: State2,
    modifier: Modifier = Modifier,
    client: Client3 = client3
) {
    client.apply {
        when(state.path3) {
            is Path3.Init -> error("Should not every be rendering uninitialized states as will lead to unpredictable behaviour ${state.id}")
            is Path3.Anon -> RenderStatic(
                state = state,
                modifier = modifier
            )
            is Path3.Local, is Path3.Global -> RenderDynamic(
                state = state,
                modifier = modifier,
                client = client,
            )
        }
    }
}

@Composable
fun RenderDynamic(
    state: State2,
    modifier: Modifier = Modifier,
    client: Client3 = client3
) {
    client.apply {
        when (state) {
            is BoxState2 -> Box2(s = getReactive(state.path3), m = modifier)
            is ButtonState2 -> Button2(s = getReactive(state.path3), m = modifier)
            is CardState2 -> Card2(s = getReactive(state.path3), m = modifier)
            is CheckboxState2 -> CheckBox2(s = getReactive(state.path3), m = modifier)
            is ColumnState2 -> Column2(s = getReactive(state.path3), m = modifier)
            is HorizontalDividerState2 -> HorizontalDivider2(s = getReactive(state.path3), m = modifier)
            is ImageState2 -> Image2(s = getReactive(state.path3), m = modifier)
            is VideoState2 -> Video2(s = getReactive(state.path3), m = modifier)
            is LazyColumnState2 -> LazyColumn2(s = getReactive(state.path3), m = modifier)
            is LazyGridState2 -> LazyGrid2(s = getReactive(state.path3), m = modifier)
            is LazyRowState2 -> LazyRow2(s = getReactive(state.path3), m = modifier)
            is RowState2 -> Row2(s = getReactive(state.path3), m = modifier)
            is FlowRowState2 -> FlowRow2(s = getReactive(state.path3), m = modifier)
            is ScreenState2 -> Screen2(s = getReactive(state.path3), m = modifier)
            is ScaffoldState2 -> Scaffold2(s = getReactive(state.path3), m = modifier)
            is SpacerState2 -> Spacer2(s = getReactive(state.path3), m = modifier)
            is SwitchState2 -> Switch2(s = getReactive(state.path3), m = modifier)
            is SearchState2 -> Search2(s = getReactive(state.path3), m = modifier)
            is TextFieldState2 -> TextField2(s = getReactive(state.path3), m = modifier)
            is TextState2 -> Text2(s = getReactive(state.path3), m = modifier)
            is VerticalDividerState2 -> VerticalDivider2(s = getReactive(state.path3), m = modifier)
            is DropDownState2 -> DropDown2(s = getReactive(state.path3), m = modifier)
            is BuilderState2 -> Builder2(s = getReactive(state.path3), m = modifier)
            is FadeInState2 -> FadeIn2(s = getReactive(state.path3), m = modifier)
            is UnknownState2 -> Unknown2(s = getReactive(state.path3), m = modifier)
            is CameraGalleryState2 -> CameraGallery2(s = getReactive(state.path3), m = modifier)
            is WebViewState2 -> WebView2(s = getReactive(state.path3), m = modifier)
            is DialogState2 -> Dialog2(s = getReactive(state.path3), m = modifier)
            is VariantState2 -> Variant2(s = getReactive(state.path3), m = modifier)
            is RefState2 -> {
                sduiLog(client.idPaths.keys.filter { it.isGlobal }, tag = "RenderDynamic > RefState2 > IdPath > Globals")
                sduiLog(client.reactiveStates.keys.filterIsInstance<Path3.Global>(), tag = "RenderDynamic > RefState2 > ReactiveState > Globals")
                sduiLog(client.stateQueue, tag = "RenderDynamic > RefState2 > StateQueue > Locals")
                Render(state = client3.getReactive<State2>(state.path3).value, modifier = modifier)
            }
        }
    }
}

@Composable
fun RenderStatic(
    state: State2,
    modifier: Modifier = Modifier,
) {
    state.WithState2(modifier) { modifier ->
        when (state) {
            is BoxState2 -> state.Box2(modifier = modifier)
            is ButtonState2 -> state.Button2(modifier = modifier)
            is CardState2 -> state.Card2(modifier = modifier)
            is CheckboxState2 -> state.CheckBox2(modifier = modifier)
            is ColumnState2 -> state.Column2(modifier = modifier)
            is HorizontalDividerState2 -> state.HorizontalDivider2(modifier = modifier)
            is ImageState2 -> state.Image2(modifier = modifier)
            is VideoState2 -> state.Video2(modifier = modifier)
            is LazyColumnState2 -> state.LazyColumn2(modifier = modifier)
            is LazyGridState2 -> state.LazyGrid2(modifier = modifier)
            is LazyRowState2 -> state.LazyRow2(modifier = modifier)
            is RowState2 -> state.Row2(modifier = modifier)
            is FlowRowState2 -> state.FlowRow2(modifier = modifier)
            is ScreenState2 -> state.Screen2(modifier = modifier)
            is ScaffoldState2 -> state.Scaffold2(modifier = modifier)
            is SpacerState2 -> state.Spacer2(modifier = modifier)
            is SwitchState2 -> state.Switch2(modifier = modifier)
            is SearchState2 -> state.Search2(modifier = modifier)
            is TextFieldState2 -> state.TextField2(modifier = modifier)
            is TextState2 -> state.Text2(modifier = modifier)
            is VerticalDividerState2 -> state.VerticalDivider2(modifier = modifier)
            is DropDownState2 -> state.DropDown2(modifier = modifier)
            is BuilderState2 -> state.Builder2(modifier = modifier)
            is FadeInState2 -> state.FadeIn2(modifier = modifier)
            is UnknownState2 -> state.Unknown2(modifier = modifier)
            is CameraGalleryState2 -> state.CameraGallery2(modifier = modifier)
            is WebViewState2 -> state.WebView2(modifier = modifier)
            is DialogState2 -> state.Dialog2(modifier = modifier)
            is VariantState2 -> state.Variant2(modifier = modifier)
            is RefState2 -> {
                Render(state = client3.getReactive<State2>(state.path3).value, modifier = modifier)
            }
        }
    }
}