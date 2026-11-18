package com.kxbuild.kmpgallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF6B4EE6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE6DEFF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = Color(0xFF0B6FB8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD2E4FF),
    onSecondaryContainer = Color(0xFF001C38),
    tertiary = Color(0xFFB4407A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E7),
    onTertiaryContainer = Color(0xFF3D0024),
    background = Color(0xFFFCF8FF),
    surface = Color(0xFFFCF8FF),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF6F2FC),
    surfaceContainer = Color(0xFFF0ECF6),
    surfaceContainerHigh = Color(0xFFEAE6F0),
    surfaceContainerHighest = Color(0xFFE5E1EA),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFCABEFF),
    onPrimary = Color(0xFF3A1C95),
    primaryContainer = Color(0xFF5235CC),
    onPrimaryContainer = Color(0xFFE6DEFF),
    secondary = Color(0xFFA0CAFF),
    onSecondary = Color(0xFF00315B),
    secondaryContainer = Color(0xFF004881),
    onSecondaryContainer = Color(0xFFD2E4FF),
    tertiary = Color(0xFFFFAFD2),
    onTertiary = Color(0xFF5E113D),
    tertiaryContainer = Color(0xFF912962),
    onTertiaryContainer = Color(0xFFFFD8E7),
    background = Color(0xFF131218),
    surface = Color(0xFF131218),
    surfaceContainerLowest = Color(0xFF0E0D13),
    surfaceContainerLow = Color(0xFF1C1B21),
    surfaceContainer = Color(0xFF201F25),
    surfaceContainerHigh = Color(0xFF2A2930),
    surfaceContainerHighest = Color(0xFF35343B),
)

@Composable
fun GalleryTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors) {
        Surface(color = MaterialTheme.colorScheme.background, content = content)
    }
}

/** Colors used by the swatches, tiles and charts of several demos. */
val DemoPalette = listOf(
    Color(0xFF6B4EE6), Color(0xFF0B6FB8), Color(0xFF00897B), Color(0xFF43A047),
    Color(0xFFF9A825), Color(0xFFEF6C00), Color(0xFFE53935), Color(0xFFB4407A),
)

/** Scrollable column with standard padding that most demos use as their root. */
@Composable
fun DemoColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        content = content,
    )
}

/** A titled block inside a demo. */
@Composable
fun DemoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        content()
    }
}

/** Secondary explanatory text under a demo section. */
@Composable
fun Caption(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

fun Modifier.clickableRow(onClick: () -> Unit): Modifier = this.clickable(onClick = onClick)

/** yyyy-MM-dd for a UTC epoch-millisecond value, as the Material date pickers report it. */
fun formatDate(epochMillis: Long): String {
    // Days-to-civil conversion (Howard Hinnant), so the demo needs no date-time library.
    val z = epochMillis.floorDiv(86_400_000L) + 719_468
    val era = z.floorDiv(146_097L)
    val doe = z - era * 146_097
    val yoe = (doe - doe / 1_460 + doe / 36_524 - doe / 146_096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val day = doy - (153 * mp + 2) / 5 + 1
    val month = if (mp < 10) mp + 3 else mp - 9
    val year = yoe + era * 400 + if (month <= 2) 1 else 0
    return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
}
