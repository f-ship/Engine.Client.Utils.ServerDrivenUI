package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ship.f.engine.shared.utils.serverdrivenui.state.*
import ship.f.engine.shared.utils.serverdrivenui.state.Align.*
import ship.f.engine.shared.utils.serverdrivenui.state.Arrange.Flex

fun Align.toAlignment() = when (this) {
    BottomCenter -> Alignment.BottomCenter
    Center -> Alignment.Center
    TopCenter -> Alignment.TopCenter
    BottomEnd -> Alignment.BottomEnd
    CenterEnd -> Alignment.CenterEnd
    TopEnd -> Alignment.TopEnd
    BottomStart -> Alignment.BottomStart
    CenterStart -> Alignment.CenterStart
    TopStart -> Alignment.TopStart
}

fun Arrange.toVerticalArrangement() = when (this) {
    is Arrange.Center, is Flex -> Arrangement.Center
    is Arrange.End -> Arrangement.Bottom
    is Arrange.Start -> Arrangement.Top
    is Arrange.SpaceBetween -> Arrangement.SpaceBetween
}

fun Arrange.toHorizontalArrangement() = when (this) {
    is Arrange.Center -> Arrangement.Center
    is Arrange.End -> Arrangement.End
    is Flex -> Arrangement.SpaceBetween
    is Arrange.SpaceBetween -> Arrangement.SpaceBetween
    is Arrange.Start -> Arrangement.Start
}

@Composable
fun Size.toModifier() = when (val size = this) {
    is DefaultSize -> Modifier

    is Fill -> Modifier
        .then(size.horizontalFill?.let { Modifier.fillMaxWidth(it) } ?: Modifier)
        .then(size.verticalFill?.let { Modifier.fillMaxHeight(it) } ?: Modifier)

    is Fixed -> Modifier
        .size(size.width.dp, size.height.dp)

    is Window -> Modifier
        .height((LocalWindowInfo.current.containerSize.height / LocalDensity.current.density).dp)
        .width((LocalWindowInfo.current.containerSize.width / LocalDensity.current.density).dp)
}

@Composable
fun STextAlign.toTextAlign() = when (this) {
    STextAlign.Center -> TextAlign.Center
    STextAlign.End -> TextAlign.End
    STextAlign.Start -> TextAlign.Start
}

/**
 * Convenience method to convert a ColorSchemeState to a Material ColorScheme
 */
fun ColorScheme.fromColorSchemeState(colorSchemeState: ColorSchemeState) = copy(
    primary = Color(colorSchemeState.primary),
    onPrimary = Color(colorSchemeState.onPrimary),
    onSecondaryContainer = Color(colorSchemeState.onSecondaryContainer),
    secondaryContainer = Color(colorSchemeState.secondaryContainer),
    background = Color(colorSchemeState.background),
    onBackground = Color(colorSchemeState.onBackground),
    surface = Color(colorSchemeState.surface),
    onSurface = Color(colorSchemeState.onSurface),
    surfaceVariant = Color(colorSchemeState.surfaceVariant),
    onSurfaceVariant = Color(colorSchemeState.onSurfaceVariant),
    outline = Color(colorSchemeState.outline),
)