package ship.f.engine.client.utils.serverdrivenui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ship.f.engine.client.utils.serverdrivenui.ext.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Fallback.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrange.Flex

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
    val modifier = size.toModifier()
    Row(
        horizontalArrangement = arrangement.toHorizontalArrangement(),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
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
        horizontalAlignment = if (arrangement is Flex || arrangement is Arrange.Center) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = arrangement.toVerticalArrangement(),
    ) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SStack(
    element: MutableState<Widget<StackState>>,
) = element.WithWidgetState {
    val modifier = size.toModifier()
    Box(
        modifier = modifier
            .then(background?.let{ Modifier.background(Color(it)) } ?: Modifier),
        contentAlignment = alignment.toAlignment(),
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
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .then(if (arrangement is Flex) Modifier.fillMaxWidth() else Modifier),
        horizontalAlignment = if (arrangement is Flex || arrangement is Arrange.Center) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = arrangement.toVerticalArrangement(),
    ) {
        items(children) {
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
