package tn.esprit.chiccercle.presentation.auth.signUp.pageViews

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.esprit.chiccercle.ui.commen.CustomDropdown


@Composable
fun SubView3(
    selectedMorphology: MutableState<String>,
    height: MutableState<Float>,
    weight: MutableState<Float>,
    onSignup: () -> Unit
) {

    val morphologyOptions = listOf("Type rectangulaire", "Type ovale", "Type triangle", "Type hour-glass")


    Column(
        modifier = Modifier

            .padding(horizontal = 32.dp),

        ) {


        // Dropdown menu for selecting morphology
        CustomDropdown(selectedItem = selectedMorphology, onItemSelected = { selectedMorphology.value = it }, options = morphologyOptions, label = "Select Morphologie")


        Spacer(modifier = Modifier.height(24.dp))

        // Height Slider
        Text(text = "Height:", color = Color(0xFF333333), fontSize = 16.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = height.value,
                onValueChange = { newValue ->
                    height.value = newValue // Update the state correctly
                },
                valueRange = 17f..200f, // Ensure this matches the expected range
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFAA8F5C),
                    activeTrackColor = Color(0xFFD9D9D9)
                ),
                modifier = Modifier.weight(1f) // Allow slider to take up space
            )

            Box (modifier = Modifier.width(55.dp)){
                Text(
                    text = "${height.value.toInt()} cm",
                    color = Color(0xFFAA8F5C),
                    fontSize = 16.sp,


                    )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))



        // Weight Slider
        Text(text = "Weight:", color = Color(0xFF333333), fontSize = 16.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = weight.value,
                onValueChange = { newValue ->
                    weight.value = newValue // Update the weight value correctly
                },
                valueRange = 17f..200f, // Ensure the range is logical
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFAA8F5C),
                    activeTrackColor = Color(0xFFD9D9D9)
                ),
                modifier = Modifier.weight(1f) // Allow slider to take up the available space
            )

            Box(modifier = Modifier.width(55.dp)) {
                Text(
                    text = "${weight.value.toInt()} kg", // Display the current weight value
                    color = Color(0xFFAA8F5C),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Sign Up Button
        Button(
            onClick = {
                onSignup()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFAA8F5C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Sign Up", color = Color(0xFFECE9E1))
        }




    }
}

