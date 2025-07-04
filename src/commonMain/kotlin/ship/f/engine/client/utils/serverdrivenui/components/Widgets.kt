package ship.f.engine.client.utils.serverdrivenui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ship.f.engine.client.utils.serverdrivenui.C
import ship.f.engine.client.utils.serverdrivenui.CommonClient
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.*
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.*

@Composable
fun SCard(
    element: MutableState<Widget<CardState>>,
) {
    Text("Card")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SBottomSheet(
    element: MutableState<Widget<BottomSheetState>>,
) {
    Text("BottomSheet")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SRow(
    element: MutableState<Widget<RowState>>,
) {
    Text("Row")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SColumn(
    element: MutableState<Widget<ColumnState>>,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .then(if (element.value.state.arrangement is Flex) Modifier.fillMaxWidth() else Modifier),
        verticalArrangement = when (element.value.state.arrangement) {
            is Center, is Flex -> Arrangement.Center
            is End -> Arrangement.Bottom
            is Start -> Arrangement.Top
        },
    ) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexRow(
    element: MutableState<Widget<FlexRowState>>,
) {
    Text("FlexRow")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexColumn(
    element: MutableState<Widget<FlexColumnState>>,
) {
    Text("FlexColumn")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SGrid(
    element: MutableState<Widget<GridState>>,
) {
    Text("Grid")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexGrid(
    element: MutableState<Widget<FlexGridState>>,
) {
    Text("FlexGrid")
    Column(modifier = Modifier.padding(16.dp)) {
        element.value.state.children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SUnknownWidget(
    element: MutableState<Widget<UnknownWidgetState>>,
    triggerActions: List<TriggerAction>,
    fallback: ScreenConfig.Fallback,
    id: ID,
    c: CommonClient,
) {
    when (fallback) {
        is ScreenConfig.Fallback.Hide -> Unit
        is ScreenConfig.Fallback.OptionalUpdate -> Unit
        is ScreenConfig.Fallback.RequireUpdate -> Unit
        is ScreenConfig.Fallback.UI -> Unit
    }
}
