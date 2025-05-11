package com.example.schooldiary20.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.schooldiary20.data.schedule.Lesson
import com.example.schooldiary20.data.schedule.Schedule
import com.example.schooldiary20.viewmodel.ScheduleState
import com.example.schooldiary20.viewmodel.ScheduleViewModel

@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val schedule by viewModel.schedule.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getScheduleForStudent()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (schedule) {
            is ScheduleState.Loading -> LinearProgressIndicator(Modifier.align(Alignment.Center))
            is ScheduleState.Success -> {
                val schedule = (schedule as ScheduleState.Success).schedule
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    schedule.schedule.forEach { day ->
                        stickyHeader {
                            DayCardHeader(day.weekDayName)
                        }

                        item {
                            DaySchedule(day)
                        }
                    }
                }
            }

            is ScheduleState.Error -> {
                Text("Не удалось загрузить расписание")
                Button({ viewModel.getScheduleForStudent() }) { Text("Повторить") }
            }

            is ScheduleState.NotLoaded -> {}
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
            text = "${dayName}"
        )
    }
}

@Composable
fun DaySchedule(day: Schedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
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
        Text(
            text = "${lesson.startTime.dropLast(3)}–${lesson.endTime.dropLast(3)}",

            )
    }
}