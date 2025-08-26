package ship.f.engine.client.utils.serverdrivenui2.ext

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource
import ship.f.engine.client.utils.serverdrivenui2.CommonClient2
import ship.f.engine.shared.utils.serverdrivenui2.client.Client2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.ColorScheme2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.models.Shapes2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ColorSchemeModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.ShapesModifier2
import ship.f.engine.shared.utils.serverdrivenui2.config.state.modifiers.TypographyModifier2
import ship.f.engine.shared.utils.serverdrivenui2.state.State2

@Composable
fun ColorScheme2.toMaterialColorScheme() = MaterialTheme.colorScheme.copy(
    primary = Color(primary),
    onPrimary = Color(onPrimary),
    primaryContainer = Color(primaryContainer),
    onPrimaryContainer = Color(onPrimaryContainer),
    inversePrimary = Color(inversePrimary),
    secondary = Color(secondary),
    onSecondary = Color(onSecondary),
    secondaryContainer = Color(secondaryContainer),
    onSecondaryContainer = Color(onSecondaryContainer),
    tertiary = Color(tertiary),
    onTertiary = Color(onTertiary),
    tertiaryContainer = Color(tertiaryContainer),
    onTertiaryContainer = Color(onTertiaryContainer),
    background = Color(background),
    onBackground = Color(onBackground),
    surface = Color(surface),
    onSurface = Color(onSurface),
    surfaceVariant = Color(surfaceVariant),
    onSurfaceVariant = Color(onSurfaceVariant),
    surfaceTint = Color(surfaceTint),
    inverseSurface = Color(inverseSurface),
    inverseOnSurface = Color(inverseOnSurface),
    error = Color(error),
    onError = Color(onError),
    errorContainer = Color(errorContainer),
    onErrorContainer = Color(onErrorContainer),
    outline = Color(outline),
    outlineVariant = Color(outlineVariant),
    scrim = Color(scrim),
    surfaceBright = Color(surfaceBright),
    surfaceDim = Color(surfaceDim),
    surfaceContainer = Color(surfaceContainer),
    surfaceContainerHigh = Color(surfaceContainerHigh),
    surfaceContainerHighest = Color(surfaceContainerHighest),
    surfaceContainerLow = Color(surfaceContainerLow),
    surfaceContainerLowest = Color(surfaceContainerLowest),
)

@Composable
fun State2.toMaterialColorScheme(
    isSystemInDarkTheme: Boolean = false,
    client: Client2,
) = (client.get<State2>(this.id) as? ColorSchemeModifier2<*>)?.run {
    if (isSystemInDarkTheme) darkColorScheme?.toMaterialColorScheme() else lightColorScheme?.toMaterialColorScheme()
} ?: MaterialTheme.colorScheme

fun Shapes2.CornerBasedShape2.toCornerBasedShape() = RoundedCornerShape(
    topStart = topStart,
    topEnd = topEnd,
    bottomStart = bottomStart,
    bottomEnd = bottomEnd,
)

@Composable
fun State2.toMaterialShapes(
    client: Client2,
) = (client.get<State2>(this.id) as? ShapesModifier2<*>)?.run {
    Shapes(
        extraSmall = shapes.extraSmall.toCornerBasedShape(),
        small = shapes.small.toCornerBasedShape(),
        medium = shapes.medium.toCornerBasedShape(),
        large = shapes.large.toCornerBasedShape(),
        extraLarge = shapes.extraLarge.toCornerBasedShape(),
    )
} ?: MaterialTheme.shapes

@Composable
fun String.toFont(
    client: CommonClient2,
) = client.getResource<FontResource>(this).let {
    FontFamily(
        Font(resource = it, weight = FontWeight.Normal),
        Font(resource = it, weight = FontWeight.Medium),
        Font(resource = it, weight = FontWeight.SemiBold),
        Font(resource = it, weight = FontWeight.Bold),
        Font(resource = it, weight = FontWeight.Light),
        Font(resource = it, weight = FontWeight.ExtraBold),
    )
}

@Composable
fun State2.toMaterialTypography(
    client: CommonClient2,
) = (client.get<State2>(this.id) as? TypographyModifier2<*>)?.run {
    val fontFamily = font.toFont(client)
    Typography().run {
        copy(
            displayLarge = displayLarge.copy(fontFamily = fontFamily),
            displayMedium = displayMedium.copy(fontFamily = fontFamily),
            displaySmall = displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = titleLarge.copy(fontFamily = fontFamily),
            titleMedium = titleMedium.copy(fontFamily = fontFamily),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily),
            labelLarge = labelLarge.copy(fontFamily = fontFamily),
            labelMedium = labelMedium.copy(fontFamily = fontFamily),
            labelSmall = labelSmall.copy(fontFamily = fontFamily),
        )
    }
} ?: MaterialTheme.typography
