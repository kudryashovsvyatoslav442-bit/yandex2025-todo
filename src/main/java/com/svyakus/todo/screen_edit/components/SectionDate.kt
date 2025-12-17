package com.svyakus.todo.screen_edit.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.text.DateFormat
import java.util.Date

@Composable
fun SectionDate(
    deadline: Long?,
    onPick: () -> Unit,
    done: Boolean
) {
    val formatted = deadline?.let { DateFormat.getDateTimeInstance().format(Date(it)) } ?: "-"
    Button(
        onClick = onPick,
        enabled = !done
    ) {
        Text("Выбрать дату: $formatted")
    }
}
