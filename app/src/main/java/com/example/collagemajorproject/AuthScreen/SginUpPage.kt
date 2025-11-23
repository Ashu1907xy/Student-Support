package com.example.collagemajorproject.AuthScreen


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.collagemajorproject.R
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthState
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel
import java.io.File
import java.io.FileOutputStream


@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    // Image handling
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var emty by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }



    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("login")
            is AuthState.Error -> Toast.makeText(
                context, (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    var name by remember { mutableStateOf("") }
    var collegeName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        capturedBitmap?.let {
            bitmap = it
            // Convert bitmap to URI
            try {
                val file = File(context.cacheDir, "camera_temp_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    it.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                imageUri = Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    "Create Account",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    "Sign up to get started",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Profile Image Section
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap?.asImageBitmap()!!,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(4.dp, Color.White, CircleShape)
                                .clickable { showDialog = true }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.3f))
                                .border(4.dp, Color.White, CircleShape)
                                .clickable { showDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_person_24),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Color.White
                            )
                        }
                    }

                    // Camera badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-30).dp, y = (-10).dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF667eea))
                            .clickable { showDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_photo_camera_24),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Image Picker Dialog
                if (showDialog) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Choose Photo",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF667eea)
                                )
                                IconButton(onClick = { showDialog = false }) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_close_24),
                                        contentDescription = "Close",
                                        tint = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        cLauncher.launch()
                                        showDialog = false
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF667eea).copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_photo_camera_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = Color(0xFF667eea)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Camera", color = Color.Gray)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        launcher.launch("image/*")
                                        showDialog = false
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF764ba2).copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_gallery_thumbnail_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp),
                                            tint = Color(0xFF764ba2)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Gallery", color = Color.Gray)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Name Field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (name.isNotEmpty()) {
                                    IconButton(onClick = { name = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // College Name Field
                        OutlinedTextField(
                            value = collegeName,
                            onValueChange = { collegeName = it },
                            label = { Text("College Name") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (collegeName.isNotEmpty()) {
                                    IconButton(onClick = { collegeName = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Roll Number Field
                        OutlinedTextField(
                            value = rollNumber,
                            onValueChange = { rollNumber = it },
                            label = { Text("Roll Number") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (rollNumber.isNotEmpty()) {
                                    IconButton(onClick = { rollNumber = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Department Field
                        OutlinedTextField(
                            value = department,
                            onValueChange = { department = it },
                            label = { Text("Department") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (department.isNotEmpty()) {
                                    IconButton(onClick = { department = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Age Field
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (age.isNotEmpty()) {
                                    IconButton(onClick = { age = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Blood Group Field
                        OutlinedTextField(
                            value = bloodGroup,
                            onValueChange = { bloodGroup = it },
                            label = { Text("Blood Group") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (bloodGroup.isNotEmpty()) {
                                    IconButton(onClick = { bloodGroup = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mobile Number Field
                        OutlinedTextField(
                            value = mobile,
                            onValueChange = { mobile = it },
                            label = { Text("Mobile Number") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_person_24),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (mobile.isNotEmpty()) {
                                    IconButton(onClick = { mobile = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                if (email.isNotEmpty()) {
                                    IconButton(onClick = { email = "" }) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_close_24),
                                            contentDescription = "Clear"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (passwordVisible)
                                            "Hide password"
                                        else
                                            "Show password"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Create Account Button
                        Button(
                            onClick = {
                                authViewModel.signup(
                                    navController,
                                    name,
                                    collegeName,
                                    rollNumber,
                                    department,
                                    age,
                                    bloodGroup,
                                    mobile,
                                    email,
                                    password,
                                    imageUri,
                                    context


                                )
                                navController.navigate("login")
                            },
                            enabled = authState.value != AuthState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF667eea)
                            )
                        ) {
                            if (authState.value == AuthState.Loading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Create Account",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Already have an account?",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            TextButton(onClick = { navController.navigate("login") }) {
                                Text(
                                    "Login",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF667eea)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

