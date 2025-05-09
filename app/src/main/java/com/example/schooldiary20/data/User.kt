package com.example.schooldiary20.data

data class User(
    val userId: String,
    val roles: List<String>,
    val classId: String? = null,
    val token: String? = null
)