package com.example.schooldiary20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schooldiary20.data.schedule.Schedule
import com.example.schooldiary20.data.schedule.ScheduleResponseItem
import com.example.schooldiary20.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repo: ScheduleRepository) : ViewModel() {
    private val academicYearStartMonth = 9
    private val academicYearStartDay = 1
    private val academicYearEndMonth = 5
    private val academicYearEndDay = 31

    private val _currentWeek = MutableStateFlow(calculateCurrentWeek())
    val currentWeek: StateFlow<Int> = _currentWeek.asStateFlow()

    private val _currentWeekDates = MutableStateFlow<Pair<LocalDate, LocalDate>?>(null)
    val currentWeekDates: StateFlow<Pair<LocalDate, LocalDate>?> = _currentWeekDates.asStateFlow()

    private val _schedule = MutableStateFlow<ScheduleState>(ScheduleState.NotLoaded)
    val schedule: StateFlow<ScheduleState> = _schedule.asStateFlow()

    private val _selectedDay = MutableStateFlow<Schedule?>(null)
    val selectedDay: StateFlow<Schedule?> = _selectedDay.asStateFlow()

    init {
        updateWeekDates()
    }

    fun plusWeek() {
        _currentWeek.value += 1
        updateWeekDates()
    }

    fun minusWeek() {
        _currentWeek.value -= 1
        updateWeekDates()
    }

    private fun updateWeekDates() {
        val startDate = calculateWeekStartDate(_currentWeek.value)
        val endDate = startDate?.plusDays(5)
        if (startDate != null && endDate != null) {
            _currentWeekDates.value = Pair(startDate, endDate)
        }
    }

    private fun calculateWeekStartDate(weekNumber: Int): LocalDate? {
        val today = LocalDate.now()
        val currentYear = today.year

        var academicYearStart = LocalDate.of(
            if (today.monthValue >= academicYearStartMonth) currentYear else currentYear - 1,
            academicYearStartMonth,
            academicYearStartDay
        )

        academicYearStart =
            academicYearStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        return try {
            academicYearStart.plusWeeks(weekNumber.toLong() - 1)
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateCurrentWeek(): Int {
        val today = LocalDate.now()
        val currentYear = today.year

        var startDate = LocalDate.of(
            if (today.monthValue >= academicYearStartMonth) currentYear else currentYear - 1,
            academicYearStartMonth,
            academicYearStartDay
        ).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val endDate = LocalDate.of(
            if (today.monthValue >= academicYearStartMonth) currentYear + 1 else currentYear,
            academicYearEndMonth,
            academicYearEndDay
        )

        return when {
            today.isBefore(startDate) -> 0
            today.isAfter(endDate) -> 0
            else -> {
                val weeksBetween = ChronoUnit.WEEKS.between(startDate, today)
                weeksBetween.toInt() + 1
            }
        }
    }

    fun getScheduleForStudent() {
        viewModelScope.launch {
            _schedule.value = ScheduleState.Loading
            try {
                val schedule = repo.getScheduleForStudent(_currentWeek.value.toString())
                _schedule.value = ScheduleState.Success(schedule)
            } catch (e: Exception) {
                _schedule.value = ScheduleState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
    fun getScheduleForTeacher() {
        viewModelScope.launch {
            _schedule.value = ScheduleState.Loading
            try {
                val schedule = repo.getScheduleForTeacher(_currentWeek.value.toString())
                _schedule.value = ScheduleState.Success(schedule)
            } catch (e: Exception) {
                _schedule.value = ScheduleState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun getDaySchedule(weekDayName: String) {
        viewModelScope.launch {
            _selectedDay.value = null

            when (val current = _schedule.value) {
                is ScheduleState.Success -> {
                    val foundDay = current.schedule.schedule
                        .firstOrNull { it.weekDayName == weekDayName }

                    _selectedDay.value = foundDay ?: run {
                        null
                    }
                }

                ScheduleState.Loading -> {
                }

                else -> {
                }
            }
        }
    }

    fun updateHomework(lessonId:String,newHomework:String) {

    }

}

sealed class ScheduleState {
    object NotLoaded : ScheduleState()
    object Loading : ScheduleState()
    data class Success(val schedule: ScheduleResponseItem) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}
