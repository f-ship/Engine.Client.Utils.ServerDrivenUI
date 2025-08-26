package ship.f.engine.client.utils.serverdrivenui2.ext

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W800
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import org.jetbrains.compose.resources.painterResource
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.*
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Source2.*
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.InnerPaddingModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.PaddingModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.WeightModifier2.Weight2
import ship.f.engine.shared.utils.serverdrivenui2.config.trigger.modifiers.OnClickModifier2
import ship.f.engine.shared.utils.serverdrivenui2.state.ImageState2
import ship.f.engine.shared.utils.serverdrivenui2.state.State2


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
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
}.copy(fontWeight = weight.toFontWeight2())

@Composable
fun FontWeight2.toFontWeight2() = when (this) {
    is FontWeight2.Bold2 -> W800
    is FontWeight2.ExtraBold2 -> W900
    is FontWeight2.Normal2 -> W400
    is FontWeight2.SemiBold2 -> W600
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
    is ColorScheme2.Color2.Unspecified -> Color.Unspecified
}

@Composable
fun Size2.toModifier2() = when (val size = this) {
    is Size2.DefaultSize2 -> Modifier

    is Size2.Fill2 -> Modifier
        .fillMaxWidth(horizontalFill)
        .fillMaxHeight(verticalFill)

    is Size2.Fixed2 -> Modifier.size(
        width = size.width.dp,
        height = size.height.dp
    )

    is Size2.HorizontalFill2 -> Modifier
        .fillMaxWidth(size.fill)

    is Size2.VerticalFill2 -> Modifier
        .fillMaxHeight(size.fill)

    is Size2.MatchParent2 -> Modifier
        .fillMaxSize()
        .wrapContentSize(unbounded = true)

    is Size2.Window2 -> Modifier.size(
        height = (LocalWindowInfo.current.containerSize.height / LocalDensity.current.density).dp,
        width = (LocalWindowInfo.current.containerSize.width / LocalDensity.current.density).dp
    )
}

@Composable
fun ImageState2.ToImage2(modifier: Modifier) {
    when (val src = src) {
        is Resource2 -> Icon(
            painter = painterResource(C.getResource(src.location)),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = color.toColor2()
        )

        is Local2 -> Image(
            painter = painterResource(C.getResource(src.location)),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale.toContentScale2(),
            colorFilter = ColorFilter.tint(color.toColor2())
        )

        is Url2 -> AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(src.location)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            onError = {
                /* TODO to use proper SDUI logging */
            },
            contentScale = contentScale.toContentScale2(),
            colorFilter = ColorFilter.tint(color.toColor2())
        )
    }
}

@Composable
fun State2.addOnClick(modifier: Modifier, enabled: Boolean = true) = (this as? OnClickModifier2)?.let {
    modifier.clickable(enabled = enabled, role = Role.Button) {
        it.onClickTrigger.trigger()
    }
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
    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
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

@Composable
fun State2.toModifier2() = Modifier
    .then(size.toModifier2())
    .then(
        when (this) {
            is PaddingModifier2<*> -> this.padding.toModifier2()
            is InnerPaddingModifier2<*> -> this.padding.toModifier2()
            else -> Modifier
        }
    )
