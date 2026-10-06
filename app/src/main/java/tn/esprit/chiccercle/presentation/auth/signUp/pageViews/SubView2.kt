package tn.esprit.chiccercle.presentation.auth.signUp.pageViews

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.ui.commen.CustomDropdown
import tn.esprit.chiccercle.ui.commen.CustomDatePicker
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField
import java.util.Calendar
import java.util.Date
@Composable
fun SubView2(
    gender: MutableState<String>,
    userType: MutableState<String>,
    dateOfBirth: MutableState<Date>,
    address: MutableState<String>,
    selectedImageUri: MutableState<Uri?>,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val genderOptions = listOf("Male", "Female", "Other")
    val userTypeOptions = listOf("client", "seller")

    // Image picker launcher
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri.value = uri
        }

    // Date picker dialog setup
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateOfBirth.value = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Image Picker for Profile Picture
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(textField)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri.value != null) {
                AsyncImage(
                    model = selectedImageUri.value,
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Text(
                    "Add Photo",
                    color = Color(0xFFC1C1C1),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomDropdown(
            selectedItem = userType,
            onItemSelected = { userType.value = it },
            options = userTypeOptions,
            label = "Select User Type"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomDropdown(
            selectedItem = gender,
            onItemSelected = { gender.value = it },
            options = genderOptions,
            label = "Select Gender"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CustomDatePicker(
            label = "Select Date of Birth",
            selectedItem = dateOfBirth,
            onDateSelected = { date ->
                println("Selected Date: $date")
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = address.value,
            onValueChange = { address.value = it },
            placeholder = { Text("Address") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = textField,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.input_field_background_color),
                textColor = primaryText
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.login_button_color)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue", color = Color.White)
        }
    }
}