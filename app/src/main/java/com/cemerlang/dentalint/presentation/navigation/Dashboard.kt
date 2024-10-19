package com.cemerlang.dentalint.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.NavigationItem
import com.cemerlang.dentalint.presentation.ui.blog.BlogScreen
import com.cemerlang.dentalint.presentation.ui.capture.CaptureScreen
import com.cemerlang.dentalint.presentation.ui.checkup.CheckupScreen
import com.cemerlang.dentalint.presentation.ui.home.HomeScreen
import com.cemerlang.dentalint.presentation.ui.notes.NotesScreen

@Composable
fun Dashboard(parentNavController: NavController, username: String) {

    val navController = rememberNavController()
    val isCaptureLoading = rememberSaveable { mutableStateOf(false) }

    val navigationItems = listOf(
        NavigationItem(label = "Home", icon = painterResource(R.drawable.ic_home_filled) to painterResource(R.drawable.ic_home_outlined)),
        NavigationItem(label = "Notes", icon = painterResource(R.drawable.ic_notes_filled) to painterResource(R.drawable.ic_notes_outlined)),
        NavigationItem(label = "Capture", icon = painterResource(R.drawable.ic_capture_filled) to painterResource(R.drawable.ic_capture_outlined)),
        NavigationItem(label = "Blog", icon = painterResource(R.drawable.ic_blog_filled) to painterResource(R.drawable.ic_blog_outlined)),
        NavigationItem(label = "Checkup", icon = painterResource(R.drawable.ic_checkup_filled) to painterResource(R.drawable.ic_checkup_outlined))
    )

    Scaffold(
        topBar = { AppTopBar(parentNavController, navController, username, isCaptureLoading.value) },
        bottomBar = { AppBottomNavigation(navController, navigationItems, isCaptureLoading.value) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = navigationItems[0].label,
            Modifier.padding(innerPadding)
        ) {
//            navigationItems.forEach { item ->
//                composable(item.label) { item.screen() }
//            }
            composable("Home") {
                HomeScreen(
                    onCaptureClick = {
                        navController.navigate("Capture") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onNotesClick = {
                        navController.navigate("Notes") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        parentNavController.navigate("notes_add")
                    },
                    onCheckupHistoryClick = { parentNavController.navigate("checkup_history") },
                    onSearchBlogClick = {
                        navController.navigate("Blog") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onBlogClick = {
                        navController.navigate("Blog") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    onReadBlogClick = { parentNavController.navigate("blog_read/$it") }
                )
            }
            composable("Notes") {
                NotesScreen(
                    onAddClick = { parentNavController.navigate("notes_add") },
                    onItemClick = { parentNavController.navigate("notes_detail/$it") }
                )
            }
            composable("Capture") {
                CaptureScreen(
                    onAnalyzeComplete = { parentNavController.navigate("capture_result/$it") },
                    onLoading = { isCaptureLoading.value = it }
//                    isQuickCaptureNav = isQuickAction
                )
            }
            composable("Blog") {
                BlogScreen { parentNavController.navigate("blog_read/$it") }
            }
            composable("Checkup") {
                CheckupScreen { parentNavController.navigate("appointment/$it") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(parentNavController: NavController, navController: NavController, username: String, isInactive: Boolean = false) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    TopAppBar(
        title = {
            Text(
                text = if (currentRoute == "Home" || currentRoute == null) {
                    "Halo, $username"
                } else {
                    currentRoute
                },
                fontSize = if (currentRoute == "Home" || currentRoute == null) {
                    MaterialTheme.typography.titleMedium.fontSize
                } else {
                    MaterialTheme.typography.headlineSmall.fontSize
                },
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (currentRoute == "Home") {
                IconButton(onClick = { parentNavController.navigate("user") }) {
                    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "profile")
                }
            }
        },
        actions = {
            when (currentRoute) {
                "Capture" -> {
                    IconButton(
                        onClick = { parentNavController.navigate("capture_history") },
                        enabled = !isInactive
//                        modifier = Modifier.border(
//                            1.dp,
//                            MaterialTheme.colorScheme.outline,
//                            CircleShape
//                        )
                    ) {
                        Icon(imageVector = Icons.Outlined.History, contentDescription = "history")
                    }
                }
                "Blog" -> {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "search")
                    }
                }
                "Checkup" -> {
                    IconButton(onClick = { parentNavController.navigate("checkup_history") }) {
                        Icon(imageVector = Icons.Outlined.History, contentDescription = "history")
                    }
                }
            }
            IconButton(
                onClick = { parentNavController.navigate("notification") },
                enabled = !isInactive
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "notification"
                )
            }
            IconButton(
                onClick = { parentNavController.navigate("chatbot") },
                enabled = !isInactive
            ) {
                Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = "chat")
            }
        },
    )
}

@Composable
fun AppBottomNavigation(navController: NavController, navigationItems: List<NavigationItem>, isInactive: Boolean) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
//                        imageVector = if (currentRoute == item.label) item.icon.first else item.icon.second,
                        painter = if (currentRoute == item.label) item.icon.first else item.icon.second,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.label,
                enabled = !isInactive,
                onClick = {
                    if (currentRoute != item.label) {
                        navController.navigate(item.label) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

//private val navigationItems = listOf(
//    NavigationItem(label = "Home", /*{ HomeScreen() },*/ icon = Icons.Filled.Home to Icons.Outlined.Home),
//    NavigationItem(label = "Notes", /*{ NotesScreen() },*/ icon = Icons.Filled.NoteAlt to Icons.Outlined.NoteAlt),
//    NavigationItem(label = "Capture", /*{ CaptureScreen() },*/ icon = Icons.Filled.CameraEnhance to Icons.Outlined.CameraEnhance),
//    NavigationItem(label = "Blog", /*{ BlogScreen() },*/ icon = Icons.AutoMirrored.Filled.TextSnippet to Icons.AutoMirrored.Outlined.TextSnippet),
//    NavigationItem(label = "Checkup", /*{ CheckupScreen() },*/ icon = Icons.Filled.DateRange to Icons.Outlined.DateRange)
//)

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Dashboard(rememberNavController(), "Cemerlang")
}