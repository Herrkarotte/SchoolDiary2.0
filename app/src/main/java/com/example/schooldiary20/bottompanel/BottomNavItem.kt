package com.example.schooldiary20.bottompanel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.schooldiary20.roles.UserRole

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Profile : BottomNavItem(
        route = "profile",
        icon = Icons.Default.Person,
        title = "Профиль"
    )

    object TeacherScreen : BottomNavItem(
        route = "teacherScreen",
        icon = Icons.Default.Call,
        title = "Тестовый экран"
    )

    object StudentScreen : BottomNavItem(
        route = "studentScreen",
        icon = Icons.Default.Star,
        title = "Тестовый экран"
    )

    object HeadTeacherScreen : BottomNavItem(
        route = "headTeacherScreen",
        icon = Icons.Default.AddCircle,
        title = "Тестовый экран"
    )

    companion object {
        fun getItemsForRole(role: UserRole): List<BottomNavItem> {
            return when (role) {
                UserRole.STUDENT -> listOf(Profile, StudentScreen)
                UserRole.TEACHER -> listOf(Profile, TeacherScreen)
                UserRole.HEADTEACHER -> listOf(Profile, HeadTeacherScreen)
            }
        }
    }
}