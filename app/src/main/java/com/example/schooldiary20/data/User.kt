package com.example.schooldiary20.data

data class User(
    val userId:String,
    val roles:List<String>,
    val classId:String?
    //val token:String?=null TODO добавить токен для улучшения безопасности
)