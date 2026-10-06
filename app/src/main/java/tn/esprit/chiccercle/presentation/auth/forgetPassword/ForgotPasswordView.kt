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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField
import tn.esprit.chiccercle.presentation.auth.forgetPassword.ForgetPasswordViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ForgetPasswordScreen() {
    val viewModel: ForgetPasswordViewModel = viewModel()  // Getting the ViewModel


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

            // Welcome Text
            Text(
                text = "Forgot Password",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.welcome_text_color),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle Text
            Text(
                text = "Select you existing mail.",
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
                placeholder = { Text("Email") },
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

            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(30.dp))

            // Send Mail Button
            Button(
                onClick = {
                    viewModel.sendMail() // Call send mail function from ViewModel
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.login_button_color)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Send Mail", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Sign Up Option
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Did you remember your account?", color = Color(0xFF999999), fontSize = 14.sp)
                Text(
                    " Sign In",
                    color = Color(0xFFAA8F5C),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { /* Add navigation to sign up screen */ }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewLoginScreen() {
    ForgetPasswordScreen(

    )
}