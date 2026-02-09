package com.example.munchtruck.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.PrimaryOrange
import com.example.munchtruck.ui.theme.PrimaryText
import com.example.munchtruck.ui.theme.White

@Composable
fun StartScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id= R.drawable.bg_foodtruck),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to MunchTruck",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))
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
                        text = "I'm a Food Truck Owner",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "For those of you who run a food truck",
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
                        text = "I'm a Food Lover",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "For those of you who want to find a food truck",
                        style = MaterialTheme.typography.bodySmall,
                        color = White.copy(alpha = 0.9f)
                    )
                }
            }


        }
    }
}