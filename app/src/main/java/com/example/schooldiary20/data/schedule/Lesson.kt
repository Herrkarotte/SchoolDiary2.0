package com.example.schooldiary20.data.schedule

data class Lesson(
    val lessonId: String,
    val endTime: String,
    val homework: String?,
    val lessonOrder: Int,
    val roomName: String,
    val startTime: String,
    val subjectName: String,
    val teacherName: String?
)