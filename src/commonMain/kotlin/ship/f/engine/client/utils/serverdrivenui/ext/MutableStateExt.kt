package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Component
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.state.ComponentState
import ship.f.engine.shared.utils.serverdrivenui.state.WidgetState

@Composable
fun <S: ComponentState>MutableState<Component<S>>.WithComponentState(block: @Composable S.() -> Unit) {
    block(this.value.state)
}

fun <S: ComponentState>MutableState<Component<S>>.update(block: S.() -> S): Component<S> {
    return value.update(block)
}

@Composable
fun <S: WidgetState>MutableState<Widget<S>>.WithWidgetState(block: @Composable S.() -> Unit) {
    block(this.value.state)
}

fun <S: WidgetState>MutableState<Widget<S>>.update(block: S.() -> S): Widget<S> {
    return value.update(block)
}
