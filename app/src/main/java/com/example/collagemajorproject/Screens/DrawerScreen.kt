package com.example.collagemajorproject.Screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthState
import com.example.collagemajorproject.ViewModel.AuthViewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.example.collagemajorproject.DataModel.ProfileData

@Composable
fun DrawerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    val drawerItems = listOf(
        DrawerItems(Icons.Default.Person, "Profile", 0, false, "profile"),
    //    DrawerItems(Icons.Default.Settings, "Settings", 0, false, "settings"),
        DrawerItems(Icons.Default.Info, "About", 0, false, "about"),
    //    DrawerItems(Icons.Default.Help, "Help & Support", 0, false, "help"),
        DrawerItems(Icons.Default.Logout, "Logout", 0, false, "Logout"),
    )

    var selectedItem by remember { mutableStateOf(drawerItems[0]) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerContent(
                    drawerItems = drawerItems,
                    selectedItem = selectedItem,
                    onItemSelected = { item ->
                        selectedItem = item

                        if (item.route == "Logout") {
                            authViewModel.signout()
                        } else {
                            navController.navigate(item.route)
                        }

                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HomeScreen(modifier = Modifier, navController = navController)
        }
    }
}

@Composable
fun DrawerContent(
    drawerItems: List<DrawerItems>,
    selectedItem: DrawerItems,
    onItemSelected: (DrawerItems) -> Unit,
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val userId = auth.currentUser?.uid

    var userData by remember { mutableStateOf<ProfileData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (userId != null) {
            database.child(userId).child("ProfileData").get()
                .addOnSuccessListener {
                    userData = it.getValue(ProfileData::class.java)
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header Section with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                    color = Color.White
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Profile Image with Shadow
                    Card(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        if (decodedBitmap != null) {
                            Image(
                                bitmap = decodedBitmap.asImageBitmap(),
                                contentDescription = "Profile Picture",
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
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default Profile",
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Name
                    Text(
                        text = userData?.name ?: "User Name",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // User Email
                    Text(
                        text = userData?.email ?: "user@example.com",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            drawerItems.forEach { item ->
                val isLogout = item.route == "Logout"

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = item.text,
                            fontSize = 16.sp,
                            fontWeight = if (item == selectedItem) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    selected = item == selectedItem,
                    onClick = { onItemSelected(item) },
                    modifier = Modifier.padding(vertical = 4.dp),
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.text,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    badge = {
                        if (item.hasBadge) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error
                            ) {
                                Text(
                                    text = item.badgeCount.toString(),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = if (isLogout)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = if (isLogout)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary,
                        selectedTextColor = if (isLogout)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary,
                        unselectedContainerColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Divider before logout
                if (isLogout && item != drawerItems.first()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Version 1.0.0",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "© 2025 College App",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

data class DrawerItems(
    val icon: ImageVector,
    val text: String,
    val badgeCount: Int,
    val hasBadge: Boolean,
    val route: String,
)



