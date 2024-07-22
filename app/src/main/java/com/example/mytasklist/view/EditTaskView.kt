package com.example.mytasklist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mytasklist.R
import com.example.mytasklist.navigation.canGoBack
import com.example.mytasklist.theme.myFontFamily
import com.example.mytasklist.util.CustomTopAppBar
import com.example.mytasklist.viewmodel.EditTaskViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditTaskView(
    navController: NavController,
    id: Int,
    editTaskViewModel: EditTaskViewModel = koinViewModel<EditTaskViewModel>()
) {
    val editTaskState by editTaskViewModel.taskState.collectAsState()

    val title = editTaskState.task.title
    val details = editTaskState.task.details

    val coroutineScope = rememberCoroutineScope()

    var isTitleError by rememberSaveable { mutableStateOf(false) }
    var isDetailsError by rememberSaveable { mutableStateOf(false) }

    val colorsTextFields = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.9f),
        focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.9f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.onSurface,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.onSurface,
        errorBorderColor = Color.Red,
        errorContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.9f),
        errorPlaceholderColor = MaterialTheme.colorScheme.onSurface
    )

    Column {
        CustomTopAppBar(onBackPressed = {
            if (navController.canGoBack) {
                navController.popBackStack("homeView", false)
            }
        }, barTitle = stringResource(R.string.editar_tarefa))

        Column(
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
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    editTaskViewModel.setTittle(it)
                    isTitleError = false
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                isError = isTitleError,
                supportingText = { if (isTitleError) Text(stringResource(R.string.digite_um_titulo_para_a_tarefa)) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.titulo),
                        fontFamily = myFontFamily,
                        fontSize = 28.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(70.dp, 120.dp),
                colors = colorsTextFields,
                textStyle = TextStyle(fontFamily = myFontFamily, fontSize = 28.sp),
                shape = RoundedCornerShape(10.dp)
                //shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
            )

            Spacer(modifier = Modifier.height(3.dp))

            OutlinedTextField(
                value = details,
                onValueChange = {
                    editTaskViewModel.setDetails(it)
                    isDetailsError = false
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                isError = isDetailsError,
                supportingText = { if (isDetailsError) Text(stringResource(R.string.digite_os_detalhes_da_tarefa)) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.detalhes),
                        fontFamily = myFontFamily,
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                colors = colorsTextFields,
                textStyle = TextStyle(fontFamily = myFontFamily, fontSize = 20.sp),
                shape = RoundedCornerShape(10.dp)
                //shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
            )

            Spacer(modifier = Modifier.height(3.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && details.isNotEmpty()) {

                        coroutineScope.launch {
                            editTaskViewModel.updateTask(editTaskState.task)
                        }

                        navController.popBackStack("homeView", false)
                    } else {
                        if (editTaskState.task.title.isEmpty()) {
                            isTitleError = true
                        }
                        if (editTaskState.task.details.isEmpty()) {
                            isDetailsError = true
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(10.dp),
                //shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
                elevation = ButtonDefaults.buttonElevation(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.editar_tarefa),
                    fontFamily = myFontFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.clickable(true) {
                        if (navController.canGoBack) {
                            navController.popBackStack()
                        }
                    },
                    text = stringResource(R.string.cancelar),
                    fontFamily = myFontFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}