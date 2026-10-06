package tn.esprit.chiccercle.presentation.auth.signUp.pageViews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField

@Composable
fun SubView1(
    name: MutableState<String>,
    phoneNumber: MutableState<Int?>,
    email: MutableState<String>,
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    onContinue: () -> Unit,

    ) {
    Column (
        modifier = Modifier.padding(horizontal = 32.dp)
    ){
        // Username TextField
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = null,
            placeholder = {
                Text("Username")
            },

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
        Spacer(modifier = Modifier.height(10.dp))
        // Email TextField
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = null,
            placeholder = {
                Text("Email")
            },

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
        Spacer(modifier = Modifier.height(10.dp))
        // Phone Number TextField
        TextField(
            value = phoneNumber.value?.toString() ?: "",
            onValueChange = {
                // Allow only numeric input and update the phoneNumber state
                if (it.all { char -> char.isDigit() }) {  // Check if all characters are digits
                    phoneNumber.value = it.toInt()  // Convert valid input back to Int
                }
            },
            label = null,
            placeholder = {
                androidx.compose.material.Text("Phone Number")
            },

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
        Spacer(modifier = Modifier.height(10.dp))
        // Password TextField
        androidx.compose.material.TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = null,
            placeholder = {
                androidx.compose.material.Text("Password")
            },

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
        Spacer(modifier = Modifier.height(10.dp))
        // Confirm Password TextField
        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = null,
            placeholder = {
               Text("Confirm Password")
            },

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
        Spacer(modifier = Modifier.height(20.dp))

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
