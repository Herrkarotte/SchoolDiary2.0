package com.example.schooldiary20.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.schooldiary20.data.grade.Grade
import com.example.schooldiary20.data.grade.GradeResponseItem
import com.example.schooldiary20.roles.UserRole
import com.example.schooldiary20.ui.theme.Grade2
import com.example.schooldiary20.ui.theme.Grade3
import com.example.schooldiary20.ui.theme.Grade4
import com.example.schooldiary20.ui.theme.Grade5
import com.example.schooldiary20.viewmodel.GradeState
import com.example.schooldiary20.viewmodel.GradeViewModel


@Composable
fun StatisticScreen(
    navController: NavController,
    viewModel: GradeViewModel = hiltViewModel(),
    role: UserRole
) {
    val grades by viewModel.grade.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getGrades()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (grades) {
            is GradeState.Loading -> LinearProgressIndicator(Modifier.align(Alignment.Center))
            is GradeState.Success -> {
                val allGrades = (grades as GradeState.Success).grade
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(allGrades) { gradeItem ->
                        GradeItem(gradeItem)
                    }
                }
            }

            is GradeState.Error -> {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Text("Не удалось загрузить оценки")
                    Button(
                        modifier = Modifier.padding(20.dp),
                        onClick = {
                            viewModel.getGrades()
                        }
                    ) { Text("Повторить") }
                }
            }

            is GradeState.NotLoaded -> {}
        }
    }
}

@Composable
fun GradeItem(grade: GradeResponseItem) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = grade.name
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(grade.grade) { grade ->
                GradeCard(grade)
            }
        }
    }
}

@Composable
fun GradeCard(grade: Grade) {
    Card(
        modifier = Modifier.size(64.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = grade.value.toString(),
                color = when (grade.value) {
                    2 -> Grade2
                    3 -> Grade3
                    4 -> Grade4
                    5 -> Grade5
                    else -> Color.Black
                }
            )
        }
    }
}