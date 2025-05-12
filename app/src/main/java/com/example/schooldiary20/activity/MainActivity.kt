package com.example.schooldiary20.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.schooldiary20.bottompanel.BottomBar
import com.example.schooldiary20.roles.UserRole
import com.example.schooldiary20.screen.DetailsScreen
import com.example.schooldiary20.screen.HeadTeacherTestScreen
import com.example.schooldiary20.screen.LoginScreen
import com.example.schooldiary20.screen.ProfileScreen
import com.example.schooldiary20.screen.ScheduleScreen
import com.example.schooldiary20.screen.TeacherTestScreen
import com.example.schooldiary20.ui.theme.SchoolDiary20Theme
import com.example.schooldiary20.viewmodel.AuthState
import com.example.schooldiary20.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SchoolDiary20Theme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()
    val authState by viewModel.authState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        bottomBar = {
            AnimatedVisibility(
                visible = authState is AuthState.Authorized && currentUser != null,
                enter = slideInVertically(
                    animationSpec = spring(dampingRatio = 0.3f, stiffness = 150f),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 1000),
                    targetOffsetY = { it }
                ))
            {
                val role = UserRole.fromString(currentUser?.roles?.firstOrNull() ?: "")
                BottomBar(navController, role)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") { LoginScreen(navController, viewModel) }
            composable("profile") { ProfileScreen(navController, viewModel) }
            composable("scheduleScreen") { ScheduleScreen(navController) }
            composable("detailsScreen/{dayName}") { backStackEntry ->
                val dayName = backStackEntry.arguments?.getString("dayName") ?: ""
                DetailsScreen(dayName = dayName)
            }
            composable("teacherScreen") { TeacherTestScreen() }
            composable("headTeacherScreen") { HeadTeacherTestScreen() }
        }
    }
}