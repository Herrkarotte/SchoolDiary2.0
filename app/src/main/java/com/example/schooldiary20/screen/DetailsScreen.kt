package com.example.schooldiary20.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.schooldiary20.data.schedule.Lesson
import com.example.schooldiary20.roles.UserRole
import com.example.schooldiary20.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    role: UserRole,
    dayName: String,
    viewModel: ScheduleViewModel,
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
                    LessonsDetailsList(
                        lessons = daySchedule!!.lessons, role = role,
                        onHomeworkUpdate = { lessonId, newHomework ->
                            viewModel.updateHomework(lessonId, newHomework)
                        })
                }
            }
        }
    }
}

@Composable
fun LessonsDetailsList(
    lessons: List<Lesson>, role: UserRole,
    onHomeworkUpdate: (String, String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(lessons) { lesson ->
            LessonDetailsCard(
                lesson, role = role,
                onHomeworkUpdate = { newHomework ->
                    onHomeworkUpdate(lesson.lessonId, newHomework)
                })
        }
    }
}

@Composable
fun LessonDetailsCard(
    lesson: Lesson, role: UserRole,
    onHomeworkUpdate: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedHomework by remember { mutableStateOf(lesson.homework ?: "") }
    LaunchedEffect(lesson.homework) {
        editedHomework = lesson.homework ?: ""
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${lesson.lessonOrder}. ${lesson.subjectName} ${lesson.startTime.dropLast(3)} - ${
                    lesson.endTime.dropLast(
                        3
                    )
                }"
            )
            lesson.teacherName?.let{Text("Преподаватель: $it")}
            Text(text = "Кабинет: ${lesson.roomName}")
            if (lesson.homework != null || role == UserRole.TEACHER) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editedHomework,
                            onValueChange = { editedHomework = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Домашнее задание") }
                        )
                    } else {
                        Text(
                            text = if (lesson.homework.isNullOrEmpty()) {
                                "Домашнее задание: не задано"
                            } else {
                                "Домашнее задание: ${lesson.homework}"
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    if (role == UserRole.TEACHER) {
                        if (isEditing) {
                            IconButton(
                                onClick = {
                                    onHomeworkUpdate(editedHomework)
                                    isEditing = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Сохранить",
                                )
                            }
                            IconButton(
                                onClick = {
                                    isEditing = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Отмена",
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { isEditing = true }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Редактировать"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
