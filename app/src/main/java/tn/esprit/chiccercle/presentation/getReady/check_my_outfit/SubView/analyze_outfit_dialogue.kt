package tn.esprit.chiccercle.presentation.getReady.check_my_outfit.SubView
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.presentation.getReady.check_my_outfit.analyzeOutfit
import java.io.File

@Composable
fun OutfitAnalysisDialog(
    imageUri: Uri,
    onDismissRequest: () -> Unit,
    context: Context,
   // capturedImageBitmap: Bitmap?,
) {
    var capturedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var analysisResult by remember { mutableStateOf("Analyzing your outfit...") }
    var isAnalyzing by remember { mutableStateOf(true) }

    // Analyser l'image une fois que l'URI est disponible
    LaunchedEffect(imageUri) {
        analysisResult = analyzeOutfit(context, imageUri) ?: "Analyse échouée."
        isAnalyzing = false // Changer l'état une fois l'analyse terminée
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Outfit Analysis", fontWeight = FontWeight.Bold,textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif,
            style = androidx.compose.material.MaterialTheme.typography.h5,color = Color(0xFF4F3E3E)) },
        // Utilisation de Modifier pour ajuster la largeur et la hauteur selon le contenu
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp) // Ajouter un peu d'espace autour du contenu
            ) {
                // Afficher l'image capturée
                capturedImageBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )
                } ?: run {
                    Text("Image not found.", style = MaterialTheme.typography.bodyMedium)
                }

                // Afficher le texte d'analyse de Gemini
                if (isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp)) // Indicateur de chargement pendant l'analyse
                    Text("Analyzing your outfit...", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5D5C56))
                } else {
                    Text(analysisResult, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5D5C56))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C))
            ) {
                Text("Close", color = Color.White) // Texte blanc pour le contraste
            }
        }
    )
}
