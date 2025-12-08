package tn.esprit.chiccercle.presentation.closet
import android.content.Context

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.model.Clothes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bson.types.ObjectId
import android.graphics.Bitmap
import coil.compose.AsyncImage
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager

@Preview(showBackground = true)
@Composable
fun ClothesScreenPreview() {
    ShowGeneratedResponseDialog(response = emptyMap(), bitmap = null, onDismissRequest = {}, selectedImageUri = null, context = LocalContext.current, imageUri = Uri.EMPTY, onClothesAdded = {})
}
// Main Screen for displaying clothes
@Composable
fun ClothesScreen(viewModel: ClothesViewModel, userId: String) {
    val clothesList = viewModel.clothesList.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    var showDialogGeneratedResponse by remember { mutableStateOf(false) }
    var showDialogImagePicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val showDialogEditResponse = remember { mutableStateOf(false) }
    val selectedClothes = remember { mutableStateOf<Clothes?>(null) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var geminiResponse = remember { mutableStateOf<Map<String, List<String>>?>(null) }
    val clothingIdToDelete = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    LaunchedEffect(userId) {
        viewModel.getClothes(userId)
    }
    val getImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    Log.e("Bitmap Error", "Failed to decode bitmap: ${e.message}")
                }

            }
        }
    )

    // Lancer l'analyse de l'image si elle a été sélectionnée
    selectedImageUri?.let { imageUri ->
        LaunchedEffect(imageUri) {
            val response = analyzeWithGemini(context, imageUri)

            if (response != null) {
                geminiResponse.value = response // Si la réponse est non nulle, affectez-la
                showDialogGeneratedResponse = true
            } else {
                geminiResponse.value = emptyMap() // Fournir une carte vide si la réponse est nulle
                Log.e("Gemini", "No valid response received.")
            }
        }
    }



    selectedImageUri?.let { imageUri ->
        LaunchedEffect(imageUri) {
            val response = analyzeWithGemini(context, imageUri)

            if (response != null) {
                geminiResponse.value = response // Si la réponse est non nulle, affectez-la
                showDialogGeneratedResponse = true
            } else {
                geminiResponse.value = emptyMap() // Fournir une carte vide si la réponse est nulle
                Log.e("Gemini", "No valid response received.")
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Winter",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = Color(0xFF5D5C56),
            modifier = Modifier.background(Color.White).fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F2E9)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add New Clothes ",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                color = Color(0xFF5D5C56),
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = { showDialogImagePicker = true }, // Toggle dialog visibility
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5A)), ///////
                modifier = Modifier.width(83.dp).height(39.dp)
            ) {
                Text("Add", color = Color.White)
            }
        }
        if (showDialogImagePicker) {

            Dialog(onDismissRequest = { showDialogImagePicker = false }) {
                Box(
                    modifier = Modifier
                        .size(width = 345.dp, height = 180.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )  // Style of the dialog
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Dialog title
                        Text(
                            text = "Add a Clothes from your device",
                            fontSize = 20.sp,
                            color = Color(0xFF766363),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 25.dp) //16
                        )

                        // Button to open the image picker
                        Button(
                            onClick = {
                                getImage.launch("image/*")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9A8A8A)),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .height(50.dp)
                                .width(200.dp)
                        ) {
                            Text("Choose Image")
                        }


                    }
                }
            }
        }
        // Show the generated response dialog when showDialogGeneratedResponse is true
        if (showDialogGeneratedResponse && geminiResponse.value?.isNotEmpty() == true) {
            val response = geminiResponse.value
            //val responseMap = response?.mapValues { it.value.firstOrNull() ?: "Unknown" }
            val clothesList = mutableListOf<Clothes>()
            ShowGeneratedResponseDialog(
                response = response ?: emptyMap(), // Assurez-vous que cette valeur est du bon type
                bitmap = bitmap,
                selectedImageUri = selectedImageUri,
                onDismissRequest = { showDialogGeneratedResponse = false },
                context = context, // Passer le contexte ici
                imageUri = selectedImageUri ?: Uri.EMPTY,
                onClothesAdded = { newClothes ->
                    clothesList.add(newClothes) // Ajouter le nouveau vêtement à la liste
                }

            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F2E9)) // Set background color to F4F2E9
                .padding(16.dp) // Optional padding for spacing around content
        ) {
        // Display Clothes List
            if (clothesList != null) {
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))  // Show loading indicator when isLoading is true

                    else -> {
                        LazyColumn(
                            modifier = Modifier

                        ) {

                            itemsIndexed(clothesList) { index, clothes ->
                                ClothesItem(
                                    clothes = clothes,
                                    onDelete = { clothingId ->
                                        clothingIdToDelete.value = clothingId
                                        showDeleteDialog.value = true
                                    },
                                    onEdit = { clothing ->
                                        selectedClothes.value = clothing
                                        showDialogEditResponse.value = true
                                    }

                                )
                                if (index < clothesList.size - 1) {
                                    Divider(
                                        color = Color.Gray, // Use your desired color
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }


            // Error Message
            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (showDeleteDialog.value && clothingIdToDelete.value != null) {
                DeleteConfirmationDialog(
                    onConfirm = {
                        viewModel.deleteClothes(clothingIdToDelete.value ?: "")
                        showDeleteDialog.value = false
                    },
                    onDismiss = { showDeleteDialog.value = false }
                )
            }

            // Edit Dialog
            if (showDialogEditResponse.value && selectedClothes.value != null) {
                EditClothesDialog(
                    showDialog = showDialogEditResponse,
                    clothes = selectedClothes.value!!,
                    onDismiss = { showDialogEditResponse.value = false },
                    onSave = { updatedClothes ->
                        viewModel.updateClothes(updatedClothes.id, updatedClothes)
                        showDialogEditResponse.value = false
                    }
                )
            }
        }
    }
}
@Composable
fun ClothesItem(clothes: Clothes, onDelete: (String) -> Unit, onEdit: (Clothes) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background (Color(0xFFF4F2E9))
            .padding(16.dp)
    ) {
        clothes.images?.let { imageId ->
            val fullimages = MyRetrofit.getBaseUrl()+"file/$imageId"

            AsyncImage(
                model = fullimages,
                contentDescription = "Clothing Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Text(
                text = clothes.types?.joinToString(", ") ?: "Aucun type",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color(0xFF875A5A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Occasion: ${clothes.occasions.joinToString(", ")}", color = Color(0xFF875A5A)
            )
            Text(text = "Weather: ${clothes.weather.joinToString(", ")}", color = Color(0xFF875A5A)
            )
            Text(text = "Colors: ${clothes.colors.joinToString(", ")}", color = Color(0xFF875A5A)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = { onEdit(clothes) }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF875A5A)

                )
            }

            IconButton(onClick = { clothes.id?.let { onDelete(it) } }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete"
                    ,tint = Color(0xFF875A5A))
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
@Composable
fun EditClothesDialog(
    showDialog: MutableState<Boolean>,
    clothes: Clothes,
    onDismiss: () -> Unit,
    onSave: (Clothes) -> Unit

) {

    val viewModel: ClothesViewModel = viewModel()

   val Name = remember { mutableStateOf(clothes.name ?: "") }
    val descriptions = remember { mutableStateOf(clothes.description ?: "") }
    val sizee = remember { mutableStateOf(clothes.size ?: "") }
    val pricee = remember { mutableStateOf(clothes.price.toString()) }
        // Initialisez d'autres valeurs si nécessaire


    if (showDialog.value) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .size(width = 345.dp, height = 570.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Edit Clothes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF875A5A),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    val selectedTypes = remember {
                        mutableStateListOf(
                            *clothes.types?.toTypedArray() ?: arrayOf()
                        ) // Utilise une liste vide si `types` est null
                    }
                    val selectedOccasions =
                        remember { mutableStateListOf(*clothes.occasions.toTypedArray()) }
                    val selectedWeather =
                        remember { mutableStateListOf(*clothes.weather.toTypedArray()) }
                    val selectedColors =
                        remember { mutableStateListOf(*clothes.colors.toTypedArray()) }
                    TextField(
                        value = Name.value,
                        onValueChange = { Name.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 7.dp)
                        ,
                        shape = RoundedCornerShape(10.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                        placeholder = { Text(text = "Enter Assemble Name", color = Color(0xFF888888)) },
                        label = { Text("Clothes Name", color = Color(0xFF888888)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF2F1EE),
                            unfocusedContainerColor = Color(0xFFF2F1EE),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    if (SessionManager().getRole() == "seller"){ ////////////////////////////////////////////////////////////////////////////////////////
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {


                            TextField(
                                value = descriptions.value,
                                onValueChange = { descriptions.value = it },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f) // Reduce width for the description
                                    .height(150.dp) // Increase height for multi-line input
                                    .padding(bottom = 7.dp),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                placeholder = { Text(text = "Enter Description", color = Color(0xFF888888)) },
                                label = { Text("Description", color = Color(0xFF888888)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF2F1EE),
                                    unfocusedContainerColor = Color(0xFFF2F1EE),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            // Row with size and name TextFields
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Size TextField
                                TextField(
                                    value = sizee.value,
                                    onValueChange = {sizee.value = it },
                                    modifier = Modifier
                                        .weight(1f) // Adjust width equally with sibling
                                        .height(56.dp), // Standard TextField height
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                    placeholder = { Text(text = "Enter Size", color = Color(0xFF888888)) },
                                    label = { Text("Size", color = Color(0xFF888888)) },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF2F1EE),
                                        unfocusedContainerColor = Color(0xFFF2F1EE),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )

                                // Name TextField
                                TextField(
                                    value = pricee.value,
                                    onValueChange = {pricee.value = it },
                                    modifier = Modifier
                                        .weight(1f) // Adjust width equally with sibling
                                        .height(56.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                    placeholder = { Text(text = "Enter Price", color = Color(0xFF888888)) },
                                    label = { Text("Price", color = Color(0xFF888888)) },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF2F1EE), unfocusedContainerColor = Color(0xFFF2F1EE),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }else{ MultiSelectDropdownForEdit(
                        label = "Types",
                        options =  listOf("shirt", "pants", "dress", "skirt", "jacket", "shoes", "accessory"),
                        selectedOptions = selectedTypes
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiSelectDropdownForEdit(
                        label = "Occasions",
                        options = listOf("formal", "casual", "party", "sports", "beach", "wedding"),
                        selectedOptions = selectedOccasions
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    MultiSelectDropdownForEdit(
                        label = "Weather",
                        options =listOf("cloudy", "rainy", "sunny", "snowy"),
                        selectedOptions = selectedWeather
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    MultiSelectDropdownForEdit(
                        label = "Colors",
                        options = listOf("Red", "blue", "Green", "black", "white", "yellow"),
                        selectedOptions = selectedColors
                    )}
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { onDismiss() }, modifier = Modifier.width(120.dp)) {
                            Text("Cancel", color = Color(0xFFAA8F5C))
                        }
                        Button(
                            onClick = {
                                val updatedClothes = clothes.copy(
                                    types = selectedTypes.toList(),
                                    occasions = selectedOccasions.toList(),
                                    weather = selectedWeather.toList(),
                                    colors = selectedColors.toList(),
                                    name = Name.value,
                                    description = descriptions.value,
                                    size = sizee.value,
                                    price =pricee.value.toDoubleOrNull()
                                )
                                onSave(updatedClothes)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C)),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowGeneratedResponseDialog(
    response: Map<String,List<String>>,
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    // onSelectionChange: (List<String>) -> Unit,
    selectedImageUri: Uri?,
    context: Context
    , imageUri: Uri,
    onClothesAdded: (Clothes) -> Unit,

) {
    val coroutineScope = rememberCoroutineScope()
    var clothingInfo by remember { mutableStateOf<Map<String, List<String>>?>(null) }
    LaunchedEffect(imageUri) {
        clothingInfo = analyzeWithGemini(context, imageUri)
    }
    val context = LocalContext.current
    val viewModel: ClothesViewModel = viewModel()
    // var selectedUser: String? by remember { mutableStateOf(null) }

    // var selectedUser by remember { mutableStateOf<List<String>>(emptyList()) }

    var   selectedTypes = clothingInfo?.get("types") ?: emptyList()
    var selectedMoods = clothingInfo?.get("moods") ?: emptyList()
    var  selectedOccasions = clothingInfo?.get("occasions") ?: emptyList()
    var selectedWeather = clothingInfo?.get("weather") ?: emptyList()
    var selectedColors = clothingInfo?.get("colors") ?: emptyList()
    val typeOptions = listOf("shirt", "pants", "dress", "skirt", "jacket", "shoes", "accessory")
    val moodOptions = listOf("happy", "sad", "excited")
    val occasionOptions = listOf("formal", "casual", "party", "sports", "beach", "wedding")
    val weatherOptions = listOf("cloudy", "rainy", "sunny", "snowy")
     val clothesList = remember { mutableStateListOf<Clothes>() }
    Dialog({onDismissRequest ()})
        {
            Box(
                modifier = Modifier
                    .size(width = 345.dp, height = 646.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Generated Response",
                        fontSize = 20.sp,
                        color = Color(0xFF5E4E4E),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Placeholder ou image sélectionnée
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center // Centrer le contenu
                    ) {
                        bitmap?.let {////////////////////////////
                            Image(
                                bitmap = it.asImageBitmap(), ///////////////////////////
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: Icon(
                            imageVector = Icons.Default.Image, // Icône par défaut si l'image n'est pas sélectionnée
                            contentDescription = "No Image",
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = viewModel.clothesName.value,
                        onValueChange = { viewModel.clothesName.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 7.dp)
                          ,
                        shape = RoundedCornerShape(10.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                        placeholder = { Text(text = "Enter Assemble Name", color = Color(0xFF888888)) },
                        label = { Text("Clothes Name", color = Color(0xFF888888)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF2F1EE),
                            unfocusedContainerColor = Color(0xFFF2F1EE),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    if (SessionManager().getRole() == "seller"){ ////////////////////////////////////////////////////////////////////////////////////////
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {

                            // Description TextField
                            TextField(
                                value = viewModel.description.value,
                                onValueChange = { viewModel.description.value = it },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f) // Reduce width for the description
                                    .height(150.dp) // Increase height for multi-line input
                                    .padding(bottom = 7.dp),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                placeholder = { Text(text = "Enter Description", color = Color(0xFF888888)) },
                                label = { Text("Description", color = Color(0xFF888888)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF2F1EE),
                                    unfocusedContainerColor = Color(0xFFF2F1EE),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            // Row with size and name TextFields
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Size TextField
                                TextField(
                                    value = viewModel.size.value,
                                    onValueChange = { viewModel.size.value = it },
                                    modifier = Modifier
                                        .weight(1f) // Adjust width equally with sibling
                                        .height(56.dp), // Standard TextField height
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                    placeholder = { Text(text = "Enter Size", color = Color(0xFF888888)) },
                                    label = { Text("Size", color = Color(0xFF888888)) },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF2F1EE),
                                        unfocusedContainerColor = Color(0xFFF2F1EE),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )

                                // Name TextField
                                TextField(
                                    value = viewModel.price.value,
                                    onValueChange = { viewModel.price.value = it },
                                    modifier = Modifier
                                        .weight(1f) // Adjust width equally with sibling
                                        .height(56.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                                    placeholder = { Text(text = "Enter Price", color = Color(0xFF888888)) },
                                    label = { Text("Price", color = Color(0xFF888888)) },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF2F1EE),
                                        unfocusedContainerColor = Color(0xFFF2F1EE),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    } else {


                        // Dropdown menus
                        MultiSelectDropdown(
                            label = "Type",
                            options = typeOptions,
                            selectedOptions = selectedTypes,
                            preSelected = clothingInfo?.get("types") ?: emptyList()
                        )
                        { selectedTypes = it }


                        MultiSelectDropdown(
                            label = "Mood",
                            options = moodOptions,
                            selectedOptions = selectedMoods,
                            preSelected = clothingInfo?.get("moods") ?: emptyList()
                        )
                        { selectedMoods = it }
                        MultiSelectDropdown(
                            label = "Occasion",
                            options = occasionOptions,
                            selectedOptions = selectedOccasions,
                            preSelected = clothingInfo?.get("occasions") ?: emptyList()
                        )
                        { selectedOccasions = it }

                        MultiSelectDropdown(
                            label = "Weather",
                            options = weatherOptions,
                            selectedOptions = selectedWeather,
                            preSelected = clothingInfo?.get("weather") ?: emptyList()
                        )
                        { selectedWeather = it }

                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Cancel", color = Color(0xFFAA8F5C))
                        }
                        Button(
                            onClick = {
                                println("Envoi des données au back-end :")
                                println("types : $selectedTypes")
                                println("moods : $selectedMoods")
                                println("occasions : $selectedOccasions")
                                println("weathers : $selectedWeather")

                                coroutineScope.launch {
                                    try {
                                       // val newId = UUID.randomUUID().toString() // Crée un nouvel ID unique

                                       val newId = ObjectId().toString()
                                        // Étape 1 : Sauvegarder l dans un fichier
                                        val file = viewModel.saveImageUriToFile(
                                            context,
                                            selectedImageUri,
                                            "clothes_image"
                                        )
                                        if (file == null) {
                                            println("Failed to save image to file")
                                            return@launch // Si l'image échoue à être sauvegardée, quitter la coroutine
                                        }


                                        viewModel.uploadFile(file) { fileIdResult ->
                                            if (fileIdResult.startsWith("Upload failed")) {
                                                println(fileIdResult) // Afficher une erreur si l'upload échoue
                                                Toast.makeText(
                                                    context,
                                                    fileIdResult,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                // Étape 3 : Ajouter les vêtements avec les données sélectionnées
                                                viewModel.addClothes(
                                                    //  selectedUser= selectedUser!!,
                                                    selectedImage = fileIdResult, // ID du fichier retourné
                                                    selectedOccasions = selectedOccasions,
                                                    selectedMoods = selectedMoods,
                                                    selectedWeather = selectedWeather,
                                                    selectedTypes = selectedTypes,
                                                    selectedColors = selectedColors,
                                                    price = if (SessionManager().getRole() == "seller") {
                                                        viewModel.price.value.toDoubleOrNull() // Convertir en Double
                                                    } else {
                                                        null // Passer null si ce n'est pas un vendeur
                                                    },
                                                    size = if (SessionManager().getRole() == "seller") {
                                                        viewModel.size.value // Utiliser la valeur de size
                                                    } else {
                                                        null // Passer null si ce n'est pas un vendeur
                                                    },
                                                    description = if (SessionManager().getRole() == "seller") {
                                                        viewModel.description.value // Utiliser la valeur de description
                                                    } else {
                                                        null // Passer null si ce n'est pas un vendeur
                                                    },
                                                    name = if (SessionManager().getRole() == "seller") {
                                                        viewModel.clothesName.value // Utiliser la valeur de clothesName
                                                    } else {
                                                        null // Passer null si ce n'est pas un vendeur
                                                    }
                                                )

                                                val newClothes = Clothes(
                                                    id = newId,
                                                    images = fileIdResult,
                                                    types = selectedTypes,
                                                    moods = selectedMoods,
                                                    occasions = selectedOccasions,
                                                    weather = selectedWeather,
                                                    colors = selectedColors,
                                                    user = "",
                                                    price = if (SessionManager().getRole() == "seller") {
                                                        viewModel.price.value.toDoubleOrNull() ?: 0.0 // Utiliser le prix de l'interface si le rôle est vendeur
                                                    } else {
                                                        null
                                                    },
                                                    name = if (SessionManager().getRole() == "seller") {
                                                        viewModel.clothesName.value // Utiliser le nom de l'interface si le rôle est vendeur
                                                    } else {
                                                        ""
                                                    },
                                                    description = if (SessionManager().getRole() == "seller") {
                                                        viewModel.description.value
                                                    } else {
                                                        "" // Passer null si ce n'est pas un vendeur
                                                    },
                                                    size = if (SessionManager().getRole() == "seller") {
                                                        viewModel.size.value // Utiliser la taille de l'interface si le rôle est vendeur
                                                    } else {
                                                        null // Passer null si ce n'est pas un vendeur
                                                    }
                                                )
                                                onClothesAdded(newClothes)
                                                Toast.makeText(context, "Vêtements ajoutés avec succès!", Toast.LENGTH_SHORT).show()
                                                onDismissRequest() //
                                            }
                                        }
                                    } catch (e: Exception) {
                                        println("Error in save process: ${e.message}")
                                        Toast.makeText(
                                            context,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show() // Afficher l'erreur générale
                                    }
                                }
                            },
                            //onDismissRequest = { showDialogGeneratedResponse.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAA8F5C)),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text("Save", color = Color.White)
                        }
// Étape 3 : Ajouter les vêtements
                    }
                }
            }
        }
}

@Composable
fun MultiSelectDropdown(label: String,
                        selectedOptions : List<String>,
                        options: List<String>,
                        preSelected: List<String>,
                        onSelectionChange: (List<String>) -> Unit) {
    var expanded by remember { mutableStateOf(false) } // Pour contrôler l'état du menu
    val selectedOptions = remember { mutableStateListOf<String>() }
    // Initialisation de `selectedOptions` si `preSelected` n'est pas nul
    LaunchedEffect(preSelected) {
        if (preSelected != null ) {
            preSelected.forEach { item ->
                if (!selectedOptions.contains(item)) {
                    selectedOptions.add(item)
                }
            }

        }
    }// Options sélectionnées

    Column {
        Spacer(modifier = Modifier.height(8.dp))

        // Zone cliquable pour ouvrir le menu déroulant
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded } // Gérer l'état `expanded`
                .padding(16.dp)
        ) {
            Text(
                if (selectedOptions.isEmpty()) "Select $label" else selectedOptions.joinToString(", "),
                fontSize = 14.sp,
                color = if (selectedOptions.isEmpty()) Color.Gray else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menu déroulant
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = null // Géré par `onClick`
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option)
                        }
                    },
                    onClick = {
                        val updatedSelection = if (selectedOptions.contains(option)) {
                            selectedOptions - option // Retirer l'option si elle est déjà sélectionnée
                        } else {
                            selectedOptions + option // Ajouter l'option si elle n'est pas sélectionnée
                        }
                        selectedOptions.clear()
                        selectedOptions.addAll(updatedSelection)
                        onSelectionChange(updatedSelection) // Mise à jour de l'état dans Compose
                    })}}}}
@Composable
fun MultiSelectDropdownForEdit( /////////////////////////////////////////////////////////////////////////////////////
    label: String,
    options: List<String>,
    selectedOptions: MutableList<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        // Label
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp,
            color = Color(0xFF766363)
        )
        Spacer(modifier = Modifier.height(8.dp))
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (selectedOptions.isEmpty()) "Select $label" else selectedOptions.joinToString(", "),
                color = if (selectedOptions.isEmpty()) Color.Gray else Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(300.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = null // Géré par `onClick`
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option)
                        }
                    },
                    onClick = {
                        val updatedSelection = if (selectedOptions.contains(option)) {
                            selectedOptions - option // Retirer l'option si elle est déjà sélectionnée
                        } else {
                            selectedOptions + option // Ajouter l'option si elle n'est pas sélectionnée
                        }
                        selectedOptions.clear()
                        selectedOptions.addAll(updatedSelection)
                       // onSelectionChange(updatedSelection) // Mise à jour de l'état dans Compose
                    }
                )
            }}
        }
    }
}
