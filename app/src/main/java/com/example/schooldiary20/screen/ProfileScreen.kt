package com.example.schooldiary20.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.schooldiary20.viewmodel.AuthViewModel
import com.example.schooldiary20.viewmodel.UserInfoState

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val user by viewModel.userInfo.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (user) {
            is UserInfoState.Loading -> LinearProgressIndicator()
            is UserInfoState.Success -> {
                val info = (user as UserInfoState.Success).info
                info.name?.let { Text("ФИО: $it") }
                info.login?.let { Text("Логин: $it") }
                info.password?.let { Text("Пароль:${"*".repeat(7)}") }
                info.email?.let { Text("Email: $it") }
                info.roles.firstOrNull().let { Text("Роль: $it") }
                info.className?.let { Text("Класс: $it") }
            }

            is UserInfoState.Error -> {
                Text("Пользователь не загружен")
                Button({ viewModel.loadUserInfo() }) { Text("Повторить") }
            }

            is UserInfoState.NotLoaded -> {}
        }
        Button(onClick = {
            viewModel.logout()
            navController.navigate("login") {
                popUpTo(0)
            }
        }) {
            Text("Выход")
        }
    }
}