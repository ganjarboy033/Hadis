package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemePalette(val label: String, val primaryColorHex: Long) {
    EMERALD_GREEN("Zamrud Hijau Islami", 0xFF0F5132),
    MIDNIGHT_GOLD("Emas Malam Elegan", 0xFFE5C07B),
    ROYAL_TEAL("Biru Laut Teal", 0xFF00796B),
    WARM_SAND("Pasir Gurun Hangat", 0xFF8D6E63)
}

fun getThemeColorScheme(palette: AppThemePalette, isDark: Boolean): ColorScheme {
    return when (palette) {
        AppThemePalette.EMERALD_GREEN -> {
            if (isDark) {
                darkColorScheme(
                    primary = Color(0xFF66BB6A),
                    onPrimary = Color(0xFF00390E),
                    primaryContainer = Color(0xFF1B5E20),
                    onPrimaryContainer = Color(0xFFA5D6A7),
                    secondary = EmeraldGoldAccent,
                    onSecondary = Color(0xFF332000),
                    background = Color(0xFF121814),
                    surface = Color(0xFF1A221C),
                    surfaceVariant = Color(0xFF263228),
                    onSurface = Color(0xFFE8F5E9),
                    onBackground = Color(0xFFE8F5E9)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF1B5E20),
                    onPrimary = Color.White,
                    primaryContainer = Color(0xFFC8E6C9),
                    onPrimaryContainer = Color(0xFF002204),
                    secondary = Color(0xFFC29015),
                    onSecondary = Color.White,
                    background = Color(0xFFF7FAF7),
                    surface = Color.White,
                    surfaceVariant = Color(0xFFE8EFE9),
                    onSurface = Color(0xFF1B241C),
                    onBackground = Color(0xFF1B241C)
                )
            }
        }
        AppThemePalette.MIDNIGHT_GOLD -> {
            darkColorScheme(
                primary = MidnightGoldPrimary,
                onPrimary = Color(0xFF2A1C00),
                primaryContainer = MidnightGoldContainer,
                onPrimaryContainer = Color(0xFFFFE082),
                secondary = Color(0xFF81D4FA),
                background = MidnightDarkBackground,
                surface = MidnightDarkSurface,
                surfaceVariant = Color(0xFF21262D),
                onSurface = Color(0xFFF0F6FC),
                onBackground = Color(0xFFF0F6FC)
            )
        }
        AppThemePalette.ROYAL_TEAL -> {
            if (isDark) {
                darkColorScheme(
                    primary = Color(0xFF4DB6AC),
                    onPrimary = Color(0xFF003731),
                    primaryContainer = Color(0xFF004D40),
                    background = Color(0xFF0E1A18),
                    surface = Color(0xFF162523),
                    onSurface = Color(0xFFE0F2F1)
                )
            } else {
                lightColorScheme(
                    primary = TealPrimary,
                    onPrimary = Color.White,
                    primaryContainer = TealPrimaryContainer,
                    background = Color(0xFFF2F9F8),
                    surface = Color.White,
                    onSurface = Color(0xFF112826)
                )
            }
        }
        AppThemePalette.WARM_SAND -> {
            if (isDark) {
                darkColorScheme(
                    primary = Color(0xFFD7CCC8),
                    onPrimary = Color(0xFF3E2723),
                    primaryContainer = Color(0xFF4E342E),
                    background = Color(0xFF1C1816),
                    surface = Color(0xFF26211E),
                    onSurface = Color(0xFFEFEBE9)
                )
            } else {
                lightColorScheme(
                    primary = SandPrimary,
                    onPrimary = Color.White,
                    primaryContainer = SandPrimaryContainer,
                    background = SandBackground,
                    surface = Color.White,
                    onSurface = Color(0xFF2E241F)
                )
            }
        }
    }
}

@Composable
fun HadithArbainTheme(
    palette: AppThemePalette = AppThemePalette.EMERALD_GREEN,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = getThemeColorScheme(palette, darkTheme)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
