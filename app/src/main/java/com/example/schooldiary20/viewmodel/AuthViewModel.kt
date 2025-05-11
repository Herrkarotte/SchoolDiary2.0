package com.example.schooldiary20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schooldiary20.data.user.User
import com.example.schooldiary20.data.user.UserInfo
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
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthorized)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userInfo = MutableStateFlow<UserInfoState>(UserInfoState.NotLoaded)
    val userInfo: StateFlow<UserInfoState> = _userInfo.asStateFlow()

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
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Ошибка авторизации")
                _currentUser.value = null
            }
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val userId = _currentUser.value?.userId ?: return@launch
            _userInfo.value = UserInfoState.Loading
            try {
                val info = repo.getUserInfo(userId)
                _userInfo.value = UserInfoState.Success(info)
            } catch (e: Exception) {
                _userInfo.value = UserInfoState.Error(e.message ?: "Ошибка загрузки")
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
                _userInfo.value = UserInfoState.NotLoaded
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

sealed class UserInfoState {
    object NotLoaded : UserInfoState()
    object Loading : UserInfoState()
    data class Success(val info: UserInfo) : UserInfoState()
    data class Error(val message: String) : UserInfoState()
}