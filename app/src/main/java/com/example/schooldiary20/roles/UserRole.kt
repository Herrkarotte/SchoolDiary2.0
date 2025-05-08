package com.example.schooldiary20.roles

enum class UserRole {
    STUDENT, TEACHER, HEADTEACHER;

    companion object {
        fun fromString(role: String): UserRole {
            return when (role) {
                "Студент" -> STUDENT
                "Учитель" -> TEACHER
                "Завуч" -> HEADTEACHER
                else -> STUDENT
            }
        }
    }
}