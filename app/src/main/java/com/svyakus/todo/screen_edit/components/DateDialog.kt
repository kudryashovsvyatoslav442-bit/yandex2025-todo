package com.svyakus.todo.screen_edit.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    show: Boolean,
    millis: Long?,
    onConfirm: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    if (!show) return
    val state = androidx.compose.material3.rememberDatePickerState(initialSelectedDateMillis = millis)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton({ onConfirm(state.selectedDateMillis) }) { androidx.compose.material3.Text("OK") } },
        dismissButton = { TextButton(onDismiss) { androidx.compose.material3.Text("Cancel") } }
    ) {
        DatePicker(state = state)
    }
}
