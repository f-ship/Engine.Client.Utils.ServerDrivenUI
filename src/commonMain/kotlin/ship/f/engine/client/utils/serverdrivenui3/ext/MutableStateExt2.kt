package ship.f.engine.client.utils.serverdrivenui3.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ship.f.engine.shared.utils.serverdrivenui2.state.State2

@Composable
fun <S: State2> MutableState<S>.WithState2(
    modifier: Modifier,
    block: @Composable S.(Modifier) -> Unit)
{
//    sduiLog(value.path, tag = "filtered index > WithState2") { value.id.name == "testZone" }
    value.run {
        LaunchedEffect(id) {
            onInitialRenderTrigger.trigger()
        }
        if (visible.value) {
            block(this, modifier.then(toModifier2()))
        }
    }
}

@Composable
fun State2.WithState2(
    modifier: Modifier,
    block: @Composable State2.(Modifier) -> Unit)
{
    run {
        LaunchedEffect(id) {
            onInitialRenderTrigger.trigger()
        }
        if (visible.value) {
            block(this, modifier.then(toModifier2()))
        }
    }
}