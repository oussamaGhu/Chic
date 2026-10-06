package tn.esprit.chiccercle.presentation.market
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.Assemble
import tn.esprit.chiccercle.model.Clothes
import tn.esprit.chiccercle.model.Request
import tn.esprit.chiccercle.model.User
import tn.esprit.chiccercle.presentation.closet.ClothesViewModel

@OptIn(ExperimentalFoundationApi::class) // Enable LazyVerticalGrid
@Composable
fun MarketScreen(viewModel: MarketViewModel = viewModel()) {
    val clothes by viewModel.clothes // Fetch clothes from ViewModel
    val assembles by viewModel.assembles // Fetch assembles from ViewModel

    val sessionManager = SessionManager()
    val user = sessionManager.getUser() // Assuming you've stored user details in the session
   // var currentRequest: Request? by remember { mutableStateOf(null) }
    var filteredClothes by remember { mutableStateOf(clothes) }
    var selectedClothing by remember { mutableStateOf<Clothes?>(null) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) } // Search input
    var selectedCategories by remember { mutableStateOf(mutableSetOf<String>()) } // Selected categories
    var showDetailsDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // Fetch user role from session and clothes from backend
        val role = sessionManager.getRole() ?: "client"
        viewModel.setUserRole(role)
        viewModel.fetchClothes() // Fetch clothes from backend
        viewModel.fetchAssembles() // Fetch assembles
    }

    // Use userRole for conditional rendering (e.g., show admin content if user is admin)

    // Dynamically filter clothes based on search text and selected categories
    LaunchedEffect(clothes, searchText, selectedCategories) {
        filteredClothes = clothes.filter { clothing ->
            val matchesCategory =
            selectedCategories.isEmpty() || (clothing.types?.any { it in selectedCategories } ?: false)
            val matchesSearch = searchText.text.isEmpty() || clothing.name?.contains(
                searchText.text,
                ignoreCase = true
            ) == true
            matchesCategory && matchesSearch
        }
    }
    // Wrapping the layout with Surface to apply the background color
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F3EC)
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp) // Proper padding around the whole LazyColumn
        ) {
            // Title Section (Sticky Header)
            stickyHeader {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Market",
                    color = Color(0xFF5D5C56),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F3EC))
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
            // Search Bar Section
            item {
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            color = Color(0xFF3C3C43).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    textStyle = TextStyle(
                        color = Color(0xFF3C3C43),
                        fontSize = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color(0xFF3C3C43).copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (searchText.text.isEmpty()) {
                                Text(
                                    text = "Search",
                                    color = Color(0xFF3C3C43).copy(alpha = 0.6f),
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

            }////////////////////////////////////////////////////////////////////////////////////////
            // Categories Section
            item {
                Text(
                    text = "Categories",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF5D5C56)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        listOf(
                            Pair(R.drawable.shirt, "T-shirt"),
                            Pair(R.drawable.pants, "pants"),
                            Pair(R.drawable.shoes, "shoes"),
                            Pair(R.drawable.dresss, "dress"),
                            Pair(R.drawable.vector1, "jacket"),
                            Pair(R.drawable.glasses, "accessory")
                        )
                    ) { item ->
                        val isSelected = item.second in selectedCategories

                        CategoryItem(
                            imageRes = if (item.first is Int) item.first else MyRetrofit.getBaseUrl()+"file/${item.first}",
                            label = item.second,
                            isSelected = isSelected,
                            onClick = {
                                // Add or remove the category from selectedCategories
                                if (isSelected) {
                                    selectedCategories.remove(item.second)
                                } else {
                                    selectedCategories.add(item.second)
                                }

                                // Update the filteredClothes based on selected categories
                                filteredClothes = clothes.filter { clothing ->
                                    // If no categories are selected, show all clothes
                                    if (selectedCategories.isEmpty()) return@filter true
                                    // Otherwise, match items belonging to any selected category
                                    selectedCategories.isEmpty() || (clothing.types?.any { it in selectedCategories } ?: false)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // Trends Section
            item {
                Text(
                    text = "Trends",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF5D5C56)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Display all assembles in the trends section
                    items(assembles) { assemble ->
                        TrendCard(
                            title = assemble.name,
                            price = assemble.price.toString(),
                            imageRes = assemble.image ?: "default_image_path.jpg",
                            user = user // Pass the user object
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
            // Clothes Section (Filtered by categories and search)
            item {
                Text(
                    text = "Items",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF5D5C56)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 600.dp) // Prevent infinite height error
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredClothes) { clothing ->
                        ClothesCard(
                            clothing = clothing,
                            onClick = {
                                // Met à jour l'état principal
                                selectedClothing = clothing
                                showDetailsDialog = true
                            })
                    }
                }
            }
        }
    }

    if (showDetailsDialog && selectedClothing != null ) {
        DetailsDialog(
            clothing = selectedClothing!!,
            onDismissRequest = { showDetailsDialog = false },

        )
    }
}
@Composable
fun TrendCard(
    title: String,
    price: String,
    imageRes: String?, // Image URL for assemble
    user: User // Passing the User object
) {
    // Construct the full URL for the image
    val imageUrl = MyRetrofit.getBaseUrl()+"file/${imageRes}"
    Log.d("TrendCard", "Image URL: $imageUrl") // Debugging the URL

    Card(
        modifier = Modifier
            .width(327.dp)
            .height(171.dp)
            .padding(8.dp), // Space around the card for separation
        elevation = 4.dp,
        backgroundColor = Color(0xFFDDD4BF) // Card background color
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Use the uploaded image as the background
            Image(
                painter = painterResource(id = R.drawable.frame_2610813), // Replace with the actual drawable name
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Crop the image to fill the card
            )

            // Second Background Vector Image

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // Image on the right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Adjusted the height
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                )  {
                    // Title and price on the left side
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Title (centered)
                        Text(
                            text = title,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                            color = Color(0xFF5D5C56),
                            maxLines = 4,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(5.dp))


                    }

                    // Image on the right
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Trend Image",
                        modifier = Modifier
                            .width(100.dp) // Image width
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(11.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Buy button section (at the bottom)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(top = 1.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Button-like label with background color AA8F5C
                    Box(
                        modifier = Modifier
                            .width(125.dp)
                            .height(27.dp)
                            .background(Color(0xFFAA8F5C), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Buy for $price TND", // Dynamic price
                            color = Color(0xFFFFFFFF), // Text color is white
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Username with image at the bottom left
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    // Slightly above the bottom of the card
                    contentAlignment = Alignment.BottomStart
                ){
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 1.dp)
                    ) {
                        // User Image
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        ){
                            // Ensure the user image URL is correct
                            val userImageUrl =
                                MyRetrofit.getBaseUrl()+"file/${user.pictureProfile}" // Corrected URL

                            AsyncImage(
                                model = userImageUrl,
                                contentDescription = "User Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(1.dp)) // Add some space between image and text

                        // User name text
                        Text(
                            text = user.name,
                            style = TextStyle(fontSize = 12.sp),
                            color = Color.White, // White text color
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.align(Alignment.CenterVertically) // Align text vertically with the image
                        )
                    }
                }
            }}}}

@Composable
fun CategoryItem(imageRes:Any, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // The Box containing the icon/image
        Box(///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    color = if (isSelected) Color(0xFFAA8F5C) else Color(0xFFDDD4BF)
                )
               // .border(1.dp, RoundedCornerShape(15.dp))
        ) {
            val painter = when (imageRes) {
                is Int -> painterResource(id = imageRes)
                is String -> rememberImagePainter(data = imageRes) // External URL
                else -> painterResource(id = R.drawable.placeholder)
            }
            Image(
                painter = painter,
                contentDescription = label,
                modifier = Modifier.size(50.dp).align(Alignment.Center)
            )

        }
        Spacer(modifier = Modifier.height(4.dp))

        // Text below the box
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            ),
            color = Color(0xFF5D5C56),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun ClothesCard(clothing: Clothes,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(153.dp) // Set the width of the card
            .height(206.dp) // Set the height of the card
            .padding(4.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        backgroundColor = Color(0xFFE4DFD4) // Card background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display clothing type at the top


            // Display image box with white background
            Box(
                modifier = Modifier
                    .width(126.dp) // Set width of the image box
                    .height(130.dp) // Set height of the image box
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFFFF)) // White background for the image box
            ) {
                clothing.images?.let { image ->
                    val fullImageUrl = MyRetrofit.getBaseUrl()+"file/${image}"
                    AsyncImage(
                        model = fullImageUrl,
                        contentDescription = clothing.name ?: "Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = clothing.types?.joinToString(", ")?: "No Type",  // Display types as a comma-separated string
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                color = Color(0xFF4F3E3E),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            // Name and price layout in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Display clothing name on the left
                Text(
                    text = clothing.name ?: "No Name",
                    style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
                    color =Color(0xFF907171),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                // Display clothing price on the right
                Text(
                    text = "${clothing.price} TND",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color(0xFF4F3E3E),
                    maxLines = 1,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun DetailsDialog(
    clothing: Clothes,
    onDismissRequest: () -> Unit,
    viewModel: MarketViewModel = viewModel(),

) {
   var price by remember { mutableStateOf(clothing.price?.toString() ?: "") }

    val clothesViewModel: ClothesViewModel = viewModel()
    val context = LocalContext.current // Get the context here

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                 .fillMaxWidth() // Fill the width of the parent
                .wrapContentHeight()
               // .heightIn(max = 650.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start // Align all content to the start
            ) {
                // Image
                clothing.images?.let { image ->
                    val fullImageUrl = MyRetrofit.getBaseUrl() + "file/${image}"
                    AsyncImage(
                        model = fullImageUrl,
                        contentDescription = clothing.name ?: "Image",
                        modifier = Modifier
                            .size(width = 240.dp, height = 220.dp) // Specific size
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name and Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Space between name and price
                ) {
                    Text(////////////////////////////
                        text = clothing.name ?: "",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold), // Bold text for name
                          color= Color(0xFF5D5C56)

                    )
                    // Price TextField
                    TextField(
                        value = price, // Valeur initiale
                        onValueChange = { newValue ->
                            price = newValue // Met à jour l'état local
                            clothesViewModel.price.value = newValue // Synchronise avec le ViewModel
                        },
                        modifier = Modifier
                            .width(100.dp) // Set the width of the TextField
                            .height(49.dp), // Set the height of the TextField
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFF2F1EE), // Container color
                            focusedIndicatorColor = Color.Transparent, // No underline when focused
                            unfocusedIndicatorColor = Color.Transparent // No underline when unfocused// Indicator color when unfocused
                        ),
                        textStyle = TextStyle(
                            color = Color.Black, // Text color
                            fontSize = 16.sp // Font size (adjust as needed)
                        )
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                // Size
                Text(
                    text = "Size: ${clothing.size}",
                    style = MaterialTheme.typography.body1,
                     color =Color(0xFF999999)

                )
                Spacer(modifier = Modifier.height(8.dp))

                // Description and Seller info in a Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color(0xFFEEEDEF), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)

                ) {
                    Column {
                        Text(
                            text = "Description:",
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold), // Optional: make it bold
                            color = Color(0xFF5D5C56) // Title color
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Actual description from clothes with Color(0xFF999999)
                        Text(
                            text = clothing.description ?: "No description available", // Fallback if description is null
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFF999999) // Description color
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Seller info
                        val seller = SessionManager().getUser()
                        Text(
                            text = "Seller: ${seller?.name ?: "Unknown"}",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFF999999)
                        )
                        Text(
                            text = "Phone: ${seller?.phoneNumber ?: "N/A"}",
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFF999999)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.width(122.dp)
                    ) {
                        Text("Cancel", color = Color(0xFFAA8F5C))
                    }
                    Button(
                        onClick = {
                            // Add action for "Buy"
                            val request = Request(
                                id = null,
                                sellerId = clothing.user,
                                clientId = SessionManager().getUserId(),
                                nameClient = DataInitializer.getUserData().name,
                                clientPhone = DataInitializer.getUserData().phoneNumber,
                                clientMail = DataInitializer.getUserData().email,
                                itemId = clothing.id,
                                isClothes = true, // true if it's clothes, otherwise false
                                isSold = false, // true if the item is sold
                                nameSeller = null,
                                nameClothes = clothing.name,
                                PriceClothes =clothing.price)

                            viewModel.addRequest(context, request)
                            onDismissRequest()
                        },
                        modifier = Modifier.width(122.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFAA8F5C), // Custom color
                            contentColor = Color.White // Text color
                        )
                    ) {
                        Text("Buy")
                    }
                }
            }
        }
    }
}




