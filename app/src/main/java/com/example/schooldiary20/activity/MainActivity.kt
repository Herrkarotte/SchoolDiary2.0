package com.example.schooldiary20.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.schooldiary20.bottompanel.BottomBar
import com.example.schooldiary20.roles.UserRole
import com.example.schooldiary20.screen.HeadTeacherTestScreen
import com.example.schooldiary20.screen.LoginScreen
import com.example.schooldiary20.screen.ProfileScreen
import com.example.schooldiary20.screen.StudentTestScreen
import com.example.schooldiary20.screen.TeacherTestScreen
import com.example.schooldiary20.ui.theme.SchoolDiary20Theme
import com.example.schooldiary20.viewmodel.AuthState
import com.example.schooldiary20.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchoolDiary20Theme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.authState.collectAsState()
                val currentUser by authViewModel.currentUser.collectAsState()


                LaunchedEffect(authState, currentUser) {
                    Log.d("MainActivity", "AuthState: $authState")
                    Log.d("MainActivity", "CurrentUser: $currentUser")
                }


                val showBottomBar = remember(authState, currentUser) {
                    authState is AuthState.Authorized && currentUser != null &&
                            currentUser!!.roles.isNotEmpty()
                }


                LaunchedEffect(authState) {
                    when (authState) {
                        is AuthState.Authorized -> {
                            if (navController.currentDestination?.route !in listOf(
                                    "profile",
                                    "studentScreen",
                                    "teacherScreen",
                                    "headTeacherScreen"
                                )
                            ) {
                                navController.navigate("profile") {
                                    popUpTo("login") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }

                        is AuthState.Unauthorized -> {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }

                        else -> {}
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            currentUser?.let { user ->
                                val role = UserRole.fromString(user.roles.firstOrNull() ?: "")

                                BottomBar(
                                    navController = navController,
                                    role = role
                                )

                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") { LoginScreen(navController, authViewModel) }
                        composable("profile") { ProfileScreen(navController, authViewModel) }
                        composable("studentScreen") { StudentTestScreen() }
                        composable("teacherScreen") { TeacherTestScreen() }
                        composable("headTeacherScreen") { HeadTeacherTestScreen() }
                    }
                }
            }
        }
    }
}