package com.example.schooldiary20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schooldiary20.data.schedule.ScheduleResponseItem
import com.example.schooldiary20.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repo: ScheduleRepository) : ViewModel() {
    private val academicYearStartMonth = 9
    private val academicYearStartDay = 1
    private val academicYearEndMonth = 5
    private val academicYearEndDay = 31

    private val _currentWeek = MutableStateFlow(calculateCurrentWeek())


    private val _schedule = MutableStateFlow<ScheduleState>(ScheduleState.NotLoaded)
    val schedule: StateFlow<ScheduleState> = _schedule.asStateFlow()

    fun plusWeek() = _currentWeek.value.plus(1)
    fun minusWeek() = _currentWeek.value.minus(1)

    private fun calculateCurrentWeek(): Int {
        val today = LocalDate.now()
        val currentYear = today.year

        val startDate = LocalDate.of(
            if (today.monthValue >= academicYearStartMonth) currentYear else currentYear - 1,
            academicYearStartMonth,
            academicYearStartDay
        )

        val endDate = LocalDate.of(
            if (today.monthValue >= academicYearStartMonth) currentYear + 1 else currentYear,
            academicYearEndMonth,
            academicYearEndDay
        )

        return when {
            today.isBefore(startDate) -> 0
            today.isAfter(endDate) -> 0
            else -> {
                val daysBetween = ChronoUnit.DAYS.between(startDate, today)
                (daysBetween / 7).toInt() + 1
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

}

sealed class ScheduleState {
    object NotLoaded : ScheduleState()
    object Loading : ScheduleState()
    data class Success(val schedule: ScheduleResponseItem) : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}
