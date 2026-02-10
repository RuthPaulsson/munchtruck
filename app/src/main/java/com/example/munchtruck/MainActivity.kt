package com.example.munchtruck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.munchtruck.ui.navigation.NavGraph
import com.example.munchtruck.ui.theme.MunchtruckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MunchtruckTheme {
                NavGraph()
            }
        }
    }
}