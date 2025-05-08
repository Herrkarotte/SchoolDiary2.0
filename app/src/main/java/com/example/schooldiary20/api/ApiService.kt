package com.example.schooldiary20.api

import com.example.schooldiary20.data.LoginRequest
import com.example.schooldiary20.data.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("Users/authenticate")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): User
}