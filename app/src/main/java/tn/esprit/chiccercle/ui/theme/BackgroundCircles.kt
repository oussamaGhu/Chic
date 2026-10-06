package tn.esprit.chiccercle.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tn.esprit.chiccercle.R

@Composable
fun BackgroundCircles() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = "Top Right Circle",
            modifier = Modifier
                .size(154.dp)
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = -30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.frame2),
            contentDescription = "Bottom Center Circle",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.frame3),
            contentDescription = "Top Left Circle",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopStart)
                .offset(x = -50.dp, y = 20.dp)
        )
    }
}
