package com.svyakus.todo.screen_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
    vm: EditViewModel = viewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Редактировать") }) }
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
