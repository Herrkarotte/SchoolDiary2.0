package com.example.schooldiary20.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.schooldiary20.R
import com.example.schooldiary20.data.schedule.Lesson
import com.example.schooldiary20.data.schedule.Schedule
import com.example.schooldiary20.roles.UserRole
import com.example.schooldiary20.viewmodel.ScheduleState
import com.example.schooldiary20.viewmodel.ScheduleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScheduleScreen(
    navController: NavController, viewModel: ScheduleViewModel = hiltViewModel(), role: UserRole
) {
    val schedule by viewModel.schedule.collectAsState()
    val currentWeek by viewModel.currentWeek.collectAsState()
    val weekDates by viewModel.currentWeekDates.collectAsState()

    LaunchedEffect(currentWeek) {
        when (role) {
            UserRole.TEACHER -> {
                viewModel.getScheduleForTeacher()
            }

            UserRole.STUDENT -> {
                viewModel.getScheduleForStudent()
            }

            else -> {}
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        WeekSelector(
            weekDates = weekDates,
            onPrevWeek = { viewModel.minusWeek() },
            onNextWeek = { viewModel.plusWeek() })

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize()) {
            when (schedule) {
                is ScheduleState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LinearProgressIndicator()
                    }
                }

                is ScheduleState.Success -> {
                    val schedules = (schedule as ScheduleState.Success).schedule
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        schedules.schedule.forEach { day ->
                            stickyHeader {
                                DayCardHeader(day.weekDayName)
                            }

                            item {
                                DaySchedule(day, onClick = {
                                    viewModel.getDaySchedule(day.weekDayName)
                                    navController.navigate("detailsScreen/${day.weekDayName}")
                                })
                            }
                        }
                    }
                }

                is ScheduleState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Не удалось загрузить расписание")
                        Button(
                            modifier = Modifier.padding(20.dp),
                            onClick = {
                                if (role == UserRole.STUDENT) {
                                    viewModel.getScheduleForStudent()
                                } else {
                                    viewModel.getScheduleForTeacher()
                                }
                            }
                        ) {
                            Text("Повторить")
                        }
                    }
                }

                is ScheduleState.NotLoaded -> {}
            }
        }
    }
}

@Composable
fun WeekSelector(
    weekDates: Pair<LocalDate, LocalDate>?,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevWeek) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Предыдущая неделя")
        }
        weekDates?.let { (startDate, endDate) ->
            val dateFormater = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())
            Text(
                text = "${startDate.format(dateFormater)}-${endDate.format(dateFormater)}"
            )
        }
        IconButton(onClick = onNextWeek) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Следующая неделя")
        }
    }
}

@Composable
fun DayCardHeader(dayName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = dayName
        )
    }
}

@Composable
fun DaySchedule(day: Schedule, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {

            LessonsList(lessons = day.lessons)
        }
    }
}

@Composable
fun LessonsList(lessons: List<Lesson>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        lessons.forEach { lesson ->
            LessonItem(lesson)
        }
    }
}

@Composable
fun LessonItem(lesson: Lesson) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${lesson.lessonOrder}. ${lesson.subjectName}",
        )
        if (!lesson.homework.isNullOrEmpty()) {
            Icon(
                painter = painterResource(id = R.drawable.homework_marker),
                contentDescription = "Есть домашнее задание",
            )
        }
        Text(
            text = "${lesson.startTime.dropLast(3)}–${lesson.endTime.dropLast(3)}",
        )
    }
}