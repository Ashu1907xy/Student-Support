package com.example.collagemajorproject.Screens.CollageNotesPaper

import com.example.collagemajorproject.Screens.TimeTable.tabsScreen.ClassTimetableScreen
import com.example.collagemajorproject.Screens.TimeTable.tabsScreen.MidSenTimetableScreen
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdfScanner
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.FlightClass
import androidx.compose.material.icons.filled.Microwave
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.launch






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabNotesPaperScreen(navController: NavController) {

    val tabs = listOf(
        TabNotesPaperItems(
            tittle = "Notes",
            icon = Icons.Default.Book,
            filledIcon = Icons.Filled.Book
        ),
        TabNotesPaperItems(
            tittle = "RgpvPaper",
            icon = Icons.Default.AdfScanner,
            filledIcon = Icons.Filled.AdfScanner
        ),
        TabNotesPaperItems(
            tittle = "MidPaper",
            icon = Icons.Default.Microwave,
            filledIcon = Icons.Filled.Microwave
        ),
        TabNotesPaperItems(
            tittle = "Shivani",
            icon = Icons.Default.BookmarkAdded,
            filledIcon = Icons.Filled.BookmarkAdded
        )

    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Notes/Paper",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "View your Notes , RgpvPapaper , MidSemPaper , Shivani",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Custom styled tab row
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    containerColor = MaterialTheme.colorScheme.surface,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                    .fillMaxHeight()
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                    )
                                    .zIndex(-1f)
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Icon(
                                    imageVector = if (pagerState.currentPage == index) tab.filledIcon else tab.icon,
                                    contentDescription = null,
                                    tint = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = tab.tittle,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (pagerState.currentPage == index)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Medium,
                                    color = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Pager content with animation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                ) {
                    when (page) {
                        0 -> StudyNotesScreen(navController = navController)
                        1 -> RgpvPaperScreen(navController = navController)
                        2->  MidPaperScreen(navController = navController)
                        3 -> ShivaniScreen(navController = navController)
                    }
                }
            }
        }
    }
}

data class TabNotesPaperItems(
    var tittle: String,
    var icon: ImageVector,
    var filledIcon: ImageVector,
)
















//////////////////////////////////////////////////////////







/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen(navController: NavController) {

    val tabs = listOf(
        TabItems(
            tittle = "ClassTime",
            icon = Icons.Default.Class,
            filledIcon = Icons.Filled.Class
        ),
        TabItems(
            tittle = "MidSem",
            icon = Icons.Default.FlightClass,
            filledIcon = Icons.Filled.FlightClass
        )
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Timetable",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "View your class schedules",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Custom styled tab row
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    containerColor = MaterialTheme.colorScheme.surface,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                    .fillMaxHeight()
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.primaryContainer
                                            )
                                        )
                                    )
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Icon(
                                    imageVector = if (pagerState.currentPage == index) tab.filledIcon else tab.icon,
                                    contentDescription = null,
                                    tint = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = tab.tittle,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (pagerState.currentPage == index)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Medium,
                                    color = if (pagerState.currentPage == index)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Pager content with animation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                ) {
                    when (page) {
                        0 -> ClassTimetableScreen(navController = navController)
                        1 -> MidSenTimetableScreen(navController = navController)
                    }
                }
            }
        }
    }
}

data class TabItems(
    var tittle: String,
    var icon: ImageVector,
    var filledIcon: ImageVector,
)


 */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*


@Composable
fun TabScreen(navController: NavController) {


    val tabs = listOf(
        TabItems(
            tittle = "ClassTime",
            icon = Icons.Default.Class,
            filledIcon = Icons.Filled.Class
        ),
        TabItems(
            tittle = "MidSem",
            icon = Icons.Default.FlightClass,
            filledIcon = Icons.Filled.FlightClass
        )
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                       // modifier = Modifier.fillMaxWidth(),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = tab.tittle)
                        },
                        icon = {
                            Icon(
                                imageVector = if (pagerState.currentPage == index) tab.filledIcon else tab.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            HorizontalPager(state = pagerState) {
                when (it) {

                    0 -> ClassTimetableScreen(navController = navController)
                    1 -> MidSenTimetableScreen(navController = navController)

                }
            }
        }


    }

}

data class TabItems(
    var tittle: String,
    var icon: ImageVector,
    var filledIcon: ImageVector,
)


 */