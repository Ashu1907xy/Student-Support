package com.example.collagemajorproject.Screens.Profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collagemajorproject.DataModel.ProfileData
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import java.io.File
import java.io.FileOutputStream


import com.example.collagemajorproject.R

import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel = AuthViewModel(),
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid
    val context = LocalContext.current

    var userData by remember { mutableStateOf<ProfileData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Editable fields
    var name by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }

    // Non-editable fields
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Image
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Load data from Firebase
    LaunchedEffect(Unit) {
        if (userId != null) {
            database.child(userId).child("ProfileData").get().addOnSuccessListener {
                val data = it.getValue(ProfileData::class.java)
                if (data != null) {
                    userData = data
                    name = data.name
                    college = data.collegeName
                    rollNumber = data.rollNumber
                    department = data.department
                    age = data.age
                    bloodGroup = data.bloodGroup
                    mobile = data.mobile
                    email = data.email

                    if (data.imageUrl.isNotEmpty()) {
                        try {
                            val bytes = Base64.decode(data.imageUrl, Base64.DEFAULT)
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        } catch (_: Exception) {
                        }
                    }
                }
                isLoading = false
            }
        }
    }

    // Camera
    val cLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        bitmap = capturedBitmap
        capturedBitmap?.let {
            val file = File(context.cacheDir, "camera_temp_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            imageUri = Uri.fromFile(file)
        }
    }

    // Gallery
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

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Edit Profile",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    "Update your details",
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
                            bitmap = bitmap!!.asImageBitmap(),
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

                // Image picker dialog
                if (showDialog) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
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
                                    }) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_photo_camera_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Color(0xFF667eea)
                                    )
                                    Text("Camera", color = Color.Gray)
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        launcher.launch("image/*")
                                        showDialog = false
                                    }) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_gallery_thumbnail_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = Color(0xFF764ba2)
                                    )
                                    Text("Gallery", color = Color.Gray)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Editable Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        @Composable
                        fun field(label: String, value: String, onChange: (String) -> Unit) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = onChange,
                                label = { Text(label) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        field("Name", name) { name = it }
                        field("College Name", college) { college = it }
                        field("Roll Number", rollNumber) { rollNumber = it }
                        field("Department", department) { department = it }
                        field("Age", age) { age = it }
                        field("Blood Group", bloodGroup) { bloodGroup = it }

                        OutlinedTextField(
                            value = mobile,
                            onValueChange = {},
                            label = { Text("Mobile Number") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {},
                            label = { Text("Email") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // ✅ Fixed Save button with image update
                        Button(
                            onClick = {
                                var finalImageBase64 = userData?.imageUrl ?: ""

                                // Convert new image to Base64 if available
                                bitmap?.let { bmp ->
                                    try {
                                        val outputStream = ByteArrayOutputStream()
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                                        val byteArray = outputStream.toByteArray()
                                        finalImageBase64 =
                                            Base64.encodeToString(byteArray, Base64.DEFAULT)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                val updated = ProfileData(
                                    name = name,
                                    collegeName = college,
                                    rollNumber = rollNumber,
                                    department = department,
                                    age = age,
                                    bloodGroup = bloodGroup,
                                    mobile = mobile,
                                    email = email,
                                    imageUrl = finalImageBase64
                                )

                                database.child(userId!!).child("ProfileData")
                                    .setValue(updated)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Profile Updated Successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Update Failed!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF667eea))
                        ) {
                            Text("Save Changes", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}




