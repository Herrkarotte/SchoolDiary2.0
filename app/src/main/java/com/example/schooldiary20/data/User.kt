package com.example.schooldiary20.data

data class User(
    val userId: String,
    val roles: List<String>,
    val token: String? = null
)