package tn.esprit.chiccercle.ui.commen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import tn.esprit.chiccercle.R

data class BottomNavigationItem(
    val title: String,
    val icon: Int, // Resource ID for icons
    val selectedColor: Color,
    val unselectedColor: Color,
    val route: String
)

// List of Navigation Items
val items = listOf(
    BottomNavigationItem(
        title = "Market",
        icon = R.drawable.ic_market, // Replace with your market icon resource
        selectedColor = Color(0xFF8B5E3C), // Brownish color
        unselectedColor = Color(0xFFA3A3A3), // Grayish color,
        route = "market"
    ),
    BottomNavigationItem(
        title = "Get Ready",
        icon = R.drawable.ic_get_ready, // Replace with your get ready icon resource
        selectedColor = Color(0xFF8B5E3C),
        unselectedColor = Color(0xFFA3A3A3),
        route = "get_ready"
    ),
    BottomNavigationItem(
        title = "Closet",
        icon = R.drawable.ic_closet, // Replace with your closet icon resource
        selectedColor = Color(0xFF8B5E3C),
        unselectedColor = Color(0xFFA3A3A3),
        route = "closet"
    ),
    BottomNavigationItem(
        title = "Profile",
        icon = R.drawable.ic_profile, // Replace with your profile icon resource
        selectedColor = Color(0xFF8B5E3C),
        unselectedColor = Color(0xFFA3A3A3),
        route = "profile"
    )
)

@Composable
fun CustomNavBar(navController: NavHostController) {
    var selectedItemIndex by remember { mutableStateOf(0) }

    Surface(color = MaterialTheme.colorScheme.background) {
        NavigationBar(
            modifier = Modifier.height(90.dp),
            containerColor = Color(0xFFF5F3EC) // Background color matching the image
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                        navController.navigate(item.route) {
                            // Avoid re-adding the same destination to the back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                              },
                    label = {
                        Text(
                            text = item.title,
                            color = if (selectedItemIndex == index) {
                                item.selectedColor
                            } else {
                                item.unselectedColor
                            },
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = if (selectedItemIndex == index) {
                                item.selectedColor
                            } else {
                                item.unselectedColor
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color.Transparent,
                        selectedTextColor = Color.Transparent,
                        unselectedIconColor = Color.Transparent,
                        unselectedTextColor = Color.Transparent
                    )

                )
            }
        }
    }
}



// Preview function for testing
@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    CustomNavBar(navController = NavHostController(LocalContext.current))
}