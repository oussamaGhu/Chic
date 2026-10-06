import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.core.content.FileProvider

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun CameraView(context: Context, onImageCaptured: (Uri) -> Unit, onClose: () -> Unit, onError: (Exception) -> Unit) {
    // Création d'un fichier temporaire pour l'image
    val tempImageFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
    )


    val tempImageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempImageFile
    )

    // Launcher pour la prise de photo
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onImageCaptured(tempImageUri) // Appel de la fonction de retour avec l'URI de l'image capturée
        } else {
            onError(Exception("Échec de la capture de l'image"))
        }
    }

    // Appel du launcher de la caméra
    LaunchedEffect(Unit) {
        cameraLauncher.launch(tempImageUri)
    }
}



















