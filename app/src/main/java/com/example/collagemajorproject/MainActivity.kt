package com.example.collagemajorproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.collagemajorproject.Navigation.MyAppNavigation
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthState
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel
import com.example.collagemajorproject.ui.theme.CollageMajorProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            CollageMajorProjectTheme {

                val authState = authViewModel.authState.observeAsState()
                val context = LocalContext.current

                LaunchedEffect(authState.value) {
                    when (authState.value) {

                        is AuthState.Error -> {
                            val error = (authState.value as AuthState.Error).message
                            if (error.contains(
                                    "disable",
                                    ignoreCase = true
                                ) || error.contains("invalid", ignoreCase = true)
                            ) {
                                Toast.makeText(
                                    context, "Your account has been disable . Please contact Ashu",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        else -> {}

                    }
                }


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
        override fun onResume() {
            super.onResume()
            authViewModel.verifyUserOnResume()
        }
    }




