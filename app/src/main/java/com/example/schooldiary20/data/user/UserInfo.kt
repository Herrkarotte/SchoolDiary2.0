package com.example.schooldiary20.data.user

data class UserInfo (
    val name:String?,
    val email: String?,
    val login:String?,
    val password:String?,
    val className: String?,
    val roles:List<String?>
)
