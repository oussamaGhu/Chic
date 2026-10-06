package tn.esprit.chiccercle.presentation.getReady
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.Assemble
import tn.esprit.chiccercle.model.Clothes
import tn.esprit.chiccercle.presentation.closet.ClothesViewModel
import  tn.esprit.chiccercle.R
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.presentation.closet.DeleteConfirmationDialog
@Composable
fun MyAssemblesScreen(AssembleList: List<Assemble>, clothesList: List<Clothes>, onDelete: (String) -> Unit) {
    val clothesViewModel: ClothesViewModel = viewModel()
    val getReadyViewModel: GetReadyViewModel = viewModel()
    val userId = SessionManager().getUserId().toString()
    val assembleIdToDelete = remember { mutableStateOf<String?>(null) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        getReadyViewModel.getAssemble(userId)
        clothesViewModel.getClothes(userId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(AssembleList) { assemble ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Titre de l'assemblage
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = assemble.name,
                        fontWeight = FontWeight.Bold, // Texte en gras
                        fontSize = 19.sp, // Taille du texte
                        color = Color(0xFF875A5A), // Couleur personnalisée
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Bouton de suppression
                    IconButton(onClick = {
                        assembleIdToDelete.value = assemble.id
                        showDeleteDialog.value = true
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF875A5A),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Boîte de dialogue de confirmation
                if (showDeleteDialog.value && assembleIdToDelete.value != null) {
                    DeleteConfirmationDialog(
                        onConfirm = {
                            getReadyViewModel.deleteAssemble(assembleIdToDelete.value ?: "")
                            showDeleteDialog.value = false
                        },
                        onDismiss = { showDeleteDialog.value = false }
                    )
                }

                // Images dans LazyRow
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(assemble.clothes) { imageId ->
                        val clothingItem = clothesList.find { it.id == imageId }
                        if (clothingItem != null) {
                            val fullImageUrl =
                                MyRetrofit.getBaseUrl() + "file/${clothingItem.images}"

                            AsyncImage(
                                model = fullImageUrl,
                                contentDescription = "Clothing Image",
                                modifier = Modifier
                                    .size(90.dp) // Hauteur et largeur de l'image
                                    .background(
                                        Color.White, // Fond blanc
                                        RoundedCornerShape(10.dp) // Bordure arrondie
                                    )
                                    .padding(8.dp), // Espacement interne
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Placeholder
                            Image(
                                painter = painterResource(id = R.drawable.placeholder),
                                contentDescription = "Placeholder Image",
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Ligne de séparation
                Divider(
                    color= Color(0xFFD4D4D4),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(345.dp, 150.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to delete this item?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel", color = Color(0xFFAA8F5C))
                    }
                    Button(
                        onClick = {
                            onConfirm() // Proceed with deletion
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
}