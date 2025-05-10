package com.example.schooldiary20.api

import com.example.schooldiary20.data.LoginRequest
import com.example.schooldiary20.data.User
import com.example.schooldiary20.data.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("Users/authenticate")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): User

    @GET("Users/GeneralInfo/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: String
    ): UserInfoResponse
}