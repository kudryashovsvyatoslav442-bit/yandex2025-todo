package com.svyakus.todo.screen_edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.svyakus.todo.model.Importance

data class EditUiState(
    val form: TextFieldValue = TextFieldValue(""),
    val deadline: Long? = null,
    val done: Boolean = false,
    val type: Importance = Importance.NORMAL,
    val hue: Float = 120f,
    val sat: Float = 0.6f,
    val lum: Float = 0.9f,
    val custom: Boolean = false,
    val selectedColor: Color = Color(0xFFC8E6C9),
    val showDate: Boolean = false,
    val showColorChooser: Boolean = false
) {
    val pickedColor: Color
        get() = if (custom) Color.hsv(hue, sat, lum) else selectedColor
}
