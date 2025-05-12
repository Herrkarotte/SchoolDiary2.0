package com.example.schooldiary20.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.schooldiary20.data.schedule.Lesson
import com.example.schooldiary20.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    dayName: String,
    viewModel: ScheduleViewModel = hiltViewModel(),
    navController: NavController
) {
    val daySchedule by viewModel.selectedDay.collectAsState()

    LaunchedEffect(dayName) {
        viewModel.getDaySchedule(dayName)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dayName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when {
                daySchedule == null -> {
                    LinearProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                else -> {
                    LessonsDetailsList(lessons = daySchedule!!.lessons)
                }
            }
        }
    }
}

@Composable
fun LessonsDetailsList(lessons: List<Lesson>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(lessons) { lesson ->
            LessonDetailsCard(lesson)
        }
    }
}

@Composable
fun LessonDetailsCard(lesson: Lesson) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${lesson.lessonOrder}. ${lesson.subjectName}"
            )
            Text(
                text = "${lesson.startTime.dropLast(3)} - ${lesson.endTime.dropLast(3)}"
            )
            Text(
                text = "Преподаватель: ${lesson.teacherName}"
            )
            Text(text = "Кабинет: ${lesson.roomName}")
            if (!lesson.homework.isNullOrEmpty()) {
                Text(
                    text = lesson.homework,
                )
            }
        }
    }
}
