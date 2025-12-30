package com.example.collagemajorproject.Screens.PickMe

import android.content.Intent
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
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.collagemajorproject.DataModel.ProfileData
import com.example.collagemajorproject.R
import com.example.collagemajorproject.ViewModel.TimetableViewModel.TimeTableViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLostItemScreen(
    viewModel: TimeTableViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val addState = viewModel.addPost.collectAsState().value
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var userData by remember { mutableStateOf<ProfileData?>(null) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ MODERN COLOR PALETTE
    val primaryGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
    )
    val accentGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFf093fb), Color(0xFFf5576c))
    )
    val successGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
    )

    // ✅ ADD SCAFFOLD
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Lost Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF667eea),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        // Fetch user data
        LaunchedEffect(Unit) {
            if (userId != null) {
                database.child(userId).child("ProfileData").get()
                    .addOnSuccessListener { snapshot ->
                        userData = snapshot.getValue(ProfileData::class.java)
                        isLoadingUser = false
                    }
                    .addOnFailureListener {
                        isLoadingUser = false
                        Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                isLoadingUser = false
            }
        }

        val cLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { capturedBitmap ->
            capturedBitmap?.let {
                bitmap = it
                try {
                    val file =
                        File(context.cacheDir, "camera_temp_${System.currentTimeMillis()}.jpg")
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

        // Show loading while fetching user data
        if (isLoadingUser) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        strokeWidth = 4.dp,
                        color = Color(0xFF667eea)
                    )
                    Text(
                        "Loading...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF667eea)
                    )
                }
            }
            return@Scaffold
        }

        // ✅ MAIN UI WITH GRADIENT BACKGROUND
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F7FA),
                            Color(0xFFE8EAF6)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                // ✅ IMAGE PICKER CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clickable { showDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap?.asImageBitmap()!!,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )

                            // ✅ EDIT OVERLAY
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                                    .size(48.dp)
                                    .background(primaryGradient, CircleShape)
                                    .clickable { showDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_photo_camera_24),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF667eea).copy(alpha = 0.1f),
                                                    Color(0xFF764ba2).copy(alpha = 0.1f)
                                                )
                                            ),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "placeholder",
                                        modifier = Modifier.size(40.dp),
                                        tint = Color(0xFF667eea)
                                    )
                                }

                                Text(
                                    text = "Tap to add photo",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF667eea),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Upload a clear image of the lost item",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ✅ TITLE INPUT FIELD
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Item Title") },
                    placeholder = { Text("e.g., Lost Blue Backpack") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = Color(0xFF667eea)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !addState.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF667eea),
                        focusedLabelColor = Color(0xFF667eea),
                        cursorColor = Color(0xFF667eea),
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(Modifier.height(24.dp))

                // ✅ USER INFO CARD
                if (userData != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF667eea).copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF667eea)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Posting as",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = userData!!.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2D3748)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // ✅ SUBMIT BUTTON WITH GRADIENT
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        if (imageUri == null) {
                            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        if (userId == null) {
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val userName = userData?.name ?: "Anonymous"
                        val contact = userData?.mobile ?: "911"
                        val userImageBase64 = userData?.imageUrl ?: ""

                        viewModel.addLostItem(
                            userName = userName,
                            title = title,
                            contactInfo = contact,
                            imageUri = imageUri,
                            context = context,
                            userImageBase64 = userImageBase64,

                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !addState.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(primaryGradient, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (addState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Text(
                                    "Post Lost Item",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // ✅ IMAGE PICKER DIALOG
            if (showDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { showDialog = false },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable(enabled = false) { },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Choose Photo",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Camera Option
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        cLauncher.launch()
                                        showDialog = false
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(primaryGradient, RoundedCornerShape(20.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_photo_camera_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp),
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "Camera",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2D3748)
                                    )
                                }

                                // Gallery Option
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        launcher.launch("image/*")
                                        showDialog = false
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(accentGradient, RoundedCornerShape(20.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_gallery_thumbnail_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp),
                                            tint = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "Gallery",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF2D3748)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            TextButton(onClick = { showDialog = false }) {
                                Text(
                                    "Cancel",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Success Handling
        LaunchedEffect(addState.success) {
            if (addState.success == true) {
                Toast.makeText(context, "✅ Lost item posted successfully!", Toast.LENGTH_SHORT)
                    .show()
                title = ""
                bitmap = null
                imageUri = null
                navController.navigate("Pickme") {
                    popUpTo("Pickme") { inclusive = true }
                }
                onSuccess()
            }
        }

        // Error Handling
        LaunchedEffect(addState.error) {
            addState.error?.let { errorMessage ->
                Toast.makeText(context, "❌ Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }
}



/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLostItemScreen(
    viewModel: TimeTableViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val addState = viewModel.addPost.collectAsState().value
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var userData by remember { mutableStateOf<ProfileData?>(null) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ MODERN COLOR PALETTE
    val primaryGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
    )
    val accentGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFf093fb), Color(0xFFf5576c))
    )
    val successGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
    )

    // Fetch user data
    LaunchedEffect(Unit) {
        if (userId != null) {
            database.child(userId).child("ProfileData").get()
                .addOnSuccessListener { snapshot ->
                    userData = snapshot.getValue(ProfileData::class.java)
                    isLoadingUser = false
                }
                .addOnFailureListener {
                    isLoadingUser = false
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            isLoadingUser = false
        }
    }

    val cLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        capturedBitmap?.let {
            bitmap = it
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

    // Show loading while fetching user data
    if (isLoadingUser) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 4.dp,
                    color = Color(0xFF667eea)
                )
                Text(
                    "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF667eea)
                )
            }
        }
        return
    }

    // ✅ MAIN UI WITH GRADIENT BACKGROUND
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F7FA),
                        Color(0xFFE8EAF6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // ✅ HEADER WITH BACK BUTTON
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF667eea)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Add Lost Item",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )
            }

            Spacer(Modifier.height(24.dp))

            // ✅ IMAGE PICKER CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clickable { showDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap?.asImageBitmap()!!,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )

                        // ✅ EDIT OVERLAY
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .size(48.dp)
                                .background(primaryGradient, CircleShape)
                                .clickable { showDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_photo_camera_24),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF667eea).copy(alpha = 0.1f),
                                                Color(0xFF764ba2).copy(alpha = 0.1f)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "placeholder",
                                    modifier = Modifier.size(40.dp),
                                    tint = Color(0xFF667eea)
                                )
                            }

                            Text(
                                text = "Tap to add photo",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF667eea),
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Upload a clear image of the lost item",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ TITLE INPUT FIELD
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Item Title") },
                placeholder = { Text("e.g., Lost Blue Backpack") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        tint = Color(0xFF667eea)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !addState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667eea),
                    focusedLabelColor = Color(0xFF667eea),
                    cursorColor = Color(0xFF667eea),
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(24.dp))

            // ✅ USER INFO CARD
            if (userData != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF667eea).copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF667eea)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Posting as",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = userData!!.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // ✅ SUBMIT BUTTON WITH GRADIENT
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (imageUri == null) {
                        Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (userId == null) {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val userName = userData?.name ?: "Anonymous"
                    val contact = userData?.mobile ?: "911"
                    val userImageBase64 = userData?.imageUrl ?: ""

                    viewModel.addLostItem(
                        userName = userName,
                        title = title,
                        contactInfo = contact,
                        imageUri = imageUri,
                        context = context,
                        userImageBase64 = userImageBase64,

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !addState.isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(primaryGradient, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (addState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                "Post Lost Item",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ✅ IMAGE PICKER DIALOG
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(enabled = false) { },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Choose Photo",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Camera Option
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    cLauncher.launch()
                                    showDialog = false
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(primaryGradient, RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_photo_camera_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Camera",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                            }

                            // Gallery Option
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    launcher.launch("image/*")
                                    showDialog = false
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(accentGradient, RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_gallery_thumbnail_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Gallery",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2D3748)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        TextButton(onClick = { showDialog = false }) {
                            Text(
                                "Cancel",
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    // Success Handling
    LaunchedEffect(addState.success) {
        if (addState.success == true) {
            Toast.makeText(context, "✅ Lost item posted successfully!", Toast.LENGTH_SHORT).show()
            title = ""
            bitmap = null
            imageUri = null
            navController.navigate("Pickme") {
                popUpTo("Pickme") { inclusive = true }
            }
            onSuccess()
        }
    }

    // Error Handling
    LaunchedEffect(addState.error) {
        addState.error?.let { errorMessage ->
            Toast.makeText(context, "❌ Error: $errorMessage", Toast.LENGTH_LONG).show()
        }
    }
}


 */











/*
@Composable
fun AddLostItemScreen(
    viewModel: TimeTableViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val addState = viewModel.addPost.collectAsState().value
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var userData by remember { mutableStateOf<ProfileData?>(null) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }

    // Fetch user data
    LaunchedEffect(Unit) {
        if (userId != null) {
            database.child(userId).child("ProfileData").get()
                .addOnSuccessListener { snapshot ->
                    userData = snapshot.getValue(ProfileData::class.java)
                    isLoadingUser = false
                }
                .addOnFailureListener {
                    isLoadingUser = false
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        } else {
            isLoadingUser = false
        }
    }

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


    val decodedBitmap = remember(userData?.imageUrl) {
        userData?.imageUrl?.takeIf { it.isNotEmpty() }?.let { base64 ->
            try {
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }


    // Show loading while fetching user data
    if (isLoadingUser) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Add Lost Item",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

///////////////////////////////////////////
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap?.asImageBitmap()!!,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,  // ✅ Crop ki jagah Fit
                    modifier = Modifier
                        .fillMaxWidth()  // ✅ Full width
                        .height(400.dp)  // ✅ Ya jo height chahiye
                        .clip(RectangleShape)
                        .background(Color.White)
                        .border(4.dp, Color.White, RectangleShape)
                        .clickable { showDialog = true }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()  // ✅ Full width
                        .height(400.dp)  // ✅ Same height
                        .clip(RectangleShape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer
                                )
                            )
                        )
                        .border(4.dp, Color.White, RectangleShape)
                        .clickable { showDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "placeholder",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
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



        Spacer(Modifier.height(12.dp))

        // Title Field
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            placeholder = { Text("e.g., Lost Blue Backpack") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !addState.isLoading
        )

        Spacer(Modifier.height(16.dp))


        // Submit Button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {




                // Validation
                if (title.isBlank()) {
                    Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (imageUri == null){
                    Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    return@Button
                }





                // Submit with user data
                val userName = userData?.name ?: "Anonymous"
                val contact = userData?.mobile ?: "911"
                val userImageBase64 = userData?.imageUrl ?: ""


                viewModel.addLostItem(
                     userName,
                    title,
                   contact ,
                    imageUri,
                    context,
                    userImageBase64 =userImageBase64


                )
                navController.navigate("Pickme")
            },
            enabled = !addState.isLoading ,
        ) {
            if (addState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Submit")
            }
        }

        // Display user info
        if (userData != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Posting as: ${userData?.name ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }

    // Success Handling
    LaunchedEffect(addState.success) {
        if (addState.success == true) {
            Toast.makeText(context, "Lost item posted successfully!", Toast.LENGTH_SHORT).show()
            // Clear fields
            title = ""
            onSuccess()
        }
    }

    // Error Handling
    LaunchedEffect(addState.error) {
        addState.error?.let { errorMessage ->
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
        }
    }
}

 */