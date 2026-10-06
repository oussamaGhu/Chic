package tn.esprit.chiccercle.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import tn.esprit.chiccercle.R
import tn.esprit.chiccercle.data.network.MyRetrofit
import tn.esprit.chiccercle.data.persistence.SessionManager

@Composable
fun ProfileScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    onNavigateToEditProfile: (String) -> Unit,

    refreshTrigger: Boolean,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val sessionManager = SessionManager()
    val user = remember { profileViewModel.user }

    // Reload user data when refreshTrigger changes
    LaunchedEffect(refreshTrigger) {
        profileViewModel.loadUserData()
        // Notify the parent that the profile was updated
    }

    Column(Modifier.background(Color.White)) {
        Spacer(Modifier.height(20.dp))

        // Profile Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            // Profile Picture
            if (user.value?.pictureProfile != null) {
                AsyncImage(
                    model = MyRetrofit.getBaseUrl()+"file/" + user.value?.pictureProfile,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Default Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }

            // User Details
            Text(
                text = user.value?.name ?: "None",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user.value?.email ?: "None",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))
            Divider()
        }

        // Profile Options
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F3EC))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ProfileOption(
                    title = "Edit Profile",
                    onClick = {
                        user.value?.email?.let { email ->
                            navController.navigate("editProfile/$email/${sessionManager.getUserId()}")
                        }
                    }
                )

                Divider()
                ProfileOption("My Clothes") { /* Add navigation logic here */ }
                Divider()
                ProfileOption("Privacy & Policy") { /* Add navigation logic here */ }
                Divider()
                ProfileOption("Terms & Conditions") { /* Add navigation logic here */ }
                Divider()
                ProfileOption("Invite Friends") { /* Add navigation logic here */ }
            }

            Spacer(Modifier.height(20.dp))

            // Log Out Button
            Button(
                onClick = {
                    onLogout()
                    SessionManager().clearSession()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Log Out",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }
    }
}



@Composable
fun ProfileOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, // Replace with your chevron icon
            contentDescription = "Navigate",
            tint = Color.Gray
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Mock user data

}