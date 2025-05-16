package com.example.schooldiary20.repository

import com.example.schooldiary20.api.ApiService
import com.example.schooldiary20.data.schedule.ScheduleResponseItem
import com.example.schooldiary20.preference.Preferences
import javax.inject.Inject

interface ScheduleRepository {
    suspend fun getScheduleForStudent(weekId: String): ScheduleResponseItem
    suspend fun getScheduleForTeacher(weekId: String): ScheduleResponseItem
}

class ScheduleRepositoryImp @Inject constructor(
    private val api: ApiService,
    private val prefs: Preferences
) : ScheduleRepository {
    override suspend fun getScheduleForStudent(
        weekId: String
    ): ScheduleResponseItem {
        return api.getScheduleByClassId(prefs.userClass, weekId).firstOrNull()
            ?: throw Exception("Schedule not found")
    }

    override suspend fun getScheduleForTeacher(weekId: String): ScheduleResponseItem {
        return api.getScheduleForTeacher(prefs.userId, weekId).firstOrNull()
            ?: throw Exception("Schedule not found")
    }
}
