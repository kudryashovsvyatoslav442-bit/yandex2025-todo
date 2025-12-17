package com.svyakus.todo.screen_edit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import com.svyakus.todo.model.Importance
import com.svyakus.todo.model.TodoItem
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import java.time.Instant
import java.util.UUID

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

fun TodoItem.toEditUiState(): EditUiState {
    val hsv = FloatArray(3)
    val baseColor = Color(color)
    android.graphics.Color.colorToHSV(baseColor.toArgb(), hsv)
    return EditUiState(
        form = TextFieldValue(text),
        deadline = deadline.toEpochMillis(),
        done = isDone,
        type = importance,
        hue = hsv[0],
        sat = hsv[1],
        lum = hsv[2],
        custom = false,
        selectedColor = baseColor,
        showDate = false,
        showColorChooser = false
    )
}

fun EditUiState.toTodoItem(existingUid: String? = null): TodoItem {
    val outColor = if (custom) Color.hsv(hue, sat, lum).toArgb() else selectedColor.toArgb()
    return TodoItem(
        uid = existingUid ?: java.util.UUID.randomUUID().toString(),
        text = form.text,
        importance = type,
        color = outColor,
        deadline = deadline.toLocalDateTime(),
        isDone = done
    )
}


private fun LocalDateTime?.toEpochMillis(): Long? =
    this?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

private fun Long?.toLocalDateTime(): LocalDateTime? =
    this?.let { LocalDateTime.ofEpochSecond(it / 1000, ((it % 1000) * 1_000_000).toInt(), ZoneOffset.UTC) }
