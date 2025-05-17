package com.example.schooldiary20.bottompanel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.schooldiary20.roles.UserRole

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
) {
    object Profile : BottomNavItem(
        route = "profile",
        icon = Icons.Default.Person
    )

    object TeacherScreen : BottomNavItem(
        route = "teacherScreen",
        icon = Icons.Default.Call
    )

    object ScheduleScreen : BottomNavItem(
        route = "scheduleScreen",
        icon = Icons.Default.DateRange
    )

    object HeadTeacherScreen : BottomNavItem(
        route = "headTeacherScreen",
        icon = Icons.Default.AddCircle
    )

    object StatisticScreen : BottomNavItem(
        route = "statisticScreen",
        icon = Icons.Default.Star
    )

    companion object {
        fun getItemsForRole(role: UserRole): List<BottomNavItem> {
            return when (role) {
                UserRole.STUDENT -> listOf(ScheduleScreen, Profile,StatisticScreen)
                UserRole.TEACHER -> listOf(ScheduleScreen, Profile, TeacherScreen)
                UserRole.HEADTEACHER -> listOf(Profile, HeadTeacherScreen)
            }
        }
    }
}