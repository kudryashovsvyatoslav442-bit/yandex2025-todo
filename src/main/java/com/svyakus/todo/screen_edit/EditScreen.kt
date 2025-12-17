package com.svyakus.todo.screen_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.svyakus.todo.screen_edit.components.ColorSelectorRow
import com.svyakus.todo.screen_edit.components.DateDialog
import com.svyakus.todo.screen_edit.components.ImportanceSelector
import com.svyakus.todo.screen_edit.components.LargeEditField
import com.svyakus.todo.screen_edit.components.SectionDate
import com.svyakus.todo.screen_edit.components.SectionDone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    todoUid: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: EditViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(todoUid) {
        if (todoUid != null) {
            vm.dispatch(EditIntent.LoadTodo(todoUid))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактировать") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(p)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LargeEditField(
                state = state.form,
                done = state.done,
                onValue = { vm.dispatch(EditIntent.ChangeText(it)) }
            )
            SectionDate(
                deadline = state.deadline,
                onPick = { vm.dispatch(EditIntent.ShowDate) },
                done = state.done
            )
            SectionDone(
                checked = state.done,
                onChecked = { vm.dispatch(EditIntent.ChangeDone(it)) }
            )
            ColorSelectorRow(
                colors = listOf(
                    Color(0xFFF44336), Color(0xFFFF9800), Color(0xFFFFEB3B),
                    Color(0xFF8BC34A), Color(0xFF03A9F4)
                ),
                selectedColor = state.selectedColor,
                isCustom = state.custom,
                onPreset = { vm.dispatch(EditIntent.PickPresetColor(it)) },
                customColor = Color.hsv(state.hue, state.sat, state.lum),
                onCustomClick = { vm.dispatch(EditIntent.ConfirmCustom) },
                onCustomLongClick = { vm.dispatch(EditIntent.OpenColorChooser) }
            )

            ImportanceSelector(
                importance = state.type,
                onChange = {
                    vm.dispatch(EditIntent.ChangeImportance(it))
                }
            )

            Button(
                onClick = {
                    if (todoUid == null) {
                        vm.dispatch(EditIntent.AddTodo)
                        onNavigateBack()
                    } else {
                        vm.dispatch(EditIntent.EditTodo(uid = todoUid))
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text(text = "Сохранить")
            }
        }
    }

    DateDialog(
        show = state.showDate,
        millis = state.deadline,
        onConfirm = { vm.dispatch(EditIntent.PickDate(it)) },
        onDismiss = { vm.dispatch(EditIntent.HideDate) }
    )

    ColorDialog(
        show = state.showColorChooser,
        h = state.hue, s_ = state.sat, v = state.lum,
        onHSV = { h, s__, v__ -> vm.dispatch(EditIntent.PickCustomColor(h, s__, v__)) },
        onDismiss = { vm.dispatch(EditIntent.HideColorChooser) },
        onDone = { vm.dispatch(EditIntent.ConfirmCustom) }
    )
}
