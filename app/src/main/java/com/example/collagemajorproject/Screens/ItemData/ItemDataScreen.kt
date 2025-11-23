package com.example.collagemajorproject.Screens.ItemData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ItemDataScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
        Text("Store is  coming soon!" , fontSize = 35.sp , fontStyle = FontStyle.Italic)

    }

}