package com.example.collagemajorproject.Screens

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.collagemajorproject.R
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthState

import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel

import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState() // change

    val alpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {

        alpha.animateTo(1f, animationSpec = tween(2500))

        delay(1500)
        navController.popBackStack()


        when (authState.value) {
            // change
            is AuthState.Authenticated -> navController.navigate("drawer")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> navController.navigate("login")
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        LoaderAnimation(
            modifier = Modifier.size(400.dp),
            anim = R.raw.study
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "@the_ashu_world",
            modifier = Modifier
                .alpha(alpha.value)
                .background(Color(0xFFF59E0B))
                .padding(20.dp),

            fontSize = 32.sp,


            )

    }
}

@Composable
fun LoaderAnimation(modifier: Modifier = Modifier, anim: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )


}