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
import ship.f.engine.client.utils.serverdrivenui.CommonClient
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.ID
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.*

@Composable
fun SCard(
    state: MutableState<StateHolder<CardState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Card")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SBottomSheet(
    state: MutableState<StateHolder<BottomSheetState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("BottomSheet")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SRow(
    state: MutableState<StateHolder<RowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Row")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SColumn(
    state: MutableState<StateHolder<ColumnState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .then(if (state.value.state.arrangement is Flex) Modifier.fillMaxWidth() else Modifier),
        verticalArrangement = when (state.value.state.arrangement) {
            is Center, is Flex -> Arrangement.Center
            is End -> Arrangement.Bottom
            is Start -> Arrangement.Top
        },
    ) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SFlexRow(
    state: MutableState<StateHolder<FlexRowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("FlexRow")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SFlexColumn(
    state: MutableState<StateHolder<FlexColumnState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("FlexColumn")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SGrid(
    state: MutableState<StateHolder<GridState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("Grid")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SFlexGrid(
    state: MutableState<StateHolder<FlexGridState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    c: CommonClient,
) {
    Text("FlexGrid")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            c.RenderUI(it)
        }
    }
}

@Composable
fun SUnknownWidget(
    state: MutableState<StateHolder<UnknownWidgetState>>,
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
