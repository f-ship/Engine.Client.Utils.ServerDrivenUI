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
import ship.f.engine.client.utils.serverdrivenui.RenderingContext
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.ID
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.TriggerAction
import ship.f.engine.shared.utils.serverdrivenui.action.Client.StateHolder
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.Center
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.End
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.Flex
import ship.f.engine.shared.utils.serverdrivenui.state.Arrangement.Start
import ship.f.engine.shared.utils.serverdrivenui.state.BottomSheetState
import ship.f.engine.shared.utils.serverdrivenui.state.CardState
import ship.f.engine.shared.utils.serverdrivenui.state.ColumnState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexColumnState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexGridState
import ship.f.engine.shared.utils.serverdrivenui.state.FlexRowState
import ship.f.engine.shared.utils.serverdrivenui.state.GridState
import ship.f.engine.shared.utils.serverdrivenui.state.RowState
import ship.f.engine.shared.utils.serverdrivenui.state.UnknownWidgetState

@Composable
fun SCard(
    state: MutableState<StateHolder<CardState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Card")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SBottomSheet(
    state: MutableState<StateHolder<BottomSheetState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("BottomSheet")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SRow(
    state: MutableState<StateHolder<RowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Row")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SColumn(
    state: MutableState<StateHolder<ColumnState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
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
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SFlexRow(
    state: MutableState<StateHolder<FlexRowState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("FlexRow")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SFlexColumn(
    state: MutableState<StateHolder<FlexColumnState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("FlexColumn")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SGrid(
    state: MutableState<StateHolder<GridState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("Grid")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SFlexGrid(
    state: MutableState<StateHolder<FlexGridState>>,
    triggerActions: List<TriggerAction>,
    id: ID,
    ctx: RenderingContext,
) {
    Text("FlexGrid")
    Column(modifier = Modifier.padding(16.dp)) {
        state.value.state.children.forEach {
            ctx.RenderUI(it, ctx)
        }
    }
}

@Composable
fun SUnknownWidget(
    state: MutableState<StateHolder<UnknownWidgetState>>,
    triggerActions: List<TriggerAction>,
    fallback: ScreenConfig.Fallback,
    id: ID,
    ctx: RenderingContext,
) {
    when (fallback) {
        is ScreenConfig.Fallback.Hide -> Unit
        is ScreenConfig.Fallback.OptionalUpdate -> Unit
        is ScreenConfig.Fallback.RequireUpdate -> Unit
        is ScreenConfig.Fallback.UI -> Unit
    }
}
