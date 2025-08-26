package ship.f.engine.client.utils.serverdrivenui.ext

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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

fun Align.toHorizontalAlignment() = when(this) {
    BottomCenter -> Alignment.CenterHorizontally
    BottomEnd -> Alignment.End
    BottomStart -> Alignment.Start
    Center -> Alignment.CenterHorizontally
    CenterEnd -> Alignment.End
    CenterStart -> Alignment.Start
    TopCenter -> Alignment.CenterHorizontally
    TopEnd -> Alignment.End
    TopStart -> Alignment.Start
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

    is MatchParent -> Modifier
        .fillMaxHeight() // Match parent height
        .wrapContentHeight(unbounded = true) // Don't influence parent height
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
    Modifier.border(width = it.width.dp, color = it.color.toColor())
} ?: Modifier

@Composable
fun Border?.toBorderStroke() = this?.let {
    BorderStroke(it.width.dp, it.color.toColor())
}

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
fun ColorScheme.fromColorSchemeState(colorScheme: ColorSchemeState) = copy(
    primary = Color(colorScheme.primary),
    onPrimary = Color(colorScheme.onPrimary),
    secondary = Color(colorScheme.secondary),
    onSecondary = Color(colorScheme.onSecondary),
    onSecondaryContainer = Color(colorScheme.onSecondaryContainer),
    secondaryContainer = Color(colorScheme.secondaryContainer),
    background = Color(colorScheme.background),
    onBackground = Color(colorScheme.onBackground),
    surface = Color(colorScheme.surface),
    onSurface = Color(colorScheme.onSurface),
    surfaceVariant = Color(colorScheme.surfaceVariant),
    onSurfaceVariant = Color(colorScheme.onSurfaceVariant),
    outline = Color(colorScheme.outline),
    outlineVariant = Color(colorScheme.outlineVariant),
)

@OptIn(InternalResourceApi::class)
@Composable
fun ImageState.Source.ToImage(
    modifier: Modifier = Modifier,
    accessibilityLabel: String? = null,
    scaleType: ScaleType = Default,
    color: ColorSchemeState.Color? = null,
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
            tint = color.toColor()
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
                colorFilter = color?.toColor()?.let { ColorFilter.tint(it) }
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
                colorFilter = color?.toColor()?.let { ColorFilter.tint(it) }
            )
        }
    }
}

@Composable
fun ColorSchemeState.Color?.toColor() = when(this){
    is ColorSchemeState.Color.Background -> MaterialTheme.colorScheme.background
    is ColorSchemeState.Color.OnBackground -> MaterialTheme.colorScheme.onBackground
    is ColorSchemeState.Color.OnPrimary -> MaterialTheme.colorScheme.onPrimary
    is ColorSchemeState.Color.OnSecondary -> MaterialTheme.colorScheme.onSecondary
    is ColorSchemeState.Color.OnSecondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
    is ColorSchemeState.Color.OnSurface -> MaterialTheme.colorScheme.onSurface
    is ColorSchemeState.Color.OnSurfaceVariant -> MaterialTheme.colorScheme.onSurfaceVariant
    is ColorSchemeState.Color.Outline -> MaterialTheme.colorScheme.outline
    is ColorSchemeState.Color.OutlineVariant -> MaterialTheme.colorScheme.outlineVariant
    is ColorSchemeState.Color.Primary -> MaterialTheme.colorScheme.primary
    is ColorSchemeState.Color.Secondary -> MaterialTheme.colorScheme.secondary
    is ColorSchemeState.Color.SecondaryContainer -> MaterialTheme.colorScheme.secondaryContainer
    is ColorSchemeState.Color.Surface -> MaterialTheme.colorScheme.surface
    is ColorSchemeState.Color.SurfaceVariant -> MaterialTheme.colorScheme.surfaceVariant
    is ColorSchemeState.Color.Error -> MaterialTheme.colorScheme.error
    is ColorSchemeState.Color.OnError -> MaterialTheme.colorScheme.onError
    null -> Color.Unspecified
}
