package tn.esprit.chiccercle.presentation.getReady


import CameraView
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import tn.esprit.chiccercle.R
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import org.bson.types.ObjectId
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.AssembleRequest
import tn.esprit.chiccercle.model.Clothes
import tn.esprit.chiccercle.model.Options
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.presentation.closet.ClothesViewModel
import java.util.Calendar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import tn.esprit.chiccercle.presentation.getReady.check_my_outfit.SubView.OutfitAnalysisDialog
import tn.esprit.chiccercle.presentation.getReady.check_my_outfit.analyzeOutfit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetReadyScreen() {

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val backgroundColor = Color(0xFFF4F2E9)
    val buttonColor = Color(0xFFAA8F5A)
    val textColor = Color(0xFF875A5A)
    val grayColor = Color(0xFF999999)
    val grayyColor = Color(0xFF8D6E52)
    val checkColor = Color(0x76736363) // Alpha de 76 et couleur gris foncé (63, 63, 63)
    val CustomGray = Color(0xFFE4E4E5)
    var selectedColor by remember { mutableStateOf(Color(0xFF8D6E52)) }
    val occasionOptions = listOf("formal", "casual", "party", "sports", "beach", "wedding")
    val moodOptions = listOf("happy", "sad", "excited")
    val weatherOptions = listOf("cloudy", "rainy", "sunny", "snowy")
    // val typeOptions = listOf("T-shirt", "pants", "dress", "skirt", "jacket", "shoes", "accessory")
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf(0) }
    var selectedOccasion by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var selectedWeather by remember { mutableStateOf("") }
    var isTShirtEnabled by remember { mutableStateOf(false) }
    var selectedMenuItem by remember { mutableStateOf(0) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }
    val clothesViewModel: ClothesViewModel = viewModel()
    val getReadyViewModel: GetReadyViewModel = viewModel()
    val assembleList by getReadyViewModel.AssembleList.observeAsState(initial = emptyList())
    val clothesList by clothesViewModel.clothesList.observeAsState(initial = emptyList())
    val viewModel: GetReadyViewModel = viewModel()
    val onDelete: (String) -> Unit = { assembleId ->
        viewModel.deleteAssemble(assembleId)

    }
    Scaffold(
        // containerColor = Color(0xFFF4F2E9),
        topBar = {              // Couleur d'arrière-plan de la Scaffold
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFCFB)
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Get Ready",
                            style = MaterialTheme.typography.titleLarge,
                            color = textColor
                        )
                    }
                })
        },


        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF7F4EB))

            ) {
                // Ligne avec les onglets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TabButton(
                        text = "New Assemble",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )

                    TabButton(
                        text = "My Assembles",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
                var showCamera by remember { mutableStateOf(false) }
                var showDialog by remember { mutableStateOf(false) }
                var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
                if (showCamera) {

                    CameraView(
                        context = LocalContext.current,
                        onImageCaptured = { uri ->
                            showCamera = false
                            capturedImageUri = uri // enregistrer l'URI de l'image
                            showDialog = true // Afficher la boîte de dialogue de l'analyse
                        },
                        onClose = { showCamera = false },
                        onError = { exception ->
                            showCamera = false // Fermer la caméra en cas d'erreur
                            // Gérer l'erreur ici, par exemple, montrer un Toast
                        }
                    )

                } else {

                    when (selectedTab) {
                        0 -> NewAssembleScreen(
                            viewModel = viewModel,
                            occasionOptions = occasionOptions,
                            moodOptions = moodOptions,
                            weatherOptions = weatherOptions,
                            onOccasionSelected = { selectedOccasion = it },
                            onMoodSelected = { selectedMood = it },
                            // onTypeSelected = { selectedType = it },
                            selectedMenuItem = selectedMenuItem,
                            onMenuItemSelected = { selectedMenuItem = it },
                            isTShirtEnabled = isTShirtEnabled, // Passer l'état ici
                            onTShirtEnabledChange = { isTShirtEnabled = it },
                            onWeatherSelected = { selectedWeather = it },
                            onClick = {
                                showCamera = true // Définit l'état pour afficher la caméra
                            }
                        )

                    1 -> MyAssemblesScreen(AssembleList = assembleList, clothesList = clothesList, onDelete)

                }}

          //////////////////////////////////////////////////////////////////////////////
                if (showDialog && capturedImageUri != null) {
                    Log.d("DEBUG", "Showing dialog with image URI: $capturedImageUri")
                    OutfitAnalysisDialog(
                        imageUri = capturedImageUri!!,
                        onDismissRequest = {
                            showDialog = false

                        },
                        context = LocalContext.current
                    )
                }
                // Contenu avec colonne gauche et contenu principal
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp)
                ) {

                    // Partie gauche : Menu avec icônes
                    LeftSideMenu(
                        selectedItem = selectedItem,
                        onItemSelected = { index -> selectedItem = index }

                    )

                    // Partie droite : Contenu en fonction de l'élément sélectionné
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF999999))

                    ) {
                        // Contenu dynamique basé sur l'élément sélectionné
                        when (selectedCategory) {
                            0 -> Text("Casquette sélectionnée")
                            1 -> Text("Haut sélectionné")
                            2 -> Text("Pantalon sélectionné")
                            3 -> Text("Chaussures sélectionnées")
                        }
                    }
                }
            }
        }

    )
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFFDFCFB) else Color(0xFFF3F2F0)
    val textColor = if (isSelected) Color.Black else Color(0xFF999999)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
        modifier = Modifier
            // Largeur spécifique
            .height(40.dp)

    ) {
        Text(text = text, color = textColor)
    }
}

////////////////////////////////////////////////////
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NewAssembleScreen(
    onClick: () -> Unit,
    occasionOptions: List<String>,
    weatherOptions: List<String>,
    moodOptions: List<String>,
    onOccasionSelected: (String) -> Unit,
    onMoodSelected: (String) -> Unit,
    //  onTypeSelected: (String) -> Unit,
    selectedMenuItem: Int,
    onMenuItemSelected: (Int) -> Unit,
    isTShirtEnabled: Boolean,
    onTShirtEnabledChange: (Boolean) -> Unit,
    onWeatherSelected: (String) -> Unit,
    viewModel: GetReadyViewModel
) {
    var showCamera by remember { mutableStateOf(false) }
    val cameraPermissionState: PermissionState =
        rememberPermissionState(android.Manifest.permission.CAMERA)
    val clothesList = remember { mutableStateOf<List<Clothes>>(emptyList()) }
    val context = LocalContext.current
    var selectedWeather by remember { mutableStateOf(listOf<String>()) }
    var selectedOccasion by remember { mutableStateOf(listOf<String>()) }
    var selectedMood by remember { mutableStateOf(listOf<String>()) }
    var selectedColor by remember { mutableStateOf(listOf<String>()) }
    var selectedType by remember { mutableStateOf(listOf<String>()) }
    var isAccessoryEnabled by remember { mutableStateOf(false) }
    var accessoryColor by remember { mutableStateOf("") }
    var selectedItem by remember { mutableStateOf(0) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var isTShirtEnabled by remember { mutableStateOf(false) }
    var weather by remember { mutableStateOf("weather") }
    var occasions by remember { mutableStateOf("occasions") }
    var moods by remember { mutableStateOf("moods") }
    var capturedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }


    val clothingItems = remember {
        mutableStateListOf(
            Options(false, "shirt", null),
            Options(false, "pants", null),
            Options(false, "shoes", null),
            Options(false, "accessory", null)
        )

    }
    ///  var optionsList by remember { mutableStateOf(clothingItems) }
    Row(
        modifier = Modifier
            .background(Color(0xFFF7F4EB)),
        // Fond global
    ) {
        Box(
            modifier = Modifier
                .weight(2f) // Poids pour le menu
                .fillMaxHeight()
        ) {
            // Menu à gauche
            LeftSideMenu(
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index // Met à jour l'état global lorsque l'utilisateur clique
                }
            )
        }
        // Contenu principal (new assemble)///////////////////////////////////////////////
        Column(
            modifier = Modifier
                .width(162.dp) // Largeur du menu
                .height(600.dp)
                .weight(3f)
                .padding(end = 16.dp),
            // Définir la largeur width
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            // Dropdown menus
            DropdownMenu(
                label = "Occasion",
                options = occasionOptions,
                selectedOption = selectedOccasion,
                onOptionSelected = { selected ->
                    // Trim the selected options
                    selectedOccasion = selected.map { it.trim() }
                }

            )
            Spacer(modifier = Modifier.height(17.dp))
            DropdownMenu(
                label = "Mood",
                options = moodOptions,
                selectedOption = selectedMood,
                onOptionSelected = { selected ->
                    // Trim the selected options
                    selectedMood = selected.map { it.trim() }
                }


            )
            Spacer(modifier = Modifier.height(17.dp))
            DropdownMenu(
                label = "Weather",
                options = weatherOptions,
                selectedOption = selectedWeather,
                onOptionSelected = { selected ->
                    // Trim the selected options
                    selectedWeather = selected.map { it.trim() }
                }

            )

            Spacer(modifier = Modifier.height(17.dp))
            Column(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .height(275.dp)
                    .width(155.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TShirtSection(
                    isEnabled = isTShirtEnabled,
                    onSwitchChange = { newValue ->
                        isTShirtEnabled = newValue
                    },
                    textColor = Color(0xFF8D6E52),
                    selectedItem = selectedItem,
                    onSelectedItemChange = { selectedItem = it },
                    onOptionsChange = { updatedOptions ->
                        // Impression pour débogage
                        println("Options mises à jour : $updatedOptions")

                        // Mettre à jour l'élément spécifique au lieu de vider toute la liste
                        val updatedOptionsList = clothingItems.toMutableList()
                        updatedOptionsList[selectedItem] = updatedOptions[selectedItem]

                        clothingItems.clear() // Effacez l'ancienne liste
                        clothingItems.addAll(updatedOptionsList) // Ajoute les nouvelles options
                    },
                    optionsList = clothingItems,
                )

                // Spacer(modifier = Modifier.height(5.dp))

                // Image Box
                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .width(155.dp)
                        .background(Color(0xFFE0D8C3))
                        .padding(bottom = 9.dp)
                        .clickable { /* Handle image click */ },
                    contentAlignment = Alignment.Center

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Image",
                        tint = Color(0xFF8D6E52)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
//////////////////////////////////////////////////////////////////////
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        if (cameraPermissionState.status.isGranted) {
                            // Si la permission est accordée, ouvrir la caméra
                            onClick()
                        } else {
                            // Si la permission n'est pas accordée, demander la permission
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }

            ) {
                Text(
                    text = "Check My outfit",
                    color = Color(0xFF767363),
                    fontSize = 15.sp // Taille de police mise à 14
                )
                Spacer(modifier = Modifier.width(8.dp)) // Un petit espace entre le texte et l'icône
                Image(
                    painter = painterResource(id = R.drawable.camera), // Utilisez ici votre icône camera
                    contentDescription = "Camera Icon", // Description pour l'accessibilité
                    modifier = Modifier.size(27.dp), // Taille de l'icône, ajustez selon vos préférences
                    colorFilter = ColorFilter.tint(Color(0xFF767363))
                )
            }
            clothingItems.forEach { item ->
                // Appel à la fonction de sélection de vêtement
            }
            Button(
                onClick = {
                    isDialogVisible = true

                    // Générer un nouvel ID
                    val newId = ObjectId()
                    val userId = SessionManager().getUserId().toString()

                    // Créez un nouveau Clothes basé sur les sélections utilisateur


                    // Mettez à jour clothesFilter avec les nouvelles valeurs
                    viewModel.clothesFilter = viewModel.clothesFilter.copy(
                        id = newId.toString(),
                        user = userId,
                        weather = selectedWeather,
                        occasions = selectedOccasion,
                        moods = selectedMood,
                        colors = selectedColor // Assurez-vous que selectedColor est défini correctement
                    )
                    Log.d("ClothesFilter", "Updated ClothesFilter: $viewModel.clothesFilter")
                    // Filtrez les options activées
                    val optionsList = clothingItems.filter { it.enable }.map {
                        Options(
                            enable = it.enable,
                            type = it.type,
                            color = if (it.color == "Default Color") null else it.color // Valeur par défaut
                        )
                    }

                    // Créez une requête Assemble
                    val assembleRequest = AssembleRequest(
                        options = optionsList,
                        clothe = viewModel.clothesFilter // Utilisez clothesFilter mis à jour
                    )

                    // Log de la requête pour débogage
                    Log.d("AssembleRequest", "Request: $assembleRequest")

                    // Appel de la méthode pour générer l'ensemble
                    viewModel.assembleGemini(assembleRequest) { success, errorMessage ->
                        if (success) {
                            // Traitement des vêtements générés
                            Toast.makeText(
                                context,
                                "Outfit généré avec succès !",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Gestion des erreurs
                            Toast.makeText(
                                context,
                                "Erreur lors de la génération de l'ensemble : $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAA8F5C), // Couleur de fond
                    contentColor = Color.White // Couleur du texte
                ),

                modifier = Modifier
                    .width(185.dp)
            ) {
                Text("Generate") // Texte du bouton
            }

        }

    }
    if (isDialogVisible) {
        AssembleDialog(
            //  clothesList = clothesList.value,
            onDismissRequest = { isDialogVisible = false }
        )
    }
}
    private const val CAMERA_REQUEST_CODE = 1002
    private fun openCamera(context: Context) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            (context as? Activity)?.startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }


@Composable
fun LeftSideMenu(selectedItem: Int, onItemSelected: (Int) -> Unit) {

    val items = listOf(
        R.drawable.img_3,
        R.drawable.img,
        R.drawable.img_1,
        R.drawable.img_4
    )

    Column(
        modifier = Modifier
            .width(162.dp)
            .height(600.dp)
            .padding(start = 15.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEEEDEF)),
/////////////////////////////////////////////////////////////////////////////
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, iconRes ->
            Box(
                modifier = Modifier
                    .size(150.dp, 145.dp) // Taille ajustée des icônes
                    .background(
                        if (selectedItem == index) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (selectedItem == index) Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onItemSelected(index) }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Menu item $index",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(60.dp) // Taille augmentée pour correspondre à l'interface cible
                )
            }
            Spacer(modifier = Modifier.height(12.dp)) // Espacement ajusté
        }
    }
}

@Composable
fun DropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: List<String>,
    onOptionSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    var expanded by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            value = selectedOption.joinToString(", "),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .width(155.dp)
                .height(53.dp) //////////////////////////////////////////////////
                .background(Color(0xFFFDFCFB))

                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        val updatedOptions = if (selectedOption.contains(option)) {
                            selectedOption - option // Supprimer si déjà sélectionné
                        } else {
                            selectedOption + option // Ajouter si non sélectionné
                        }
                        onOptionSelected(updatedOptions) // Passer la liste mise à jour
                        expanded = false
                    }
                )
            }
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////
@Composable
fun TShirtSection(
    isEnabled: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    textColor: Color,
    optionsList: SnapshotStateList<Options>,
    selectedItem: Int,
    onOptionsChange: (List<Options>) -> Unit,
    onSelectedItemChange: (Int) -> Unit
) {
    // val item = optionsList[selectedItem]

    // Texte correspondant à chaque icône
    val itemTexts = listOf("accessory", "shirt", "pants", "shoes")
    val colorOptions = listOf("red", "blue", "green", "yellow", "black", "white")
    val grayColor = Color(0xFF999999)
    // État pour gérer l'affichage du menu déroulant et la couleur sélectionnée
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf("") } // Par défaut "Color"
    val selectedOption = optionsList[selectedItem]
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        // Titre de la section
        Text(
            text = itemTexts[selectedItem], // Ne changera que lorsque `onSelectedItemChange` est appelé
            fontSize = 19.sp,
            color = textColor,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )

        // Ligne pour le switch "Enabled"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text("Enabled", fontSize = 16.sp, color = grayColor)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = selectedOption.enable,
                onCheckedChange = { newValue ->
                    // Copie les options actuelles
                    val updatedOptions = optionsList.toMutableList()

                    if (newValue) {
                        // Si activé, définir le type comme une chaîne et s'assurer qu'il est correctement sélectionné
                        updatedOptions[selectedItem] = updatedOptions[selectedItem].copy(
                            enable = true,
                            type = itemTexts[selectedItem] // Définit le type en tant que chaîne
                        )

                        // Optionnel : Si une couleur n'est pas définie, demander une sélection
                        if (updatedOptions[selectedItem].color == null) {
                            isDropdownExpanded = true // Affiche le menu déroulant de couleur
                        }
                    } else {
                        // Si désactivé, effacer le type
                        updatedOptions[selectedItem] = updatedOptions[selectedItem].copy(
                            enable = false,
                            type = "", // Réinitialise le type à une chaîne vide
                            color = null // Réinitialise également la couleur pour un comportement propre
                        )
                    }

                    // Appelle la fonction de mise à jour avec les nouvelles options
                    onOptionsChange(updatedOptions)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 10.dp)
        ) {
            // Texte cliquable pour ouvrir le menu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { isDropdownExpanded = true }
                    .padding(8.dp) // Espacement
            ) {
                Text(
                    text = selectedColor, // Affiche la couleur sélectionnée
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = Color.Gray
                )
            }

            // Menu déroulant
            androidx.compose.material3.DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                colorOptions.forEach { color ->
                    androidx.compose.material3.DropdownMenuItem(
                        onClick = {
                            selectedColor = color // Met à jour uniquement la couleur sélectionnée
                            isDropdownExpanded = false
                            val updatedOptions = optionsList.toMutableList()
                            updatedOptions[selectedItem] =
                                updatedOptions[selectedItem].copy(
                                    color = if (color == "Default Color") null else color
                                )
                            onOptionsChange(updatedOptions)
                            println("Couleur sélectionnée : $selectedColor")
                        },
                        text = { Text(color) }
                    )
                }
            }
        }
    }
}

@Composable
fun AssembleDialog(

    onDismissRequest: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel: GetReadyViewModel = viewModel()
    val assembleName = remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.observeAsState(false)
    val showError = remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val generatedList by viewModel.generatedList.observeAsState(emptyList())
    LaunchedEffect(generatedList) {
        if (generatedList.isEmpty()) {
            showError.value = true
        } else {
            showError.value = false
        }
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 500.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                // verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = "Generated Assemble",
                    fontSize = 20.sp,
                    color = Color(0xFF5E4E4E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        //.padding(bottom = 6.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Name Input Field
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF888888)),
                    placeholder = { Text(text = "Enter Assemble Name", color = Color(0xFF888888)) },
                    label = { Text("Assemble Name", color = Color(0xFF888888)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F1EE),
                        unfocusedContainerColor = Color(0xFFF2F1EE),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Afficher l'indicateur de chargement ou le message d'erreur
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    showError.value -> {
                        Text(
                            text = "Échec de la récupération de la réponse Gemini",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    else -> {
                        // Liste horizontale des vêtements
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            items(generatedList) { Clothes ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(150.dp)
                                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                                ) {

                                    val imageId = Clothes.images // Récupération de l'ID d'image
                                    if (imageId!!.isNotEmpty()) { // Vérifiez si l'ID n'est pas vide
                                        val fullImageUrl =
                                            MyRetrofit.getBaseUrl() + "file/$imageId" // Chemin correct vers l'image
                                        AsyncImage(
                                            model = fullImageUrl,
                                            contentDescription = "Clothing Image",
                                            modifier = Modifier
                                                .width(130.dp)
                                                .padding(vertical = 6.dp)
                                                .height(178.dp)
                                                .background(
                                                    Color.Gray.copy(alpha = 0.1f),
                                                    RoundedCornerShape(6.dp)
                                                ),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        // Optionnel : affichage d'un espace réservé ou d'un message si aucune image n'est disponible
                                        Text(
                                            text = "Image non disponible",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = Clothes.types?.joinToString(" ") ?: "Aucun type",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF5E4E4E)
                                    )
                                    Text(
                                        text = "----",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Spacer(modifier = Modifier.weight(1f))
                // Boutons pour les actions Annuler et Enregistrer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // Alignement vertical au centre
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.width(120.dp)
                    ) {

                        Text("Cancel", color = Color(0xFFAA8F5C))
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                Log.d("couretine", "le couretine est lanché")
                                // Vérification du nom
                                if (name.isEmpty()) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Entrer le nom de l'assemblage!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    return@launch  // Quitte la coroutine si le nom est vide
                                }
                                val clothesIds = generatedList.map { it.id } ?: listOf()
                                val calendar = Calendar.getInstance()
                                calendar.set(2024, Calendar.JANUARY, 1)  // 1er janvier 2024
                                val defaultDate = calendar.time
                                // Vérification que des vêtements ont été sélectionnés
                                if (generatedList.isEmpty()) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Aucun vêtement à enregistrer",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    return@launch  // Quitte la coroutine si aucun vêtement n'est sélectionné
                                }

                                // Appel de la méthode viewModel pour enregistrer l'assemblage
                                viewModel.addAssemble(
                                    name = name,
                                    clothesIds = clothesIds,
                                    images = "",
                                    date = defaultDate,
                                    price = 0.0,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Assemblage enregistré avec succès!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    },
                                    onError = { errorMessage ->
                                        Toast.makeText(
                                            context,
                                            "Erreur: $errorMessage",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )

                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFAA8F5C)
                        ),
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}
