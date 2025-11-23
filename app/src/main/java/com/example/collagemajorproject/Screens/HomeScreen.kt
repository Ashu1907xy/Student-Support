package com.example.collagemajorproject.Screens


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collagemajorproject.Screens.Notes.NotesStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okio.utf8Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {




    var navBarState by rememberSaveable { mutableIntStateOf(0) }
    var userName by remember { mutableStateOf("Student") }

    // Fetch user name from Firebase
    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("ProfileData")
                .get()
                .addOnSuccessListener { snapshot ->
                    userName = snapshot.child("name").getValue(String::class.java) ?: "Student"
                }
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Welcome Back!",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            userName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("Profile") }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Quick Stats Section
            QuickStatsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Access Section
            Text(
                text = "Quick Access",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickAccessItems) { item ->
                    QuickAccessCard(
                        icon = item.icon,
                        title = item.title,
                        subtitle = item.subtitle,
                        gradientColors = item.gradientColors,
                        onClick = { navController.navigate(item.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recent Activity Section
            Text(
                text = "Faculty Help",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            RecentActivitySection(navController = navController)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickStatsSection() {
    val notesCount = NotesStore.notes.size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(notesCount.toString(), "Notes", Icons.Default.Book)
                StatItem("8", "Subjects", Icons.Default.School)
                StatItem("95%", "Attendance", Icons.Default.CheckCircle)
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
fun QuickAccessCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    gradientColors: List<Color>,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentActivitySection(navController: NavController) {
    val activities = listOf(
        Activity("Faculty Contact", Icons.Default.Book, "Faculty"),
        Activity("CR Contact", Icons.Default.Assessment, "Store"),

        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        activities.forEach { activity ->

            ActivityItem(activity , navController = navController , index = 0)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
fun ActivityItem(activity: Activity, navController: NavController, index: Int) {
    val isDarkTheme = isSystemInDarkTheme()

    // Gradient color combinations
    val gradientColorsLight = listOf(
        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
        listOf(Color(0xFFEC4899), Color(0xFFF59E0B)),
        listOf(Color(0xFF10B981), Color(0xFF06B6D4)),
        listOf(Color(0xFFF59E0B), Color(0xFFEF4444))
    )

    val gradientColorsDark = listOf(
        listOf(Color(0xFF4F46E5), Color(0xFF7C3AED)),
        listOf(Color(0xFFDB2777), Color(0xFFD97706)),
        listOf(Color(0xFF059669), Color(0xFF0891B2)),
        listOf(Color(0xFFD97706), Color(0xFFDC2626))
    )

    val selectedGradients = if (isDarkTheme) gradientColorsDark else gradientColorsLight
    val gradientColors = remember(index) { selectedGradients[index % selectedGradients.size] }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        onClick = {
            navController.navigate(activity.route)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 500f)
                    )
                )
        ) {
            // Background decoration circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.12f),
                    radius = 70.dp.toPx(),
                    center = Offset(size.width * 0.9f, size.height * 0.5f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = 50.dp.toPx(),
                    center = Offset(size.width * 0.15f, size.height * 0.3f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with gradient overlay
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Title and arrow
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = activity.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Arrow indicator
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Navigate",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}



data class QuickAccessItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val gradientColors: List<Color>,
    val route: String,
)

data class Activity(
    val title: String,
    val icon: ImageVector,
    val route: String,
)

val quickAccessItems = listOf(
    QuickAccessItem(
        Icons.Default.Book,
        "My Notes",
        "View all notes",
        listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
        "Notes"
    ),
    QuickAccessItem(
        Icons.Default.DateRange,
        "TimeTable",
        "Class schedule",
        listOf(Color(0xFFEC4899), Color(0xFFF59E0B)),
        "Timetable"
    ),
    QuickAccessItem(
        Icons.Default.Store,
        "PickMe",
        "LostItems",
        listOf(Color(0xFF10B981), Color(0xFF06B6D4)),
        "Pickme"
    ),
    QuickAccessItem(
        Icons.Default.School,
        " Notes / Paper",
        "Study Material",
        listOf(Color(0xFFF59E0B), Color(0xFFEF4444)),
        "Material"
    )
)



