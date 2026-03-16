import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.model.Request
import tn.esprit.chiccercle.presentation.closet.ClothesViewModel
import tn.esprit.chiccercle.presentation.market.seller.SellerMarkerViewModel
import tn.esprit.chiccercle.ui.commen.AppColors
import tn.esprit.chiccercle.ui.commen.FontStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerMarketScreen(requestViewModel: SellerMarkerViewModel) {
    val requests = requestViewModel.requests.value
    val isLoading = requestViewModel.isLoading.value
    val errorMessage = requestViewModel.errorMessage.value

    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFFFDFCFB)
                    ),
                    title = {
                        Text(
                            text = "Market",
                            style = FontStyles.title2,
                            color = AppColors.primaryText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
                Divider(
                    color = Color(0xFF9F9B9B),
                    thickness = 1.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(Color(0xFFF4F2E9))
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Request",
                    style = FontStyles.title2,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        items(requests) { request ->
                            RequestItem(request,requestViewModel)
                            Divider(
                                color = Color(0xFFBDBDBD),
                                thickness = 1.dp
                            ) // Divider between items
                        }
                    }
                }
            }
        }
    )
}
@Composable
fun RequestItem(request: Request,requestViewModel: SellerMarkerViewModel) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val clothesViewModel: ClothesViewModel = viewModel()
    Box(
        modifier = Modifier.fillMaxWidth() // Ensure Box takes full width
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Distribute space between items
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    request.nameClothes?.let {
                        Text(
                            text = it,
                            style = FontStyles.title3,
                            color = AppColors.primaryText,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    request.clientMail?.let {
                        Text(
                            text = it,
                            style = FontStyles.subtitle2,
                            color = AppColors.secondaryText,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    request.clientPhone?.let {
                        Text(
                            text = it.toString(),
                            style = FontStyles.subtitle2,
                            color = AppColors.secondaryText,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Proposed Price by ${request.nameClient}: ${request.PriceClothes}",
                        style = FontStyles.subtitle2,
                        color = AppColors.secondaryText,
                    )
                }

                IconButton(onClick = {
                    val phoneNumber = request.clientPhone
                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(dialIntent)
                }) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFFAA8F5C))
                }

                // Positioned more flexibly
                Box(
                    modifier = Modifier


                        //.align(alignment = Alignment.CenterEnd)
                ) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier



                            .padding(20.dp)
                    ){
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .padding(end = 16.dp) // Padding inside dropdown from right
                        ) {
                            androidx.compose.material.DropdownMenuItem(onClick = {

                                requestViewModel.deleteRequest(
                                    request.id.toString(),
                                    onSuccess = {

                                    // Handle success - Show a success message or update the UI
                                        //Toast.makeText(context, "Request deleted successfully", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { errorMessage ->
                                        // Handle error - Show an error message
                                    //    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    }
                                )


                                expanded = false
                            }) {
                                Text("Remove")
                            }
                            androidx.compose.material.DropdownMenuItem(onClick = {
                                requestViewModel.updateRequest(
                                    request.id.toString(),
                                    request,
                                    onSuccess = {

                                        // Handle success - Show a success message or update the UI
                                        //Toast.makeText(context, "Request deleted successfully", Toast.LENGTH_SHORT).show()
                                       // requestViewModel.fetchRequests()
                                    },
                                    onError = { errorMessage ->
                                        // Handle error - Show an error message
                                        //    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                    }
                                )

                                expanded = false
                            }) {
                                Text("Mark as Sold")
                            }
                        }
                    }

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SellerMarketScreen(requestViewModel = SellerMarkerViewModel())
}
