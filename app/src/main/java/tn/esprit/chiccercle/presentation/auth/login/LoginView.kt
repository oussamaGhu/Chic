package tn.esprit.chiccercle.presentation.auth.login


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.ui.theme.*
import tn.esprit.chiccercle.ui.theme.primaryText


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgetPassword: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)){
        Image(
            painter = painterResource(id = R.drawable.contents), // Replace with your image resource
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(), // Makes image fill the box
            contentScale = ContentScale.Crop // Ensures the image covers the whole area
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .align(Alignment.Center), // Align the Column at the top center
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                       // Logo positioned at the top
            Image(
                painter = painterResource(id = R.drawable.logo_image), // Replace with your logo image ID
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp) // Adjust logo size if needed
            )

            Spacer(modifier = Modifier.height(30.dp)) // Gap between logo and welcome text

            // Welcome Text
            Text(
                text = "Welcome",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.welcome_text_color),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp)) // Space between welcome text and subtitle

            // Subtitle text
            Text(
                text = "Chic Choices, Smart Sales.",
                fontSize = 16.sp,
                color = colorResource(id = R.color.subtitle_text_color),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Email Input Field
            TextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = null,
                placeholder = { Text("Email") }, // Placeholder text


                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = textField, // Sets the background color
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                 //   cursorColor = colorResource(id = R.color.input_field_background_color),
                    textColor = primaryText, // Customize text color if needed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(12.dp)), // Adds rounded corners with a radius of 12.dp

            )


            Spacer(modifier = Modifier.height(8.dp))

            // Password Input Field
            Column (

            ) {
                TextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.password.value = it },
                    label = null,
                    placeholder = { Text("Password") }, // Placeholder text


                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = textField, // Sets the background color
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = colorResource(id = R.color.input_field_background_color),
                        textColor = primaryText, // Customize text color if needed
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)

                        .clip(RoundedCornerShape(12.dp)), // Adds rounded corners with a radius of 12.dp

                    visualTransformation = PasswordVisualTransformation()
                )
                Text(
                    " Fotgot Password",
                    color = Color(0xFFAA8F5C),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp)
                        .clickable { onForgetPassword() }

                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            // Log in Button
            Button(
                onClick = {
                    if(viewModel.validateLogin()){
                        viewModel.login(

                            onSuccess = {



                                onLoginSuccess()
                            },
                            onError = { error ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(error)
                                }
                                println(error)
                            }
                        )
                    }else{
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
                        }
                    }







                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.login_button_color) ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Log in", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Spacer(modifier = Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't Have Account?", color = Color(0xFF999999), fontSize = 14.sp)
                Text(
                    " Sign Up",
                    color = Color(0xFFAA8F5C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                            modifier = Modifier
                            .clickable { onNavigateToSignUp() }

                )
            }


        }}}


}




@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        viewModel= LoginViewModel(),
        onNavigateToSignUp= {},
    onLoginSuccess= {},
        onForgetPassword = {}
    )
}