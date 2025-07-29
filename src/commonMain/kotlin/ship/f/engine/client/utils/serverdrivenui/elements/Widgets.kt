package ship.f.engine.client.utils.serverdrivenui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ship.f.engine.client.utils.serverdrivenui.ext.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Element
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Fallback.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnClickTrigger
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrange.Flex

@Composable
fun SCard(
    element: MutableState<Widget<CardState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    if (visible) { // TODO Will eventually wrap is visible in this WithWidgetState, though maybe I won't for animation purposes
        Surface(
            shape = when (val shape = shape) {
                is RoundedRectangle -> RoundedCornerShape(
                    topStart = shape.topStart.dp,
                    topEnd = shape.topEnd.dp,
                    bottomStart = shape.bottomStart.dp,
                    bottomEnd = shape.bottomEnd.dp,
                )
            },
            border = BorderStroke(width = 1.dp, color = Color(0xFFD5D7DA)),
            modifier = modifier
                .then(size.toModifier())
                .then(padding.let { // TODO replace long winded method for handling padding
                    Modifier.padding(top = it.top.dp, bottom = it.bottom.dp, start = it.start.dp, end = it.end.dp)
                })
                .then(background?.let { Modifier.background(Color(it)) } ?: Modifier),
        ) {
            Column {
                children.forEach {
                    C.RenderUI(it)
                }
            }
        }
    }
}

@Composable
fun SBottomSheet(
    element: MutableState<Widget<BottomSheetState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Text("BottomSheet")
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SRow(
    element: MutableState<Widget<RowState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Row(
        horizontalArrangement = arrangement.toHorizontalArrangement(),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(size.toModifier())
            .then(padding.let { // TODO replace long winded method for handling padding
                Modifier.padding(top = it.top.dp, bottom = it.bottom.dp, start = it.start.dp, end = it.end.dp)
            }),
    ) {
        children.forEach {
            val m = (it.state as? ColumnState)?.let { state ->
                (state.size as? Weight)?.let { it.horizontalWeight?.let { Modifier.weight(weight = it) } }
            } ?: Modifier
            C.RenderUI(
                it,
                modifier = m,
            )
        }
    }
}

@Composable
fun SColumn(
    element: MutableState<Widget<ColumnState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Column(
        modifier = modifier // TODO create a much cleaner way to chain modifiers together
            .then( if (element.value.triggers.filterIsInstance<OnClickTrigger>().isNotEmpty()){
                Modifier.clickable(enabled = true, role = Role.Button) {
                    element.value.trigger<OnClickTrigger>()
                }
            } else {
                Modifier
            })
            .then(size.toModifier())
            .then(padding.let { // TODO replace long winded method for handling padding
                Modifier.padding(top = it.top.dp, bottom = it.bottom.dp, start = it.start.dp, end = it.end.dp)
            })
            .then(border?.let { Modifier.border(width = it.width.dp, color = Color(it.color)) } ?: Modifier)
            .then(if (arrangement is Flex) Modifier.fillMaxWidth() else Modifier)
            .then(background?.let { Modifier.background(Color(it)) } ?: Modifier),
        horizontalAlignment = if (arrangement is Flex || arrangement is Arrange.Center) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = arrangement.toVerticalArrangement(),
    ) {
        children.forEach {
            // TODO I've goofed this can never work whoops
            val m = (it.state as? ColumnState)?.let {
                (size as? Weight)?.let { it.verticalWeight?.let { Modifier.weight(weight = it) } }
            } ?: Modifier
            C.RenderUI(
                element = it,
                modifier = m,
            )
        }
    }
}

@Composable
fun SStack(
    element: MutableState<Widget<StackState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    if (visible) {
        Box(
            modifier = modifier
                .then(size.toModifier())
                .then(background?.let { Modifier.background(Color(it)) } ?: Modifier)
                .then(padding.let { // TODO replace long winded method for handling padding
                    Modifier.padding(top = it.top.dp, bottom = it.bottom.dp, start = it.start.dp, end = it.end.dp)
                }),
            contentAlignment = alignment.toAlignment(),
        ) {
            children.forEach {
                C.RenderUI(it)
            }
        }
    }
}

@Composable
fun SFlexRow(
    element: MutableState<Widget<FlexRowState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Text("FlexRow")
    Column(modifier = modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexColumn(
    element: MutableState<Widget<FlexColumnState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    LazyColumn(
        modifier = modifier
            .then(if (arrangement is Flex) Modifier.fillMaxWidth() else Modifier),
        horizontalAlignment = if (arrangement is Flex || arrangement is Arrange.Center) Alignment.CenterHorizontally else Alignment.Start,
        verticalArrangement = arrangement.toVerticalArrangement(),
    ) {
        val groupedChildren = mutableListOf<MutableList<Element<out State>>>()
        children.forEach {
            if (groupedChildren.isEmpty()) {
                groupedChildren.add(mutableListOf(it))
            } else if(it.state.isStickyHeader != groupedChildren.last().last().state.isStickyHeader) {
                groupedChildren.add(mutableListOf(it))
            } else {
                groupedChildren.last().add(it)
            }
        }

        groupedChildren.forEach { group ->
            if (group.last().state.isStickyHeader) {
                group.forEach { element ->
                    stickyHeader {
                        C.RenderUI(element)
                    }
                }
            } else {
                items(group) { element ->
                    C.RenderUI(element)
                }
            }
        }
    }
}

@Composable
fun SGrid(
    element: MutableState<Widget<GridState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Text("Grid")
    Column(modifier = modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SFlexGrid(
    element: MutableState<Widget<FlexGridState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    Text("FlexGrid")
    Column(modifier = modifier.padding(16.dp)) {
        children.forEach {
            C.RenderUI(it)
        }
    }
}

@Composable
fun SUnknownWidget(
    element: MutableState<Widget<UnknownWidgetState>>,
    modifier: Modifier = Modifier,
) = element.WithWidgetState {
    when (element.value.fallback) {
        is Hide -> Unit
        is OptionalUpdate -> Unit
        is RequireUpdate -> Unit
        is UI -> Unit
    }
}
