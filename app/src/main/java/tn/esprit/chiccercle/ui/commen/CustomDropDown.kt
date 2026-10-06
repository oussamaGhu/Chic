package tn.esprit.chiccercle.ui.commen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField

@Composable
fun CustomDropdown(
    selectedItem: MutableState<String>, // Now accepting MutableState
    onItemSelected: (String) -> Unit,
    options: List<String>,
    label: String = "Select an option"
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(textField, RoundedCornerShape(10.dp))
            .clickable { expanded = true }
            .padding(14.dp, 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Text
            Text(
                text = if (selectedItem.value.isEmpty()) label else selectedItem.value,
                color = if (selectedItem.value.isEmpty()) Color(0xFFC1C1C1) else primaryText,
                fontSize = 16.sp, // Set text size
                modifier = Modifier.weight(1f) // Make sure text takes available space
            )

            // Down Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowDropDown, // Use the down arrow icon
                contentDescription = "Dropdown Arrow",
                tint = primaryText, // Set the icon color to match the text color
                modifier = Modifier.size(24.dp) // Adjust the size of the icon
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onItemSelected(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}