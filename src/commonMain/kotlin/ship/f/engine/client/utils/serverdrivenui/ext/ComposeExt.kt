package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.getResourceUri
import org.jetbrains.compose.resources.painterResource
import ship.f.engine.client.utils.serverdrivenui.generated.resources.*
import ship.f.engine.shared.utils.serverdrivenui.ScreenConfig
import ship.f.engine.shared.utils.serverdrivenui.action.Trigger.OnClickTrigger
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

    is Weight -> Modifier // TODO whoops weight doesn't fit in here
}

@Composable
fun STextAlign.toTextAlign() = when (this) {
    STextAlign.Center -> TextAlign.Center
    STextAlign.End -> TextAlign.End
    STextAlign.Start -> TextAlign.Start
}

@Composable
fun Padding.toModifier() = Modifier.padding(top = top.dp, bottom = bottom.dp, start = start.dp, end = end.dp)

@Composable
fun Border?.toModifier() = this?.let {
    Modifier.border(width = it.width.dp, color = Color(it.color))
} ?: Modifier

@Composable
fun Long?.toModifier() = this?.let { Modifier.background(Color(it)) } ?: Modifier
/**
 * Convenience method to convert a ColorSchemeState to a Material ColorScheme
 */

@Composable
fun ScreenConfig.Element<out State>.toOnClickModifier() = if (triggers.filterIsInstance<OnClickTrigger>().isNotEmpty()){
    Modifier.clickable(enabled = true, role = Role.Button) {
        trigger<OnClickTrigger>()
    }
} else {
    Modifier
}

@Composable
fun ScreenConfig.Element<out State>.toDefaultModifier() = Modifier
    .then( toOnClickModifier())
    .then(state.size.toModifier())
    .then(state.padding.toModifier())
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

@OptIn(InternalResourceApi::class)
@Composable
fun ImageState.Source.ToImage(
    modifier: Modifier = Modifier,
    accessibilityLabel: String? = null,
    scaleType: ScaleType = Default,
) {
    val scale = when (scaleType) {
        is Default -> ContentScale.Fit
        is FillWidth -> ContentScale.FillWidth
        is FillBounds -> ContentScale.FillBounds
    }
    when (this) {
        is ImageState.Source.Resource -> Icon(
            painter = painterResource(
                // TODO extract this out to it's own function
                when (resource) {
                    "icon-back" -> Res.drawable.icon_back
                    "compose-multiplatform" -> Res.drawable.compose_multiplatform
                    "icon-search" -> Res.drawable.icon_search
                    "icon-filter" -> Res.drawable.icon_filter
                    "icon-open" -> Res.drawable.icon_open
                    "icon-close" -> Res.drawable.icon_close
                    else -> Res.drawable.compose_multiplatform
                }
            ),
            contentDescription = accessibilityLabel,
            modifier = modifier.then((scaleType as? FillBounds)?.let {
                it.limit?.let { limit ->
                    when(limit) {
                        is FillBounds.Limit.Height -> Modifier.height(limit.value.dp)
                        is FillBounds.Limit.Width -> Modifier.width(limit.value.dp)
                    }
                } ?: Modifier
            } ?: Modifier),
        )

        is ImageState.Source.Url -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(url)
                    .build(),
                contentDescription = accessibilityLabel,
                modifier = modifier,
                error = painterResource(Res.drawable.compose_multiplatform), // TODO should have a different error image
                onError = {
                    println("Coil: Image failed to load: ${it.result.throwable}")
                },
                contentScale = scale,
            )
        }

        is ImageState.Source.Local -> {
            AsyncImage(
                model = getResourceUri(C.getDrawableResourcePath(file)), //TODO a terrible hack
                contentDescription = accessibilityLabel,
                modifier = modifier,
                error = painterResource(Res.drawable.compose_multiplatform),
                onError = {
                    println("Coil: Image failed to load: ${it.result.throwable}")
                },
                contentScale = scale,
            )
        }
    }
}