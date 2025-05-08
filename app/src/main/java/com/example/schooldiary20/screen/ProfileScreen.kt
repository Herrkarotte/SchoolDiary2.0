package com.example.schooldiary20.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.schooldiary20.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            Text("Имя: ${user!!.userId}")
            Text("Роль:${user!!.roles[0]}")


        } else {
            Text("Пользователь не загружен")
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