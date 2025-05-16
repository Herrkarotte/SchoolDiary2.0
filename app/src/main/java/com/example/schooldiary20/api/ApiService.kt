package com.example.schooldiary20.api

import com.example.schooldiary20.data.schedule.ScheduleResponse
import com.example.schooldiary20.data.user.LoginRequest
import com.example.schooldiary20.data.user.User
import com.example.schooldiary20.data.user.UserInfoResponse
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

    @GET("Schedules/ByClassId/{classId}/{weekId}")
    suspend fun getScheduleByClassId(
        @Path("classId") classId: String, @Path("weekId") weekId: String
    ): ScheduleResponse

    @GET("Schedules/ByTeacher/{userId}/{weekId}")
    suspend fun getScheduleForTeacher(
        @Path("userId") userId: String, @Path("weekId") weekId: String
    ): ScheduleResponse
}