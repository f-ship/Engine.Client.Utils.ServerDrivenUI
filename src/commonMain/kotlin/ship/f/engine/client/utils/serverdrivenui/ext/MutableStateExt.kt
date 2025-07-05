package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Component
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig.Widget
import ship.f.engine.shared.utils.serverdrivenui.state.ComponentState
import ship.f.engine.shared.utils.serverdrivenui.state.WidgetState

/**
 * Extension used to provide context of a component to be the state of the component for convenience
 */
@Composable
fun <S: ComponentState>MutableState<Component<S>>.WithComponentState(block: @Composable S.() -> Unit) {
    block(this.value.state)
}

/**
 * Extension used as a convenience to bypass accessing the value property on the component mutable state
 */
fun <S: ComponentState>MutableState<Component<S>>.update(block: S.() -> S): Component<S> {
    return value.update(block)
}

/**
 * Extension used to provide context of a widget to be the state of the component for convenience
 */
@Composable
fun <S: WidgetState>MutableState<Widget<S>>.WithWidgetState(block: @Composable S.() -> Unit) {
    block(this.value.state)
}

/**
 * Extension used as a convenience to bypass accessing the value property on the widget mutable state
 */
fun <S: WidgetState>MutableState<Widget<S>>.update(block: S.() -> S): Widget<S> {
    return value.update(block)
}
