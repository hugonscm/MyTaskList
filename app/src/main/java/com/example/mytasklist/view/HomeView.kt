package com.example.mytasklist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytasklist.R
import com.example.mytasklist.room.RepositoryResponse
import com.example.mytasklist.theme.myFontFamily
import com.example.mytasklist.util.CustomCard
import com.example.mytasklist.util.ThemeSwitcher
import com.example.mytasklist.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeView(
    navController: NavController,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    taskViewModel: TaskViewModel = koinViewModel<TaskViewModel>()
) {

    val uiTaskState by taskViewModel.taskListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tarefas),
                    fontFamily = myFontFamily,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.weight(1f))
                ThemeSwitcher(
                    darkTheme = darkTheme,
                    size = 50.dp,
                    padding = 5.dp,
                    onClick = onThemeUpdated
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (val state = uiTaskState) {
                is RepositoryResponse.Loading -> {
                    LoadingScreen()
                }

                is RepositoryResponse.Success -> {
                    val list = state.data.taskList

                    if (list.isNotEmpty()) {
                        LazyColumn {
                            items(items = list, key = {it.id}) {
                                CustomCard(
                                    task = it,
                                    onEditClick = { navController.navigate("editTaskView/${it.id}") },
                                    onRemoveClick = {
                                        coroutineScope.launch {
                                            taskViewModel.removeTask(it)
                                        }
                                    })
                            }
                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.7f),
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.experimente_adicionar_uma_tarefa_clicando_no_bot_o),
                                fontFamily = myFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }

                is RepositoryResponse.Error -> {
                    Text(text = "Erro desconhecido.")
                }
            }
        }

        FloatingActionButton(modifier = Modifier
            .padding(bottom = 10.dp, end = 10.dp)
            .size(70.dp)
            .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.primary,
            //shape = RoundedCornerShape(10.dp),
            shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
            onClick = {
                navController.navigate("addTaskView") { launchSingleTop = true }
            }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.adicionar_tarefa)
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}