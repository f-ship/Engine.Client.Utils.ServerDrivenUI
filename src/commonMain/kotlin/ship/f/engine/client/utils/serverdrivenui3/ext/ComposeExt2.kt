package ship.f.engine.client.utils.serverdrivenui3.ext

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import org.jetbrains.compose.resources.painterResource
import ship.f.engine.shared.utils.serverdrivenui2.client3.Client3.Companion.client3
import ship.f.engine.shared.utils.serverdrivenui2.config.meta.models.ZoneViewModel3
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.*
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.LiveValue2.StaticDrawLiveValue2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Source2.*
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.computation.value.Draw2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ChildrenModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.InnerPaddingModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.PaddingModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.WeightModifier2.Weight2
import ship.f.engine.shared.utils.serverdrivenui2.config.trigger.modifiers.OnClickModifier2
import ship.f.engine.shared.utils.serverdrivenui2.ext.sduiLog
import ship.f.engine.shared.utils.serverdrivenui2.state.ImageState2
import ship.f.engine.shared.utils.serverdrivenui2.state.State2
import kotlin.math.cos
import kotlin.math.sin


/**
 * Launched effect typically allows one frame to slip by before finish executing.
 * In the case a local resource is used in the very first screen, this will lead to a crash as it will not be found.
 * Remember works well as a poor man's Blocking LaunchedEffect
 */
@Composable
fun BlockingLaunchedEffect2(key: Any? = Unit, block: () -> Unit) {
    remember(key, block)
}

@Composable
fun UIType2.toButtonColors2() = when (this) {
    is UIType2.Primary2 -> ButtonDefaults.buttonColors()
    is UIType2.Secondary2 -> ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    is UIType2.Tertiary2 -> ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun TextStyle2.toTextStyle2(weight: FontWeight2) = when (this) {
    is TextStyle2.BodyLarge2 -> MaterialTheme.typography.bodyLarge
    is TextStyle2.BodyMedium2 -> MaterialTheme.typography.bodyMedium
    is TextStyle2.BodySmall2 -> MaterialTheme.typography.bodySmall
    is TextStyle2.DisplayLarge2 -> MaterialTheme.typography.displayLarge
    is TextStyle2.DisplayMedium2 -> MaterialTheme.typography.displayMedium
    is TextStyle2.DisplaySmall2 -> MaterialTheme.typography.displaySmall
    is TextStyle2.HeadlineLarge2 -> MaterialTheme.typography.headlineLarge
    is TextStyle2.HeadlineMedium2 -> MaterialTheme.typography.headlineMedium
    is TextStyle2.HeadlineSmall2 -> MaterialTheme.typography.headlineSmall
    is TextStyle2.LabelLarge2 -> MaterialTheme.typography.labelLarge
    is TextStyle2.LabelMedium2 -> MaterialTheme.typography.labelMedium
    is TextStyle2.LabelSmall2 -> MaterialTheme.typography.labelSmall
    is TextStyle2.TitleLarge2 -> MaterialTheme.typography.titleLarge
    is TextStyle2.TitleMedium2 -> MaterialTheme.typography.titleMedium
    is TextStyle2.TitleSmall2 -> MaterialTheme.typography.titleSmall
    is TextStyle2.Custom2 -> MaterialTheme.typography.bodyMedium.copy(
        fontSize = size.sp,
        lineHeight = lineHeight.sp,
        fontWeight = fontWeight.toFontWeight2(),
    )
}.let {
    if (this !is TextStyle2.Custom2) it.copy(fontWeight = weight.toFontWeight2()) else it
}

@Composable
fun FontWeight2.toFontWeight2() = when (this) {
    is FontWeight2.Light2 -> W300
    is FontWeight2.Regular2 -> W400
    is FontWeight2.Medium2 -> W500
    is FontWeight2.SemiBold2 -> W600
    is FontWeight2.Bold2 -> W700
    is FontWeight2.ExtraBold2 -> W800
    is FontWeight2.BlackBold2 -> W900

}

@Composable
fun TextAlign2.toTextAlign2() = when (this) {
    is TextAlign2.Center2 -> androidx.compose.ui.text.style.TextAlign.Center
    is TextAlign2.End2 -> androidx.compose.ui.text.style.TextAlign.End
    is TextAlign2.Justify2 -> androidx.compose.ui.text.style.TextAlign.Justify
    is TextAlign2.Left2 -> androidx.compose.ui.text.style.TextAlign.Left
    is TextAlign2.Right2 -> androidx.compose.ui.text.style.TextAlign.Right
    is TextAlign2.Start2 -> androidx.compose.ui.text.style.TextAlign.Start
    is TextAlign2.Unspecified2 -> androidx.compose.ui.text.style.TextAlign.Unspecified
}

@Composable
fun ColorScheme2.Color2.toColor2() = when (this) {
    is ColorScheme2.Color2.Background -> MaterialTheme.colorScheme.background
    is ColorScheme2.Color2.Error -> MaterialTheme.colorScheme.error
    is ColorScheme2.Color2.ErrorContainer -> MaterialTheme.colorScheme.errorContainer
    is ColorScheme2.Color2.InverseOnSurface -> MaterialTheme.colorScheme.inverseOnSurface
    is ColorScheme2.Color2.InversePrimary -> MaterialTheme.colorScheme.inversePrimary
    is ColorScheme2.Color2.InverseSurface -> MaterialTheme.colorScheme.inverseSurface
    is ColorScheme2.Color2.OnBackground -> MaterialTheme.colorScheme.onBackground
    is ColorScheme2.Color2.OnError -> MaterialTheme.colorScheme.onError
    is ColorScheme2.Color2.OnErrorContainer -> MaterialTheme.colorScheme.onErrorContainer
    is ColorScheme2.Color2.OnPrimary -> MaterialTheme.colorScheme.onPrimary
    is ColorScheme2.Color2.OnPrimaryContainer -> MaterialTheme.colorScheme.onPrimaryContainer
    is ColorScheme2.Color2.OnSecondary -> MaterialTheme.colorScheme.onSecondary
    is ColorScheme2.Color2.OnSecondaryContainer -> MaterialTheme.colorScheme.onSecondaryContainer
    is ColorScheme2.Color2.OnSurface -> MaterialTheme.colorScheme.onSurface
    is ColorScheme2.Color2.OnSurfaceVariant -> MaterialTheme.colorScheme.onSurfaceVariant
    is ColorScheme2.Color2.OnTertiary -> MaterialTheme.colorScheme.onTertiary
    is ColorScheme2.Color2.OnTertiaryContainer -> MaterialTheme.colorScheme.onTertiaryContainer
    is ColorScheme2.Color2.Outline -> MaterialTheme.colorScheme.outline
    is ColorScheme2.Color2.OutlineVariant -> MaterialTheme.colorScheme.outlineVariant
    is ColorScheme2.Color2.Primary -> MaterialTheme.colorScheme.primary
    is ColorScheme2.Color2.PrimaryContainer -> MaterialTheme.colorScheme.primaryContainer
    is ColorScheme2.Color2.Scrim -> MaterialTheme.colorScheme.scrim
    is ColorScheme2.Color2.Secondary -> MaterialTheme.colorScheme.secondary
    is ColorScheme2.Color2.SecondaryContainer -> MaterialTheme.colorScheme.secondaryContainer
    is ColorScheme2.Color2.Surface -> MaterialTheme.colorScheme.surface
    is ColorScheme2.Color2.SurfaceBright -> MaterialTheme.colorScheme.surfaceBright
    is ColorScheme2.Color2.SurfaceContainer -> MaterialTheme.colorScheme.surfaceContainer
    is ColorScheme2.Color2.SurfaceContainerHigh -> MaterialTheme.colorScheme.surfaceContainerHigh
    is ColorScheme2.Color2.SurfaceContainerHighest -> MaterialTheme.colorScheme.surfaceContainerHighest
    is ColorScheme2.Color2.SurfaceContainerLow -> MaterialTheme.colorScheme.surfaceContainerLow
    is ColorScheme2.Color2.SurfaceContainerLowest -> MaterialTheme.colorScheme.surfaceContainerLowest
    is ColorScheme2.Color2.SurfaceDim -> MaterialTheme.colorScheme.surfaceDim
    is ColorScheme2.Color2.SurfaceTint -> MaterialTheme.colorScheme.surfaceTint
    is ColorScheme2.Color2.SurfaceVariant -> MaterialTheme.colorScheme.surfaceVariant
    is ColorScheme2.Color2.Tertiary -> MaterialTheme.colorScheme.tertiary
    is ColorScheme2.Color2.TertiaryContainer -> MaterialTheme.colorScheme.tertiaryContainer
    is ColorScheme2.Color2.AlphaColor2 -> Color.Black.copy(alpha = 0.5f) // TODO to fix by using proper sealed class hierarchy
    is ColorScheme2.Color2.CustomColor2 -> Color(color = color).copy(alpha = alpha)
    is ColorScheme2.Color2.Transparent -> Color.Transparent
    is ColorScheme2.Color2.Unspecified -> Color.Unspecified
    is ColorScheme2.Color2.Gradient -> Color.Unspecified.also { println("Gradient is not supported") }
}

@Composable
fun ColorScheme2.Color2.toBrush() = when (this) {
    is ColorScheme2.Color2.Gradient -> when (direction) {
        is ColorScheme2.Color2.Gradient.GradientDirection.Horizontal -> Brush.horizontalGradient(colors.map {
            it.color.toColor2().copy(alpha = it.alpha)
        })

        is ColorScheme2.Color2.Gradient.GradientDirection.Vertical -> Brush.verticalGradient(colors.map {
            it.color.toColor2().copy(alpha = it.alpha)
        })
    }

    else -> Brush.also { println("Brush is not supported for solid color as of yet, will be soon") }
} as Brush

@Composable
fun Draw2.toModifier2() = when (val draw = this) {
    is Draw2.Behind2.Circle2 -> {
        when (color) {
            is ColorScheme2.Color2.Gradient -> {
                val brush = color.toBrush()
                Modifier.drawBehind {
                    drawCircle(
                        brush = brush,
                        radius = size.maxDimension * 0.5f,
                    )
                }
            }

            else -> {
                val color = color.toColor2()
                Modifier.drawBehind {
                    drawCircle(
                        color = color,
                        radius = radius?.dp?.toPx() ?: (size.maxDimension * 0.5f),
                    )
                }
            }
        }
    }

    is Draw2.Behind2.Rectangle2 -> {
        when (color) {
            is ColorScheme2.Color2.Gradient -> {
                val brush = color.toBrush()
                Modifier.drawBehind {
                    val path = rectangleToPath(this@toModifier2)
                    drawPath(path, brush = brush)
                }
            }
            else -> {
                val color = color.toColor2()
                Modifier.drawBehind {
                    val path = rectangleToPath(this@toModifier2)
                    drawPath(path, color = color)
                }
            }
        }
    }

    is Draw2.Border2 -> Modifier
        .then( if (fill !is ColorScheme2.Color2.Unspecified) Modifier.background(fill.toColor2(), shape = shape.toShape2()) else Modifier)
        .border(width = width.dp, color = color.toColor2(), shape = shape.toShape2())
        .then(padding.toModifier2())

    is Draw2.Offset2 -> Modifier.offset(x = x.dp, y = y.dp)

    is Draw2.RadialOffset2 -> Modifier.offset(
        x = (cos(angle) * radius).dp,
        y = (sin(angle) * radius).dp
    )

    Draw2.Blank2 -> Modifier // Do nothing
    is Draw2.Behind2.BottomBorder2 -> {
        val color = color.toColor2()
        Modifier.drawBehind {
            val y = size.height
            drawLine(
                color = color,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth,
            )
        }
    }
}

fun DrawScope.rectangleToPath(rectangle: Draw2.Behind2.Rectangle2): Path {
    val rect = Rect(
        left = rectangle.roundRect.rect.left?.dp?.toPx() ?: 0f,
        right = rectangle.roundRect.rect.right?.dp?.toPx() ?: size.width,
        top = rectangle.roundRect.rect.top?.dp?.toPx() ?: 0f,
        bottom = rectangle.roundRect.rect.bottom?.dp?.toPx() ?: size.height
    )
    val roundRect = RoundRect(
        rect = rect,
        topLeft = rectangle.roundRect.topLeft.let { CornerRadius(it.dp.toPx(), it.dp.toPx()) },
        topRight = rectangle.roundRect.topRight.let { CornerRadius(it.dp.toPx(), it.dp.toPx()) },
        bottomLeft = rectangle.roundRect.bottomLeft.let { CornerRadius(it.dp.toPx(), it.dp.toPx()) },
        bottomRight = rectangle.roundRect.bottomRight.let { CornerRadius(it.dp.toPx(), it.dp.toPx()) },
    )

    return Path().apply { addRoundRect(roundRect) }
}

@Composable
fun Size2.toModifier2() = when (val size = this) {
    is Size2.DefaultSize2 -> Modifier
    is Size2.HorizontalSize2 -> size.toModifier2()
    is Size2.VerticalSize2 -> size.toModifier2()

    is Size2.Fill2 -> Modifier
        .fillMaxWidth(horizontalFill)
        .fillMaxHeight(verticalFill)

    is Size2.Fixed2 -> Modifier.size(
        width = size.width.dp,
        height = size.height.dp
    )

    is Size2.MatchParent2 -> Modifier
        .fillMaxSize()
        .wrapContentSize(unbounded = true)

    is Size2.Window2 -> Modifier.size(
        height = (LocalWindowInfo.current.containerSize.height / LocalDensity.current.density).dp,
        width = (LocalWindowInfo.current.containerSize.width / LocalDensity.current.density).dp
    )

    is Size2.IntrinsicHorizontalSize2 -> Modifier.width(
        when (size.horizontal) {
            is Size2.Intrinsic2.Max2 -> IntrinsicSize.Max
            is Size2.Intrinsic2.Min -> IntrinsicSize.Min
        }
    ).then(size.vertical?.toModifier2() ?: Modifier)

    is Size2.IntrinsicVerticalSize2 -> Modifier.height(
        when (size.vertical) {
            Size2.Intrinsic2.Max2 -> IntrinsicSize.Max
            Size2.Intrinsic2.Min -> IntrinsicSize.Min
        }
    ).then(size.horizontal?.toModifier2() ?: Modifier)

    is Size2.HorizontalFill2 -> Modifier.fillMaxWidth(size.fill).then(size.vertical?.toModifier2() ?: Modifier)

    is Size2.VerticalFill2 -> Modifier.fillMaxHeight(size.fill).then(size.horizontal?.toModifier2() ?: Modifier)
}

@Composable
fun Size2.HorizontalSize2.toModifier2() = when (val size = this) {
    is Size2.HorizontalOnlyFill2 -> Modifier.fillMaxWidth(size.fill)
    is Size2.FixedHorizontal2 -> Modifier.width(size.width.dp)
}

@Composable
fun Size2.VerticalSize2.toModifier2() = when (val size = this) {
    is Size2.FixedVertical2 -> Modifier.height(size.height.dp)
    is Size2.VerticalOnlyFill2 -> Modifier.fillMaxHeight(size.fill)
}

@Composable
fun ImageState2.ToImage2(modifier: Modifier) {
    val ctx = LocalPlatformContext.current

    val loader = remember(ctx) {
        ImageLoader.Builder(ctx)
            .components { add(KtorNetworkFetcherFactory()) }
            .logger(DebugLogger()) // see Coil logs
            .build()
    }

    when (val src = src) {
        is Resource2 -> Icon(
            painter = painterResource(client3.getResource(src.location)),
            contentDescription = contentDescription,
            modifier = modifier.clip(shape.toShape2()),
            tint = color.toColor2()
        )

        is Material2 -> Icon(
            imageVector = client3.getImageVector(src.location),
            contentDescription = contentDescription,
            modifier = modifier.clip(shape.toShape2()),
            tint = color.toColor2()
        )

        is Local2 -> Image(
            painter = painterResource(client3.getResource(src.location)),
            contentDescription = contentDescription,
            modifier = modifier.clip(shape.toShape2()),
            contentScale = contentScale.toContentScale2(),
//            colorFilter = ColorFilter.tint(color.toColor2()) TODO this caused major issues, now disabled
        )

        is Url2 -> AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(src.location)
                .build(),
            imageLoader = loader,
            contentDescription = contentDescription,
            modifier = modifier.clip(shape.toShape2()),
            onError = {
                /* TODO to use proper SDUI logging */
                println("BizClik Error loading image: ${it.result.throwable}")
            },
            contentScale = contentScale.toContentScale2(),
//            colorFilter = ColorFilter.tint(color.toColor2()) TODO this caused major issues, now disabled
        )
    }
}

@Composable
fun State2.addOnClick(modifier: Modifier, enabled: Boolean = true) = (this as? OnClickModifier2)?.let {
    if (onClickTrigger.actions.isNotEmpty()) {
        modifier.clickable(enabled = enabled, role = Role.Button) {
            it.onClickTrigger.trigger()
        }
    } else modifier
} ?: modifier

@Composable
fun ContentScale2.toContentScale2() = when (this) {
    is ContentScale2.Crop2 -> ContentScale.Crop
    is ContentScale2.FillBounds2 -> ContentScale.FillBounds
    is ContentScale2.FillHeight2 -> ContentScale.FillHeight
    is ContentScale2.FillWidth2 -> ContentScale.FillWidth
    is ContentScale2.Fit2 -> ContentScale.Fit
    is ContentScale2.Inside2 -> ContentScale.Inside
    is ContentScale2.None2 -> ContentScale.None
}

@Composable
fun textFieldDefaults2() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedPlaceholderColor = Color.Transparent,
    focusedTextColor = MaterialTheme.colorScheme.primary,
    unfocusedTextColor = Color.Unspecified,
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = Color(0xFFF4F4F4), // TODO hardcoded for now
    disabledIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = Color.Unspecified,
    errorTextColor = MaterialTheme.colorScheme.error,
    errorContainerColor = Color.Transparent,
)

@Composable
fun Shapes2.CornerBasedShape2.toShape2() = RoundedCornerShape(
    topStart = topStart.dp,
    topEnd = topEnd.dp,
    bottomEnd = bottomEnd.dp,
    bottomStart = bottomStart.dp,
)

@Composable
fun Border2.toBorder2() = BorderStroke(
    width = width.dp,
    color = color.toColor2()
)

@Composable
fun FieldType2.toVisualTransformation2() = remember(this) {
    when (this) {
        is FieldType2.Number, is FieldType2.Text -> VisualTransformation.None
        is FieldType2.Password -> PasswordVisualTransformation()
    }
}

@Composable
fun FieldType2.toKeyboardOptions2() = remember(this) {
    when (this) {
        is FieldType2.Number -> KeyboardOptions(keyboardType = KeyboardType.Number)
        is FieldType2.Text -> KeyboardOptions(keyboardType = KeyboardType.Text)
        is FieldType2.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
    }
}

@Composable
fun Arrangement2.toVerticalArrangement2() = when (this) {
    is Arrangement2.HorizonalArrangement2.Start2, is Arrangement2.VerticalArrangement2.Top2 -> Arrangement.Top
    is Arrangement2.HorizonalArrangement2.End2, is Arrangement2.VerticalArrangement2.Bottom2 -> Arrangement.Bottom
    is Arrangement2.HorizontalOrVerticalArrangement2.Center2 -> Arrangement.Center
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceAround2 -> Arrangement.SpaceAround
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceBetween2 -> Arrangement.SpaceBetween
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceEvenly2 -> Arrangement.SpaceEvenly
}

@Composable
fun Arrangement2.toHorizontalArrangement2() = when (this) {
    is Arrangement2.HorizonalArrangement2.Start2, is Arrangement2.VerticalArrangement2.Top2 -> Arrangement.Start
    is Arrangement2.HorizonalArrangement2.End2, is Arrangement2.VerticalArrangement2.Bottom2 -> Arrangement.End
    is Arrangement2.HorizontalOrVerticalArrangement2.Center2 -> Arrangement.Center
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceAround2 -> Arrangement.SpaceAround
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceBetween2 -> Arrangement.SpaceBetween
    is Arrangement2.HorizontalOrVerticalArrangement2.SpaceEvenly2 -> Arrangement.SpaceEvenly
}

@Composable
fun Alignment2.toHorizontalAlignment2() = when (this) {
    is Alignment2.HorizontalAndVerticalAlignment2.Center2,
    is Alignment2.VerticalAlignment2.CenterVertically2,
    is Alignment2.HorizontalAlignment2.CenterHorizontally2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopCenter2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomCenter2,
        -> Alignment.CenterHorizontally

    is Alignment2.HorizontalAlignment2.End2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopEnd2,
    is Alignment2.HorizontalAndVerticalAlignment2.CenterEnd2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomEnd2,
    is Alignment2.VerticalAlignment2.Bottom2,
        -> Alignment.End

    is Alignment2.HorizontalAlignment2.Start2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopStart2,
    is Alignment2.HorizontalAndVerticalAlignment2.CenterStart2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomStart2,
    is Alignment2.VerticalAlignment2.Top2,
        -> Alignment.Start
}

@Composable
fun Alignment2.toVerticalAlignment2() = when (this) {
    is Alignment2.HorizontalAndVerticalAlignment2.Center2,
    is Alignment2.VerticalAlignment2.CenterVertically2,
    is Alignment2.HorizontalAlignment2.CenterHorizontally2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopCenter2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomCenter2,
        -> Alignment.CenterVertically

    is Alignment2.HorizontalAlignment2.End2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopEnd2,
    is Alignment2.HorizontalAndVerticalAlignment2.CenterEnd2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomEnd2,
    is Alignment2.VerticalAlignment2.Bottom2,
        -> Alignment.Bottom

    is Alignment2.HorizontalAlignment2.Start2,
    is Alignment2.HorizontalAndVerticalAlignment2.TopStart2,
    is Alignment2.HorizontalAndVerticalAlignment2.CenterStart2,
    is Alignment2.HorizontalAndVerticalAlignment2.BottomStart2,
    is Alignment2.VerticalAlignment2.Top2,
        -> Alignment.Top
}

@Composable
fun Alignment2.toAlignment2(): Alignment = when (this) {
    is Alignment2.VerticalAlignment2.CenterVertically2 -> Alignment.CenterVertically
    is Alignment2.HorizontalAlignment2.CenterHorizontally2 -> Alignment.CenterHorizontally
    is Alignment2.HorizontalAlignment2.Start2 -> Alignment.Start
    is Alignment2.HorizontalAlignment2.End2 -> Alignment.End
    is Alignment2.VerticalAlignment2.Bottom2 -> Alignment.Bottom
    is Alignment2.VerticalAlignment2.Top2 -> Alignment.Top
    is Alignment2.HorizontalAndVerticalAlignment2.TopStart2 -> Alignment.TopStart
    is Alignment2.HorizontalAndVerticalAlignment2.TopCenter2 -> Alignment.TopCenter
    is Alignment2.HorizontalAndVerticalAlignment2.TopEnd2 -> Alignment.TopEnd
    is Alignment2.HorizontalAndVerticalAlignment2.CenterStart2 -> Alignment.CenterStart
    is Alignment2.HorizontalAndVerticalAlignment2.Center2 -> Alignment.Center
    is Alignment2.HorizontalAndVerticalAlignment2.CenterEnd2 -> Alignment.CenterEnd
    is Alignment2.HorizontalAndVerticalAlignment2.BottomStart2 -> Alignment.BottomStart
    is Alignment2.HorizontalAndVerticalAlignment2.BottomCenter2 -> Alignment.BottomCenter
    is Alignment2.HorizontalAndVerticalAlignment2.BottomEnd2 -> Alignment.BottomEnd
} as Alignment

@Composable
fun Alignment2.to2DAlignment2(): Alignment = when (this) {
    is Alignment2.VerticalAlignment2.CenterVertically2 -> Alignment.Center
    is Alignment2.HorizontalAlignment2.CenterHorizontally2 -> Alignment.Center
    is Alignment2.HorizontalAlignment2.Start2 -> Alignment.TopStart
    is Alignment2.HorizontalAlignment2.End2 -> Alignment.TopEnd
    is Alignment2.VerticalAlignment2.Bottom2 -> Alignment.BottomCenter
    is Alignment2.VerticalAlignment2.Top2 -> Alignment.TopCenter
    is Alignment2.HorizontalAndVerticalAlignment2.TopStart2 -> Alignment.TopStart
    is Alignment2.HorizontalAndVerticalAlignment2.TopCenter2 -> Alignment.TopCenter
    is Alignment2.HorizontalAndVerticalAlignment2.TopEnd2 -> Alignment.TopEnd
    is Alignment2.HorizontalAndVerticalAlignment2.CenterStart2 -> Alignment.CenterStart
    is Alignment2.HorizontalAndVerticalAlignment2.Center2 -> Alignment.Center
    is Alignment2.HorizontalAndVerticalAlignment2.CenterEnd2 -> Alignment.CenterEnd
    is Alignment2.HorizontalAndVerticalAlignment2.BottomStart2 -> Alignment.BottomStart
    is Alignment2.HorizontalAndVerticalAlignment2.BottomCenter2 -> Alignment.BottomCenter
    is Alignment2.HorizontalAndVerticalAlignment2.BottomEnd2 -> Alignment.BottomEnd
}

@Composable
fun ColumnScope.toModifier2(weight: Weight2?) = when (weight) {
    null -> Modifier
    else -> Modifier.weight(weight.value)
}

@Composable
fun RowScope.toModifier2(weight: Weight2?) = when (weight) {
    null -> Modifier
    else -> Modifier.weight(weight.value)
}

@Composable
fun PaddingValues2.toModifier2() = Modifier.padding(
    start = start.dp,
    end = end.dp,
    top = top.dp,
    bottom = bottom.dp
)
val modifierChain = mutableListOf<State2.() -> Modifier>()

@Composable
fun State2.toModifier2() = Modifier
    .then(size.toModifier2())
    .then(
        when (this) {
            is PaddingModifier2<*> -> this.padding.toModifier2()
            is InnerPaddingModifier2<*> -> this.padding.toModifier2()
            else -> Modifier
        }
    ).let {
        var modifier = it
        for (modifierProvider in modifierChain) {
            modifier = modifier.then(modifierProvider())
        }
        modifier
    }.let {
        var modifier = it
        for (draw in draws) {
            modifier = modifier.then(draw.toModifier2())
        }
        liveDraws?.forEach { liveDraw ->
            val draw = client3.computationEngine.computeConditionalBranchLive<StaticDrawLiveValue2>(liveDraw)
            modifier = modifier.then(draw.value.toModifier2())
        }
        liveDraws3?.forEach { liveDraw ->
            val draw = client3.computationEngine.computeConditionalValue(value = liveDraw, state2 = this)
            if (draw !is Draw2) {
                sduiLog("liveDraws3 > expected draw but got $draw")
                return@forEach
            }
            sduiLog("draws $id", metas.filterIsInstance<ZoneViewModel3>().firstOrNull()?.map, tag = "timer > draws")
            modifier = modifier.then(draw.toModifier2())
        }
        modifier
    }.let {
        (this as? ChildrenModifier2<*>)?.let { parent ->
            val sortedParent = parent.sort3?.let { sort -> client3.computationEngine.sort(sort, parent) } ?: parent
            val filteredParent = sortedParent.filter3?.let { filter -> client3.computationEngine.filter(filter, sortedParent) } ?: parent
            client3.computationEngine.index(filteredParent)
            val jumpToParent = filteredParent.jumpTo3?.let { jumpTo -> client3.computationEngine.jumpTo(jumpTo, filteredParent) } ?: filteredParent
            client3.computationEngine.focus(jumpToParent)
        }

        // Filter
        client3.commit()
        it
    }

fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    fun Dp.toPxInt(): Int = this.toPx().toInt()

    layout(
        placeable.width - (horizontal * 2).toPxInt(),
        placeable.height - (vertical * 2).toPxInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}

