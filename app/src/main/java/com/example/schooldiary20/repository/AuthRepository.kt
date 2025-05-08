package com.example.schooldiary20.repository

import android.util.Log
import com.example.schooldiary20.api.ApiService
import com.example.schooldiary20.data.LoginRequest
import com.example.schooldiary20.data.User
import com.example.schooldiary20.preference.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
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
    override suspend fun login(login: String, password: String): User =
        withContext(Dispatchers.IO) {

            val user = api.loginUser(LoginRequest(login, password))
            Log.i(this@AuthRepositoryImp.javaClass.name, "Ответ сервера: $user")
            prefs.userId = user.userId
            prefs.userRole = user.roles.firstOrNull() ?: ""
            return@withContext user
        }

    override fun isLoggedIn(): Boolean = prefs.userId.isNotEmpty()

    override fun getCurrentUser(): User? {
        return if (isLoggedIn()) {
            Log.i("Берем текущего","${User(prefs.userId, listOf(prefs.userRole), "")}")
            User(prefs.userId, listOf(prefs.userRole), "")
        } else {
            null
        }
    }

    override fun logout() = prefs.clear()

}