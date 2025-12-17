package com.svyakus.todo.screen_edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun LargeEditField(
    state: TextFieldValue,
    done: Boolean,
    onValue: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        label = { Text(text = "Дело о") } ,
        value = state,
        onValueChange = onValue,
        minLines = 3,
        maxLines = Int.MAX_VALUE,
        enabled = !done,
        modifier = Modifier.fillMaxWidth()
    )
}
