package tn.esprit.chiccercle.presentation.profile.subViews

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.model.User
import tn.esprit.chiccercle.presentation.profile.ProfileViewModel
import java.util.Calendar
import java.util.Date


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun EditProfileFormPreview() {
    val mockUser = User(
        name = "John Doe",
        email = "johndoe@example.com",
        password = "password123",
        phoneNumber = null,
        role = "client",
        pictureProfile = null, // Provide a placeholder URL or keep it null
        birthday = Date(),
        clothes = emptyList(),
        assemble = emptyList(),
        height = 180,
        weight = 75,
        shape = "Type rectangulaire",
        location = null
    )

    EditProfileForm(
        user = mockUser,
        onSaveChanges = { updatedUser, imageUri ->
            // Handle save logic for preview purposes
            println("Updated User: $updatedUser, Image URI: $imageUri")
        }
    )
}


@Composable
fun EditProfileScreen(
    userId: String, // userId passed explicitly
    onSaveChanges: (User) -> Unit,
    onBack: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()

) {
    val user = profileViewModel.user.value
    val viewModel: EditProfileVIewModel = viewModel()

    // Observing user state
    val userState = remember { mutableStateOf<User?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    // Fetch user data
    LaunchedEffect(userId) {
        viewModel.loadUserData(
            userId,
            onSuccess = { user ->
                userState.value = user
                isLoading.value = false
            },
            onError = { error ->
                println("Error loading user: $error")
                isLoading.value = false
            }
        )
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F3EC)
    )
    {
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            userState.value?.let { user ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F3EC) // Background color
                ) {
                    Column {
                        // Back Button
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Edit Your Profile",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.h6,
                            color = Color(0xFF5D5C56), // Set color
                            fontFamily = FontFamily.Serif, // Apply Georgia font
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp), // Center the text and add vertical padding
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(5.dp)) // Adding more space between title and image


                        EditProfileForm(
                            user = user,
                            onSaveChanges = { updatedUser, newImageUri ->
                                isLoading.value = true
                                profileViewModel.updateUser(updatedUser)
                                // Call ViewModel to update user data
                                viewModel.updateUserData(
                                    userId = userId, // Pass userId explicitly
                                    updatedUser = updatedUser, // Pass updated user details

                                    onSuccess = {
                                        isLoading.value = false
                                        // Call the provided onSaveChanges callback with the updated user
                                        onSaveChanges(updatedUser)
                                    },
                                    onError = { error ->
                                        println("Error updating user: $error")
                                        isLoading.value = false
                                    }
                                )
                            }
                        )

                    }
                }
            }
        }
    }}

@Composable
fun EditProfileForm(
    user: User,
    onSaveChanges: (User, Uri?) -> Unit
) {
    // State for fields
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var height by remember { mutableStateOf(user.height?.toFloat() ?: 170f) }
    var weight by remember { mutableStateOf(user.weight?.toFloat() ?: 70f) }
    var selectedMorphology by remember { mutableStateOf(user.shape ?: "Type rectangulaire") }
    var selectedUserType by remember { mutableStateOf(user.role ?: "client") }
    var birthday by remember { mutableStateOf(user.birthday ?: Date()) }
    var newProfileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Validation state
    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isHeightValid by remember { mutableStateOf(true) }
    var isWeightValid by remember { mutableStateOf(true) }
    var isBirthdayValid by remember { mutableStateOf(true) }
    var isProfileImageValid by remember { mutableStateOf(true) }

    // Options for dropdowns
    val morphologyOptions = listOf("Type rectangulaire", "Type ovale", "Type triangle", "Type hour-glass")
    val userTypeOptions = listOf("client", "seller")

    // Context and DatePickerDialog
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = birthday

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            birthday = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            newProfileImageUri = uri
        }

    }

    // Validate form
    fun validateForm(): Boolean {
        isNameValid = name.isNotEmpty()
        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isHeightValid = height in 100f..250f
        isWeightValid = weight in 30f..200f
        isBirthdayValid = birthday.before(Date()) // Make sure the date is before the current date
        isProfileImageValid = newProfileImageUri != null || user.pictureProfile != null

        return isNameValid && isEmailValid && isHeightValid && isWeightValid && isBirthdayValid && isProfileImageValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Make the entire content scrollable
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Image Section
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFECE9E1))
                .clickable {
                    imagePickerLauncher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (newProfileImageUri != null) {
                AsyncImage(
                    model = newProfileImageUri,
                    contentDescription = "New Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (user.pictureProfile != null) {
                AsyncImage(
                    model = MyRetrofit.getBaseUrl()+"file/${user.pictureProfile}",
                    contentDescription = "Current Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Add Photo", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name Field
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .width(288.dp)
                .height(48.dp)
                .background(Color(0xFFECE9E1), shape = MaterialTheme.shapes.small),
            placeholder = { Text("Enter your name", color = Color(0xFF888888)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (!isNameValid) {
            Text("Name is required", color = Color.Red, fontSize = 12.sp)
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .width(288.dp)
                .height(49.dp)
                .background(Color(0xFFECE9E1), shape = MaterialTheme.shapes.small),
            placeholder = { Text("Enter your email", color = Color(0xFF888888)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        if (!isEmailValid) {
            Text("Invalid email format", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Birthday Field with Date Picker
        Row(
            modifier = Modifier
                .width(288.dp)
                .height(48.dp)
                .background(Color(0xFFECE9E1), shape = MaterialTheme.shapes.small)
                .clickable { datePickerDialog.show() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Birthday: ${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}",
                color = Color.Black
            )
        }
        if (!isBirthdayValid) {
            Text("Please select a valid birthday", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Morphology Dropdown
        DropdownField(
            label = "Morphology",
            selectedOption = selectedMorphology,
            options = listOf("Type rectangulaire", "Type ovale", "Type triangle", "Type hour-glass"),
            onOptionSelected = { selectedMorphology = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Type Dropdown
        DropdownField(
            label = "User Type",
            selectedOption = selectedUserType,
            options = listOf("client", "seller"),
            onOptionSelected = { selectedUserType = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Height Slider
        Text("Height: ${height.toInt()} cm", color = Color.Black)
        Slider(
            value = height,
            onValueChange = { height = it },
            valueRange = 100f..200f,
            modifier = Modifier.width(240.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFAA8F5C),
                activeTrackColor = Color(0xFFAA8F5C)
            )
        )
        if (!isHeightValid) {
            Text("Please enter a valid height", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Weight Slider
        Text("Weight: ${weight.toInt()} kg", color = Color.Black)
        Slider(
            value = weight,
            onValueChange = { weight = it },
            valueRange = 30f..200f,
            modifier = Modifier.width(240.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFAA8F5C),
                activeTrackColor = Color(0xFFAA8F5C)
            )
        )
        if (!isWeightValid) {
            Text("Please enter a valid weight", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(1.dp)) // Added space before the button


        // Save Button
        Button(
            onClick = {
                if (validateForm()) {
                    val updatedUser = user.copy(
                        name = name,
                        email = email,
                        height = height.toInt(),
                        weight = weight.toInt(),
                        shape = selectedMorphology,
                        role = selectedUserType,
                        birthday = birthday
                    )
                    onSaveChanges(updatedUser, newProfileImageUri)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFAA8F5C))
        ) {
            Text("Save Changes", color = Color.White)
        }
    }
}



@Composable
fun DropdownField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(288.dp)
            .height(48.dp)
            .fillMaxWidth()
            .background(Color(0xFFECE9E1), shape = MaterialTheme.shapes.small)
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Text(
            text = selectedOption,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}


