package com.example.buy_it

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buy_it.navigation.AppNavigation
import com.example.buy_it.navigation.Screen
import com.example.buy_it.ui.components.BarNav
import com.example.buy_it.ui.components.MainBackground
import com.example.buy_it.ui.components.ProfileAsyncImage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val drawerItems = listOf(
    DrawerItem("Actividad de seguidos", Icons.Default.Home, Screen.Home.route),
    DrawerItem("Catálogo de productos", Icons.Default.TrendingUp, Screen.Trends.route),
    DrawerItem("Perfil", Icons.Default.Person, Screen.Profile.route),
    DrawerItem("Configuraciones", Icons.Default.Settings, Screen.Configuration.route),
)


@Composable
fun BuyIt(
    modifier: Modifier = Modifier
){
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            Log.d("Token", token)

            if (!userId.isNullOrBlank()) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .set(
                        mapOf("fcmToken" to token),
                        SetOptions.merge()
                    )
            }
        } else {
            Log.d("Token", "Error al obtener el token")
        }
    }
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val showTopBar = currentRoute != null &&
            currentRoute != Screen.Login.route &&
            currentRoute != Screen.Register.route &&
            currentRoute != Screen.Profile.route &&
            currentRoute != Screen.EditInfo.route &&
            currentRoute != Screen.Configuration.route &&
            currentRoute != Screen.Splash.route

    val showNavBar = currentRoute != null &&
            currentRoute != Screen.Login.route &&
            currentRoute != Screen.Register.route &&
            currentRoute != Screen.Comments.route &&
            currentRoute != Screen.Splash.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val photoUrl: String = currentUser?.photoUrl?.toString() ?: ""

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("NotificationPermission", "Permiso de notificación otorgado")
            } else {
                Log.d("NotificationPermission", "Permiso de notificación denegado")
            }
        }
    )

    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (showTopBar) {
            MainBackground()
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = showTopBar || showNavBar,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Text(
                        text = "buy it.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    drawerItems.forEach { item ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = item.title,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            selected = currentRoute == item.route,
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedContainerColor = Color.Transparent,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    if (showTopBar) {
                        BuyItTopAppBar(
                            imageProfile = photoUrl,
                            profileClicked = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    if (showNavBar) {
                        BarNav(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            onHomeClick = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            onBuscarClick = { navController.navigate(Screen.Trends.route) },
                            onProfileClick = { navController.navigate(Screen.Profile.route) },
                            currentRoute = currentRoute
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyItTopAppBar(
    imageProfile: String,
    profileClicked: () -> Unit = {}
){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        title = {
            Text(
                text = "buy it.",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight(510),
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.17f to colorResource(R.color.graybluebuyit),
                            1f to colorResource(R.color.navybluebuyit)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(80f, -40f)
                    ),
                    textAlign = TextAlign.Center
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = profileClicked
            ) {
                ProfileAsyncImage(
                    profileLink = imageProfile,
                    size = 40
                )
            }
        }
    )
}
