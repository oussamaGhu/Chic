package tn.esprit.chiccercle.ui.commen



import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tn.esprit.chiccercle.R

// Define custom font and color constants
object FontStyles {

    // Georgia font family (you'll need to add your custom font to the `res/font` folder)
    private val georgiaFontFamily = FontFamily.Serif

    // Title 1: Bold, 32, primary text color
    val title1 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = AppColors.primaryText
    )

    // Subtitle 1: Regular, 17, primary text color
    val subtitle1 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        color = AppColors.primaryText
    )

    // Title 2: Bold, 20, primary text color
    val title2 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = AppColors.primaryText
    )

    // Subtitle 2: Regular, 14, secondary text color
    val subtitle2 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = AppColors.secondaryText
    )

    // Title 3: Bold, 16, primary text color
    val title3 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = AppColors.primaryText
    )

    // Title 4: Bold, 12, primary text color
    val title4 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = AppColors.primaryText
    )

    // Subtitle 3: Regular, 12, secondary text color
    val subtitle3 = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = AppColors.secondaryText
    )

    // Button text: Bold, 18, white
    val textButton = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color.White
    )

    // Subtext button: Regular, 14, primary button color
    val subtextButton = TextStyle(
        fontFamily = georgiaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = AppColors.primaryButton
    )
}

// App-specific color definitions (replace these with your actual colors)
object AppColors {
    val primaryText = Color(0xFF5D5C56) // Replace with your primary text color
    val secondaryText = Color(0xFF907171) // Replace with your secondary text color
    val primaryButton = Color(0xFFAA8F5C) // Replace with your primary button color
}

// Extension function to apply custom text styles
fun Modifier.textStyle(textStyle: TextStyle): Modifier {
    return this.then(Modifier.alpha(1f)) // Customize if needed
}

// Example usage:
// Text("Your Text Here", style = FontStyles.title1)