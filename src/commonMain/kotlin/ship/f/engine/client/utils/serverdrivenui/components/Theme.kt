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

val Orange = Color(0xFFE64A19)
val LinkedInBlue = Color(0xFF0077B5)
val DarkSurface = Color(0xFF121212)
val DarkBackground = Color(0xFF1E1E1E)
val DarkOutline = Color(0xFF444444)
val LightText = Color(0xFFF1F1F1)
val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE64A19),
    onPrimary = Color.White,
    onSecondaryContainer = Color.Black,

    background = Color(0xFFF9F9F9),
    onBackground = Color(0xFF1C1C1E),

    surface = Color.White,
    onSurface = Color(0xFF1C1C1E),

    surfaceVariant = Color(0xFFF1F1F1),
    onSurfaceVariant = Color(0xFF6E6E6E), // Label color

    outline = Color(0xFFBDBDBD), // Border around inputs
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE64A19),
    onPrimary = Color.Black,

    background = Color(0xFF1E1E1E),
    onBackground = Color(0xFFF1F1F1),

    surface = Color(0xFF121212),
    onSurface = Color(0xFFF1F1F1),

    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB0B0B0), // Label/hint color

    outline = Color(0xFF444444), // Border color
)

val InterVariable: FontFamily
    @Composable get() = FontFamily(
    Font(resource = Res.font.inter_variable, weight = FontWeight.Normal),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Medium),
    Font(resource = Res.font.inter_variable, weight = FontWeight.SemiBold),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Bold),
    Font(resource = Res.font.inter_variable, weight = FontWeight.Light),
    Font(resource = Res.font.inter_variable, weight = FontWeight.ExtraBold),
)

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

@Composable
fun ServerDrivenUITheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = typography,
    )
}


