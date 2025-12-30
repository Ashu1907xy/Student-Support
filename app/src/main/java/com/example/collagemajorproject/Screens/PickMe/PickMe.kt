package com.example.collagemajorproject.Screens.PickMe

// PickMeScreen.kt

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.collagemajorproject.DataModel.ProfileData
import com.example.collagemajorproject.ViewModel.TimetableViewModel.TimeTableViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// PostCard mein Image section ko replace karo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickMeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TimeTableViewModel = hiltViewModel(),

    ) {
    val getAllLostItemState = viewModel.getAllLostItemState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid
    var navBarState by rememberSaveable { mutableIntStateOf(0) }


    val items = listOf(
        NavItemState("Home", Icons.Filled.Home, Icons.Default.Home, "drawer"),
        NavItemState("Add", Icons.Filled.Add, Icons.Default.Add, "Addpick"),
        NavItemState("Profile", Icons.Filled.Person, Icons.Default.Person, "Profile"),

        )



    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                            start = Offset(0f, 0f),
                            end = Offset(1500f, 0f)
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "PickMe",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Lost Items",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))   // <-- pushes search icon to the end

                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "search",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )

            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = navBarState == index,
                        onClick = {
                            navBarState = index
                            navController.navigate(item.routes)
                        },
                        icon = {
                            Icon(
                                imageVector = if (navBarState == index) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(item.title)
                        }

                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { innerPadding ->

        when {
            getAllLostItemState.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(70.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                strokeWidth = 6.dp
                            )
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = "Loading ...",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            getAllLostItemState.value.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.size(100.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(24.dp)
                                    .size(52.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Text(
                            text = "Oops! Something went wrong",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = getAllLostItemState.value.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            getAllLostItemState.value.success.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Assignment,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No post ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Waiting for wser uploading post",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    items(getAllLostItemState.value.success) { data ->
                        Eacha(
                            data.userName,
                            data.title,
                            data.contactInfo,
                            data.id,
                            data.imageUrl,
                            data.userImageBase64
                            )

                    }

                }
            }


        }


    }
}



// Pura Eacha composable ko is tarah update karein:

@Composable
fun Eacha(
    userName: String,
    title: String,
    contactInfo: String,
    id: String,
    imageUrl: String,
    userImageBase64 : String
) {
    var showMenu by remember { mutableStateOf(false) }
    var showLikeAnimation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val scale by animateFloatAsState(
        targetValue = if (showLikeAnimation) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { showLikeAnimation = false }
    )

    val decodedBitmap = remember(imageUrl) {
        imageUrl?.takeIf { it.isNotEmpty() }?.let { base64s ->
            try {
                val bytes = Base64.decode(base64s, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }

    val decodedBitmapUser = remember(userImageBase64) {
        userImageBase64?.takeIf { it.isNotEmpty() }?.let { base64ss ->
            try {
                val bytes = Base64.decode(base64ss, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            ),
                            shape = CircleShape
                        )
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (decodedBitmapUser != null){
                        Image(
                            bitmap = decodedBitmapUser.asImageBitmap(),
                            contentDescription = "Lost Item Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "avatar placeholder",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }




                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "2 minutes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = " • ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Bhopal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete Post") },
                            onClick = { showMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Report") },
                            onClick = { showMenu = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Call") },
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${contactInfo}")
                                }
                                context.startActivity(intent)
                                showMenu = false
                            }
                        )
                    }
                }
            }

            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (decodedBitmap != null) {
                    Image(
                        bitmap = decodedBitmap.asImageBitmap(),
                        contentDescription = "Lost Item Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Default Image",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like"
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Comment,
                        contentDescription = "Comment"
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
            }

            // Title Section
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(userName)
                    }
                    append(" ")
                    append(title)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

data class NavItemState(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val routes: String,
)