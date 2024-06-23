package com.example.mytasklist.util

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytasklist.R
import com.example.mytasklist.model.Task
import com.example.mytasklist.theme.myFontFamily

@Composable
fun CustomCard(task: Task, onEditClick: () -> Unit, onRemoveClick: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var titleOverflow by remember { mutableStateOf(false) }
    var detailsOverflow by remember { mutableStateOf(false) }

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        CustomAlertDialog(onclose = { openDialog.value = false }, onConfirm = {
            onRemoveClick()
            openDialog.value = false
        })
    }

    Card(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .padding(
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            ),
//        shape = RoundedCornerShape(10.dp),
        shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = task.title,
                    fontFamily = myFontFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        titleOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
                Text(
                    text = task.details,
                    fontFamily = myFontFamily,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (expanded) Int.MAX_VALUE else 6,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult: TextLayoutResult ->
                        detailsOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
            }


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if ((titleOverflow || detailsOverflow) && !expanded) {
                    Text(
                        text = stringResource(R.string.toque_para_ver_mais),
                        fontFamily = myFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onEditClick() }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(
                                R.string.editar_tarefa
                            )
                        )
                    }
                    IconButton(onClick = { openDialog.value = true }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.excluir_tarefa)
                        )
                    }
                }
            }
        }
    }
}