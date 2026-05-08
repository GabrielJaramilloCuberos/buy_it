package com.example.buy_it.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buy_it.ui.screens.configuration.Configuration
import com.example.buy_it.ui.screens.editinfo.EditInfo
import com.example.buy_it.ui.screens.login.Login
import com.example.buy_it.ui.screens.profile.Profile
import com.example.buy_it.ui.screens.register.Register
import com.example.buy_it.ui.screens.home.Home
import com.example.buy_it.ui.screens.detail.Detail
import com.example.buy_it.ui.screens.trends.Trends
import com.example.buy_it.ui.screens.comments.Comments
import com.example.buy_it.ui.screens.home.HomeViewModel
import com.example.buy_it.ui.screens.login.LoginViewModel
import com.example.buy_it.ui.screens.prices.Prices
import com.example.buy_it.ui.screens.prices.PricesViewModel
import com.example.buy_it.ui.screens.detail.DetailViewModel
import com.example.buy_it.ui.screens.comments.CommentsViewModel
import com.example.buy_it.ui.screens.configuration.ConfigurationViewModel
import com.example.buy_it.ui.screens.editinfo.EditInfoViewModel
import com.example.buy_it.ui.screens.trends.TrendsViewModel
import com.example.buy_it.ui.screens.revieweditor.ReviewEditor
import com.example.buy_it.ui.screens.splash.SplashScreen
import com.example.buy_it.ui.screens.splash.SplashViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.buy_it.ui.screens.createreview.Createreview
import com.google.firebase.auth.FirebaseAuth
import com.example.buy_it.ui.screens.followlist.FollowList

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Trends : Screen("trends")
    object EditInfo : Screen("editinfo")
    object Configuration : Screen("configuration")

    object Prices : Screen("prices/{productId}") {
        fun createRoute(productId: String) = "prices/$productId"
    }

    object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
    }
    object Comments : Screen("comments/{productId}") {
        fun createRoute(productId: String) = "comments/$productId"
    }
    object UserProfile : Screen("userProfile/{userId}") {
        fun createRoute(userId: String) = "userProfile/$userId"
    }
    object CreateReview : Screen("createreview")
    object ReviewEditorScreen : Screen("review_editor/{productId}?reviewId={reviewId}") {
        fun createRoute(productId: String, reviewId: String? = null): String {
            return if (reviewId == null) {
                "review_editor/$productId"
            } else {
                "review_editor/$productId?reviewId=$reviewId"
            }
        }
    }
    object FollowList : Screen("followList/{userId}/{type}") {
        fun createRoute(userId: String, type: String) = "followList/$userId/$type"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ){

        composable(route = Screen.Splash.route){
            SplashScreen(
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                splashViewModel = hiltViewModel()
            )
        }

        composable(route = Screen.Login.route){
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state by loginViewModel.uiState.collectAsState()

            if(state.navigate){
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                loginViewModel.toogleNavigation()
            }
            Login(
                loginViewModel = loginViewModel,
                onRegisterButtonPressed = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(route = Screen.Register.route){
            Register(
                registerButtonPressed = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackScreen = {
                    navController.popBackStack()
                },
            )
        }

        composable(route = Screen.Profile.route){
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
            Profile(
                onProfileEdit = {
                    navController.navigate(Screen.EditInfo.route)
                },
                onConfigurationEdit = {
                    navController.navigate(Screen.Configuration.route)
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onProfileClick = { /* Ya estamos aquí */ },
                onTrendsClick = {
                    navController.navigate(Screen.Trends.route) {
                        launchSingleTop = true
                    }
                },
                onOpenDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
                onFollowersClick = { id ->
                    navController.navigate(Screen.FollowList.createRoute(id, "followers"))
                },
                onFollowingClick = { id ->
                    navController.navigate(Screen.FollowList.createRoute(id, "following"))
                },
                userId = currentUserId,
            )
        }

        composable(route = Screen.EditInfo.route){
            val editInfoViewModel: EditInfoViewModel = hiltViewModel()
            EditInfo(
                onSaveChanges = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                viewModel = editInfoViewModel
            )
        }

        composable(route = Screen.Configuration.route){
            val configurationViewModel: ConfigurationViewModel = hiltViewModel()
            Configuration(
                onBackPressed = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                configurationViewModel = configurationViewModel
            )
        }

        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            Home(
                onOpenDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
                homeViewModel = homeViewModel
            )
        }

        composable(route = Screen.Comments.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            val commentsViewModel: CommentsViewModel = hiltViewModel()
            Comments(
                productId = productId,
                onBackPressed = { navController.popBackStack() },
                onNotificationClick = { /* TODO */ },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                commentsViewModel = commentsViewModel
            )
        }

        composable(route = Screen.Detail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            val detailViewModel: DetailViewModel = hiltViewModel()

            val refresh = backStackEntry.savedStateHandle.get<Boolean>("refresh_detail") == true

            if (refresh) {
                detailViewModel.loadProductDetail(productId)
                backStackEntry.savedStateHandle["refresh_detail"] = false
            }

            Detail(
                productId = productId,
                onBackPressed = { navController.popBackStack() },
                onSeeStores = {
                    navController.navigate(Screen.Prices.createRoute(productId))
                },
                onOpenReviewEditor = { selectedProductId ->
                    navController.navigate(
                        Screen.ReviewEditorScreen.createRoute(selectedProductId)
                    )
                },
                onEditReview = { selectedProductId, reviewId ->
                    navController.navigate(
                        Screen.ReviewEditorScreen.createRoute(
                            productId = selectedProductId,
                            reviewId = reviewId
                        )
                    )
                },
                onNavigateToProfile = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                },
                detailViewModel = detailViewModel
            )
        }

        composable(route = Screen.Trends.route) {
            val trendsViewModel: TrendsViewModel = hiltViewModel()
            Trends(
                onOpenDetail = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                trendsViewModel = trendsViewModel
            )
        }

        composable(
            route = Screen.ReviewEditorScreen.route,
            arguments = listOf(
                navArgument("reviewId") {
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            val reviewId = backStackEntry.arguments?.getString("reviewId")

            ReviewEditor(
                productId = productId,
                reviewId = reviewId,
                onBackPressed = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_detail", value = true)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

            Profile(
                userId = userId,
                onProfileEdit = {
                    navController.navigate(Screen.EditInfo.route)
                },
                onConfigurationEdit = {
                    navController.navigate(Screen.Configuration.route)
                },
                onHomeClick = { navController.navigate(Screen.Home.route) },
                onProfileClick = {
                    if (userId == currentUserId) {
                        // Ya estamos en nuestro perfil, un poco redundante si es pero pues se logra la logica
                        //Entonces el fin justifica el medio :)
                    } else {
                        navController.navigate(Screen.Profile.route)
                    }
                },
                onTrendsClick = { navController.navigate(Screen.Trends.route) },
                onOpenDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
                onFollowersClick = { id ->
                    navController.navigate(Screen.FollowList.createRoute(id, "followers"))
                },
                onFollowingClick = { id ->
                    navController.navigate(Screen.FollowList.createRoute(id, "following"))
                }
            )
        }

        composable(route = Screen.Prices.route) {
            val pricesViewModel: PricesViewModel = hiltViewModel()
            Prices(pricesViewModel = pricesViewModel)
        }

        composable(route = Screen.CreateReview.route){
            Createreview(
                onReviewSubmitted = {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(
            route = Screen.FollowList.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId").orEmpty()
            val type = backStackEntry.arguments?.getString("type").orEmpty()

            FollowList(
                userId = userId,
                type = type,
                onBackPressed = { navController.popBackStack() },
                onUserClick = { selectedUserId ->
                    navController.navigate(Screen.UserProfile.createRoute(selectedUserId))
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NavigationPreview() {
    val navController = rememberNavController()
    AppNavigation(navController = navController)
}