package tn.esprit.chiccercle.ui.commen

import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import tn.esprit.chiccercle.ui.theme.primaryText
import tn.esprit.chiccercle.ui.theme.textField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun CustomDatePicker(
    label: String,
    selectedItem: MutableState<Date>,
    onDateSelected: (Date) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Box for Date Picker Trigger
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
                text = if (selectedItem.value == Date(0)) label else dateFormatter.format(selectedItem.value),
                color = if (selectedItem.value == Date(0)) Color(0xFFC1C1C1) else primaryText,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            // Down Arrow Icon
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Calendar Icon",
                tint = if (selectedItem.value == Date(0)) Color(0xFFC1C1C1) else primaryText,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    // Dialog for Date Picker
    if (expanded) {
        Dialog(onDismissRequest = { expanded = false }) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colors.surface,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AndroidView(

                        factory = { context ->
                            DatePicker(context).apply {
                                // Set current date
                                init(
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ) { _, year, month, dayOfMonth ->
                                    val selectedDate = calendar.apply {
                                        set(year, month, dayOfMonth)
                                    }.time
                                    selectedItem.value = selectedDate
                                }
                            }
                        },
                        modifier = Modifier.wrapContentSize()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Button
                    Button(onClick = { expanded = false }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
