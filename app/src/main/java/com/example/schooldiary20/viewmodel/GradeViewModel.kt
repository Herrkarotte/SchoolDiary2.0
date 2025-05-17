package com.example.schooldiary20.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schooldiary20.data.grade.GradeResponse
import com.example.schooldiary20.repository.GradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeViewModel @Inject constructor(private val repo: GradeRepository) : ViewModel() {
    private val _grade = MutableStateFlow<GradeState>(GradeState.NotLoaded)
    val grade: StateFlow<GradeState> = _grade.asStateFlow()

    fun getGrades() {
        viewModelScope.launch {
            _grade.value = GradeState.Loading
            try {
                val grades = repo.getGrades()
                _grade.value = GradeState.Success(grades)
            } catch (e: Exception) {
                _grade.value = GradeState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}

sealed class GradeState {
    object NotLoaded : GradeState()
    object Loading : GradeState()
    data class Success(val grade: GradeResponse) : GradeState()
    data class Error(val message: String) : GradeState()
}