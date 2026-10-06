package tn.esprit.chiccercle.ui.theme

import androidx.compose.ui.graphics.Color

val primaryText = Color("#5D5C56")
val secondaryText = Color("#999891")

// Background Colors
val primaryBackground = Color("#F4F2E9")
val secondaryBackground = Color("#FDFCFB")

val textField = Color("#ECE3E3")

// Button Colors
val primaryButton = Color("#AA8F5C")
val secondaryButton = Color("#DDD4BF")

fun Color(hex: String): Color {
    return Color(android.graphics.Color.parseColor(hex))
}