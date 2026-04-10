package tn.esprit.chiccercle
import ForgetPasswordScreen
import SellerMarketScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import android.view.WindowManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tn.esprit.chiccercle.data.DataInitializer
import tn.esprit.chiccercle.data.persistence.SessionManager
import tn.esprit.chiccercle.presentation.getReady.GetReadyScreen
import tn.esprit.chiccercle.presentation.auth.login.LoginScreen
import tn.esprit.chiccercle.presentation.auth.login.LoginViewModel
import tn.esprit.chiccercle.presentation.market.MarketScreen

import tn.esprit.chiccercle.presentation.auth.signUp.SignUpScreen
import tn.esprit.chiccercle.presentation.auth.signUp.SignUpViewModel
import tn.esprit.chiccercle.presentation.closet.ClothesScreen
import tn.esprit.chiccercle.presentation.closet.ClothesViewModel
import tn.esprit.chiccercle.presentation.profile.ProfileScreen
import tn.esprit.chiccercle.presentation.profile.subViews.EditProfileScreen
import tn.esprit.chiccercle.ui.commen.CustomNavBar

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import tn.esprit.chiccercle.presentation.market.seller.SellerMarkerViewModel
import tn.esprit.chiccercle.presentation.profile.ProfileViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.initialize(this)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            val navController = rememberNavController()
            val isLoggedIn = !SessionManager().getUserId().isNullOrEmpty()

            MainNavGraph(navController = navController, isLoggedIn = isLoggedIn )
        }
    }
}

@Composable
fun MainNavGraph(navController: NavHostController, isLoggedIn: Boolean) {
    var refreshTrigger by remember { mutableStateOf(false) } // State to trigger refresh
    val profileViewModel: ProfileViewModel = viewModel() // Shared ViewModel

    if (isLoggedIn) {
        LaunchedEffect(Unit) {
            val userId = SessionManager().getUserId()
            if (userId != null) {
                DataInitializer.initializeData(userId)
                profileViewModel.loadUserData()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "navbar" else "login"
    ) {
        val role = SessionManager().getRole()
        composable("login") {
            LoginScreen(
                viewModel = LoginViewModel(),
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = {
                    navController.navigate("navbar") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onForgetPassword = {
                    navController.navigate("forget_password")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = SignUpViewModel(),
                onNavigateToSignIn = { navController.navigate("login") },
                onSignUpSuccess = {
                    navController.navigate("navbar") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        composable("forget_password") {
            ForgetPasswordScreen()
        }

        composable("navbar") {
            MainScreen(
                parentNavController = navController,
                refreshTrigger = refreshTrigger,
                profileViewModel = profileViewModel
            )
        }

        composable(
            route = "editProfile/{email}/{userId}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: "Unknown"
            val userId = backStackEntry.arguments?.getString("userId") ?: "Unknown"

            EditProfileScreen(
                userId = userId,
                onSaveChanges = { updatedUser ->
                    refreshTrigger = !refreshTrigger // Notify ProfileScreen to refresh
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(
    parentNavController: NavHostController,
    refreshTrigger: Boolean,
    profileViewModel: ProfileViewModel
) {
    val role = SessionManager().getRole()
    val isSeller = role == "seller"
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { CustomNavBar(navController = bottomNavController) }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "market",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("market") {
                if (isSeller) {
                    SellerMarketScreen(requestViewModel = SellerMarkerViewModel())  // Navigate to SellerMarketScreen if isSeller is false

                } else {
                    MarketScreen()  // Navigate to MarketScreen if isSeller is true
                }
            }
            composable("get_ready") { GetReadyScreen() }
            composable("closet") {
                val clothesViewModel: ClothesViewModel = viewModel()
                val userId = SessionManager().getUserId()

                if (userId != null) {
                    ClothesScreen(viewModel = clothesViewModel, userId = userId)
                } else {
                    parentNavController.navigate("login") {
                        popUpTo("navbar") { inclusive = true }
                    }
                }
            }
            composable("profile") {
                ProfileScreen(
                    navController = parentNavController,
                    onLogout = {
                        parentNavController.navigate("login") {
                            popUpTo("navbar") { inclusive = true }
                        }
                    },
                    onNavigateToEditProfile = { userId ->
                        if (userId.isNotEmpty()) {
                            parentNavController.navigate("editProfile/${SessionManager().getUserId()}")
                        }
                    },
                    refreshTrigger = refreshTrigger,
                    profileViewModel = profileViewModel // Pass shared ViewModel
                )
            }
        }
    }


}
