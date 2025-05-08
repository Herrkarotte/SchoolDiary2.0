package com.example.schooldiary20.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schooldiary20.data.User
import com.example.schooldiary20.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkAuth()
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repo.login(login, password)
                _currentUser.value = user
                _authState.value = AuthState.Authorized(user)
                Log.d("AuthVM", "User set: ${user.userId}, role: ${user.roles.firstOrNull()}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ошибка авторизации")
                _currentUser.value = null
            }
        }
    }

    fun checkAuth() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (repo.isLoggedIn()) {
                    val user = repo.getCurrentUser()
                    _currentUser.value = user
                    _authState.value = if (user != null) {
                        AuthState.Authorized(user)
                    } else {
                        AuthState.Unauthorized
                    }
                } else {
                    _authState.value = AuthState.Unauthorized
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Ошибка проверки авторизации")
                _currentUser.value = null
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repo.logout()
                _currentUser.value = null
                _authState.value = AuthState.Unauthorized
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Logout failed")
            }
        }
    }
}

sealed class AuthState {
    object Unauthorized : AuthState()
    object Loading : AuthState()
    data class Authorized(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}