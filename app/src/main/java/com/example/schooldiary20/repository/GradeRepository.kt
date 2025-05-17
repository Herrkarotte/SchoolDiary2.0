package com.example.schooldiary20.repository

import com.example.schooldiary20.api.ApiService
import com.example.schooldiary20.data.grade.GradeResponse
import com.example.schooldiary20.preference.Preferences
import javax.inject.Inject

interface GradeRepository {
    suspend fun getGrades(): GradeResponse
}

class GradeRepositoryImp @Inject constructor(
    private val api: ApiService,
    private val prefs: Preferences
) : GradeRepository {
    override suspend fun getGrades(): GradeResponse {
        return api.getGrade(prefs.userId)
    }
}