package com.example.munchtruck.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.White

@Composable
fun StartScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.bg_foodtruck),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(230.dp))

            Text(
                "Welcome to",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 50.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.9f),
                        offset = Offset(4f, 4f),
                        blurRadius = 10f
                    )
                ),

                color = White,
                fontWeight = FontWeight.Bold
            )
//
            Image(
                painter = painterResource(id = R.drawable.munchtruck_text),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(70.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                       "I'm a Food Truck Owner",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                  "For those of you who run a food truck",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.9f)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("discovery")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                       "I'm a Food Lover",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                     "For those of you who want to find a food truck",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.9f)
                    )
                }
            }


        }
    }
}