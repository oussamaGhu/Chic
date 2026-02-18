package tn.esprit.chiccercle.presentation.closet

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content

import java.io.FileNotFoundException
import java.io.IOException

suspend fun analyzeWithGemini(context: Context, imageUri: Uri): Map<String, List<String>>? {
    // Convertir URI en Bitmap
    Log.d("SelectedImageUri", "URI: $imageUri")
    val imageBitmap = uriToBitmap(context, imageUri) ?: run {
        Log.e("Gemini", "Failed to convert URI to Bitmap.")
        return null
    }
    // Initialiser le modèle génératif (API Key à sécuriser)
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyDNpHbHsVvPa0tQR261dy1vEAHWjCo4KCw" // Remplacez par une méthode sécurisée pour gérer l'API Key
    )
    val inputContent = content {
        image(imageBitmap)
        text(
            "Analyze this image and return a JSON object with the following keys: " +
                    "'types' ( e.g shirt, dress,pants,skirt,jacket,shoes,accessory.) juste choose from the suggestions i give it to you, " +
                    "'occasions' (e.g., casual, formal), " +
                    "'moods' describe the general emotional state the clothing might represent or be appropriate for ,you should necessarily choose from the sugestions :( happy, sad, excited), " +
                    "'colors' (e.g., red, blue, black), " +
                    "'weather' – specify the weather condition that this clothing is suited for, such as a light shirt for sunny weather or a coat for cold weather choose from the sugestions :( sunny, rainy, snowy,cloudy))." +
                    "Do not include any additional text or explanations."
        )

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
    Log.d("GeminiRawResponse", responseText)



    val clothingInfo = mapOf(
        "types" to extractInfoFromText(responseText, "types"),
        "occasions" to extractInfoFromText(responseText, "occasions"),
        "moods" to extractInfoFromText(responseText, "moods"),
        "colors" to extractInfoFromText(responseText, "colors"),
        "weather" to extractInfoFromText(responseText, "weather")
    )


    return clothingInfo
}


fun extractInfoFromText(responseText: String, key: String): List<String> {
    // Améliorer la correspondance pour des valeurs qui peuvent être suivies de virgules ou de fins de ligne
    val pattern = """(?i)"?$key"?\s*:\s*\[(.*?)\]""".toRegex()
    return pattern.find(responseText)?.groups?.get(1)?.value?.split(",")?.map { it.trim().removeSurrounding("\"") } ?: listOf("Unknown")
}



// Fonction pour convertir l'URI en Bitmap
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







