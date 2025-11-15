package tn.esprit.chiccercle.presentation.auth.signUp


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import tn.esprit.chiccercle.presentation.auth.signUp.pageViews.SubView1
import tn.esprit.chiccercle.presentation.auth.signUp.pageViews.SubView2
import tn.esprit.chiccercle.presentation.auth.signUp.pageViews.SubView3
import tn.esprit.chiccercle.R
import java.io.File


@Composable
fun SignUpScreen(viewModel: SignUpViewModel,
                 onNavigateToSignIn: () -> Unit,
                 onSignUpSuccess: () -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Image(
                painter = painterResource(id = R.drawable.contents), // Replace with your image resource
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_image),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "New Account",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.welcome_text_color),
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Please Provide your data here",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.subtitle_text_color),
                    fontFamily = FontFamily.Serif
                )

                val coroutineScope = rememberCoroutineScope()
                val pagerState = rememberPagerState(pageCount = { 3 })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(490.dp)
                ) { page ->
                    when (page) {
                        0 -> SubView1(
                            name = viewModel.name,
                            email = viewModel.email,
                            password = viewModel.password,
                            confirmPassword = viewModel.confirmPassword,
                            phoneNumber = viewModel.phoneNumber,
                            onContinue = {
                                if (viewModel.validateStep1()) {
                                    coroutineScope.launch { pagerState.scrollToPage(1) }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
                                    }
                                }
                            }
                        )
                        1 -> SubView2(
                            gender = viewModel.gender,
                            userType = viewModel.userType,
                            dateOfBirth = viewModel.dateOfBirth,
                            address = viewModel.address,
                            selectedImageUri = viewModel.selectedImageUri,
                            onContinue = {
                                if (viewModel.validateStep2()) {
                                    coroutineScope.launch { pagerState.scrollToPage(2) }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
                                    }
                                }

                            }
                        )
                        2 -> SubView3(
                            selectedMorphology = viewModel.selectedMorphology,
                            height = viewModel.height,
                            weight = viewModel.weight ,
                            onSignup = {
                                if (viewModel.validateStep3()) {
                                    if(viewModel.selectedImageUri.value != null){
                                       val file = viewModel.saveImageUriToFile(context,viewModel.selectedImageUri.value,"image")
                                        if (file != null) {
                                            viewModel.uploadFile(file,
                                                onResult = {
                                                    println(viewModel.fileId.value)

                                                    viewModel.signUp(
                                                        it,

                                                        onSuccess = {
                                                            onSignUpSuccess()
                                                        },
                                                        onError = { error ->
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar(error)
                                                            }
                                                        }
                                                    )
                                                }
                                            )
                                        }


                                    }



                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(viewModel.snackbarMessage.value)
                                    }
                                }



                            }
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already Have Account?", color = Color(0xFF999999), fontSize = 14.sp)
                    Text(
                        " Sign In",
                        color = Color(0xFFAA8F5C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable {

                                onNavigateToSignIn()

                            }

                    )
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SignUpScreen(
        viewModel = SignUpViewModel(),
        onNavigateToSignIn = {},
        onSignUpSuccess = {}

    )
}