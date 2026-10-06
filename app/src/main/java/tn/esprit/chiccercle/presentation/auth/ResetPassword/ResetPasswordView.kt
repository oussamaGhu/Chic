package tn.esprit.chiccercle.presentation.auth.ResetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResetPasswordScreen() {
    val viewModel: ResetPasswordViewModel = viewModel()  // Getting the ViewModel


    Box {
        Image(
            painter = painterResource(id = R.drawable.contents), // Background Image
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Title Text
            Text(
                text = "Reset Your Password",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.welcome_text_color),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle Text
            Text(
                text = "Create a new password for your account.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.subtitle_text_color),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))

            // New Password Input Field
            TextField(
                value = viewModel.newPassword.value ,
                onValueChange = { viewModel.newPassword.value = it },
                label = null,
                placeholder = { Text("New Password") },
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
                    .clip(RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Confirm Password Input Field
            TextField(
                value = viewModel.confirmPassword.value,
                onValueChange = {  viewModel.confirmPassword.value = it },
                label = null,
                placeholder = { Text("Confirm Password") },
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
                    .clip(RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.savePassword() // Call save password function from ViewModel
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.login_button_color)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Save", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Back to Login Option
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Remembered your password?", color = Color(0xFF999999), fontSize = 14.sp)
                Text(
                    " Sign In",
                    color = Color(0xFFAA8F5C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { /* Add navigation to login screen */ }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewLoginScreen() {
    ResetPasswordScreen(

    )
}