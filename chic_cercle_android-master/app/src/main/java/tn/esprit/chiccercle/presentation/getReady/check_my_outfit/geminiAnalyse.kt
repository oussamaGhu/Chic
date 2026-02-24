package tn.esprit.chiccercle.presentation.getReady.check_my_outfit
import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import java.io.FileNotFoundException
import java.io.IOException

suspend fun analyzeOutfit(context: Context, imageUri: Uri): String? {
    // Convertir URI en Bitmap
    val imageBitmap = uriToBitmap(context, imageUri) ?: run {
        Log.e("Gemini", "Failed to convert URI to Bitmap.")
        return null
    }

    // Initialiser le modèle génératif
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyDNpHbHsVvPa0tQR261dy1vEAHWjCo4KCw" // Remplace par une méthode sécurisée pour gérer l'API Key
    )

    // Préparer le contenu pour l'analyse
    val inputContent = content {
        image(imageBitmap)
        text("Voici une photo de ma tenue. Peux-tu me donner une évaluation ? Est-elle harmonieuse ? si non donne moi des recommandations pour l'améliorer,pas une longue paragraphe juste maximum deux phrase ou une seule phrase ")
    }

    // Obtenir la réponse de Gemini
    val response = try {
        generativeModel.generateContent(inputContent)
    } catch (e: Exception) {
        Log.e("Gemini", "Error generating content: ${e.message}")
        return null
    }

    val responseText = response.text ?: ""
    if (responseText.isEmpty()) {
        Log.e("Gemini", "Empty response received from Gemini.")
        return null
    }

    Log.d("GeminiResponse", responseText)
    return responseText
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    try {
        // Vérification du type MIME (optionnel pour debug)
        val type = context.contentResolver.getType(uri)
        Log.d("URIType", "Type MIME de l'URI: $type")

        // Ouverture via FileDescriptor
        context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            return BitmapFactory.decodeFileDescriptor(fileDescriptor)
        }
    } catch (e: FileNotFoundException) {
        Log.e("Gemini", "File not found: ${e.message}")
    } catch (e: IOException) {
        Log.e("Gemini", "Error opening file: ${e.message}")
    }
    return null
}

