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
import ship.f.engine.client.utils.serverdrivenui.WithWidgetState
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Fallback.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.*

@Composable
fun SCard(
    element: MutableState<Widget<CardState>>,
) = element.WithWidgetState {
    Text("Card")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SBottomSheet(
    element: MutableState<Widget<BottomSheetState>>,
) = element.WithWidgetState {
    Text("BottomSheet")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SRow(
    element: MutableState<Widget<RowState>>,
) = element.WithWidgetState {
    Text("Row")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SColumn(
    element: MutableState<Widget<ColumnState>>,
) = element.WithWidgetState {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .then(if (arrangement is Flex) Modifier.fillMaxWidth() else Modifier),
        verticalArrangement = when (arrangement) {
            is Center, is Flex -> Arrangement.Center
            is End -> Arrangement.Bottom
            is Start -> Arrangement.Top
        },
    ) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexRow(
    element: MutableState<Widget<FlexRowState>>,
) = element.WithWidgetState {
    Text("FlexRow")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexColumn(
    element: MutableState<Widget<FlexColumnState>>,
) = element.WithWidgetState {
    Text("FlexColumn")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SGrid(
    element: MutableState<Widget<GridState>>,
) = element.WithWidgetState {
    Text("Grid")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexGrid(
    element: MutableState<Widget<FlexGridState>>,
) = element.WithWidgetState {
    Text("FlexGrid")
    Column(modifier = Modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SUnknownWidget(
    element: MutableState<Widget<UnknownWidgetState>>,
) = element.WithWidgetState {
    when (element.value.fallback) {
        is Hide -> Unit
        is OptionalUpdate -> Unit
        is RequireUpdate -> Unit
        is UI -> Unit
    }
}
