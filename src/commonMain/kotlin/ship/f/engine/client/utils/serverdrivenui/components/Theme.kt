package ship.f.engine.client.utils.serverdrivenui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import ship.f.engine.client.utils.serverdrivenui.generated.resources.Res
import ship.f.engine.client.utils.serverdrivenui.generated.resources.inter_variable

/**
 * Theme colors for SDUIDrivenUI for light theme are quite basic for now, they'll be expanded as needed.
 */
val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE64A19),
    onPrimary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF414651),

    background = Color(0xFFF9F9F9),
    onBackground = Color(0xFF1C1C1E),

    surface = Color(0xFFFFFF),
    onSurface = Color(0xFF1C1C1E),

    surfaceVariant = Color(0xFFF1F1F1),
    onSurfaceVariant = Color(0xFF6E6E6E), // Label color

    outline = Color(0xFFBDBDBD), // Border around inputs
)

/**
 * Theme colors for SDUIDrivenUI dark theme are quite basic for now, they'll be expanded as needed.
 */
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE64A19),
    onPrimary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFFCECFD2),

    background = Color(0xFF1E1E1E),
    onBackground = Color(0xFFF1F1F1),

    surface = Color(0xFF121212),
    onSurface = Color(0xFFF1F1F1),

    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0), // Label/hint color

    outline = Color(0xFF444444), // Border color
)

/**
 * Font is currently hardcoded to use Inter Variable font family.
 * In the future I will probably include more fonts and make it possible to override this from SDUIDrivenUI.
 * In such a scenario there will need to be a build script phase downloads the required fonts so they aren't all loaded.
 * That is quite the luxury.
 */
val InterVariable: FontFamily
    @Composable get() = FontFamily(
    Font(resource = Res.font.inter_variable, weight = FontWeight.Normal),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Medium),
    Font(resource = Res.font.inter_variable, weight = FontWeight.SemiBold),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Bold),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Light),
    Font(resource = Res.font.inter_variable, weight = FontWeight.ExtraBold),
)

/**
 * Typography is currently hardcoded to only use the values from material3.
 * This is preferable over making our own Typography class for now even though we are using the untitledUI library.
 * Fortunately, Untitled does use the material3 design kit for typography, though it is quite a bit more advanced.
 * A Temporary solution until we make an expanded UntitledTypography class is we can just roughly match designs for now.
 * Creating a full system only makes sense once we have the full set of designs to work with.
 */
val typography: Typography
    @Composable
    get() = Typography().let {
        val f = InterVariable
        it.copy(
            displayLarge = it.displayLarge.copy(fontFamily = f),
            displayMedium = it.displayMedium.copy(fontFamily = f),
            displaySmall = it.displaySmall.copy(fontFamily = f),
            headlineLarge = it.headlineLarge.copy(fontFamily = f),
            headlineMedium = it.headlineMedium.copy(fontFamily = f),
            headlineSmall = it.headlineSmall.copy(fontFamily = f),
            titleLarge = it.titleLarge.copy(fontFamily = f),
            titleMedium = it.titleMedium.copy(fontFamily = f),
            titleSmall = it.titleSmall.copy(fontFamily = f),
            bodyLarge = it.bodyLarge.copy(fontFamily = f),
            bodyMedium = it.bodyMedium.copy(fontFamily = f),
            bodySmall = it.bodySmall.copy(fontFamily = f),
            labelLarge = it.labelLarge.copy(fontFamily = f),
            labelMedium = it.labelMedium.copy(fontFamily = f),
            labelSmall = it.labelSmall.copy(fontFamily = f)
        )
    }

/**
 * Default Themes used for SDUIDriven applications
 * That being said, it's quite simple to have each app override this theme with their own values if needed
 */
@Composable
fun ServerDrivenUITheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    // We are not currently interested in shapes as they are not used as often as colors
    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = typography,
    )
}


