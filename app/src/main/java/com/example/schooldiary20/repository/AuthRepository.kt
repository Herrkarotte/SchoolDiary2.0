package com.example.schooldiary20.repository

import com.example.schooldiary20.api.ApiService
import com.example.schooldiary20.data.user.LoginRequest
import com.example.schooldiary20.data.user.User
import com.example.schooldiary20.data.user.UserInfo
import com.example.schooldiary20.preference.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun getUserInfo(userId: String): UserInfo
    suspend fun login(login: String, password: String): User
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): User?
    fun logout()
}

class AuthRepositoryImp @Inject constructor
    (
    private val api: ApiService,
    private val prefs: Preferences
) : AuthRepository {
    override suspend fun getUserInfo(userId: String): UserInfo {
        return api.getUserInfo(userId).firstOrNull() ?: throw Exception("User info not found")
    }

    override suspend fun login(login: String, password: String): User =
        withContext(Dispatchers.Main) {
            val user = api.loginUser(LoginRequest(login, password))
            prefs.userId = user.userId
            prefs.userRole = user.roles.firstOrNull() ?: ""
            prefs.userToken = user.token ?: ""
            prefs.userClass = user.classId ?: ""
            return@withContext user
        }

    override fun isLoggedIn(): Boolean = prefs.userId.isNotEmpty()

    override fun getCurrentUser(): User? {
        return if (isLoggedIn()) {
            User(prefs.userId, listOf(prefs.userRole), "")
        } else {
            null
        }
    }

    override fun logout() = prefs.clear()
}