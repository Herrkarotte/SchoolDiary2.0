package com.example.schooldiary20.data.user

data class User(
    val userId: String,
    val roles: List<String>,
    val token: String? = null,
    val classId: String? = null
)