package tn.esprit.chiccercle.ui.commen

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier

@Composable
fun ActionButton() {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.Black)
    }

    // Dropdown menu
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(onClick = {
            // Handle Remove action
            expanded = false
        }) {
            Text("Remove")
        }
        DropdownMenuItem(onClick = {
            // Handle Mark as Sold action
            expanded = false
        }) {
            Text("Mark as Sold")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionButton() {
    ActionButton()
}