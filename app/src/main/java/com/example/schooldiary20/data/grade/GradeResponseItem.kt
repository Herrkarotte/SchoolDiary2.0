package com.example.schooldiary20.data.grade

data class GradeResponseItem(
    val grade: List<Grade>,
    val name: String,
    val subjectId: String
)