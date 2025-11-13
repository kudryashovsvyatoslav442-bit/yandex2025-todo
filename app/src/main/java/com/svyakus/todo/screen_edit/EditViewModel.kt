package com.svyakus.todo.screen_edit

import android.graphics.Color.colorToHSV
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.svyakus.todo.model.Importance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface EditIntent {
    data class ChangeText(val v: TextFieldValue) : EditIntent
    data class ChangeDone(val checked: Boolean) : EditIntent
    data class ChangeImportance(val imp: Importance) : EditIntent
    object ShowDate : EditIntent
    object HideDate : EditIntent
    data class PickDate(val millis: Long?) : EditIntent
    data class PickPresetColor(val color: Color) : EditIntent
    data class PickCustomColor(val h: Float, val s: Float, val v: Float) : EditIntent
    object OpenColorChooser : EditIntent
    object HideColorChooser : EditIntent
    object ConfirmCustom : EditIntent
}

class EditViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val KEY = "EditUiState"
    private val _ui = MutableStateFlow(savedStateHandle.get<EditUiState>(KEY) ?: EditUiState())
    val state: StateFlow<EditUiState> = _ui

    fun dispatch(intent: EditIntent) {
        val s = _ui.value
        when (intent) {
            is EditIntent.ChangeText -> _ui.value = s.copy(form = intent.v)
            is EditIntent.ChangeDone -> _ui.value = s.copy(done = intent.checked)
            is EditIntent.ChangeImportance -> _ui.value = s.copy(type = intent.imp)
            is EditIntent.ShowDate -> _ui.value = s.copy(showDate = true)
            is EditIntent.HideDate -> _ui.value = s.copy(showDate = false)
            is EditIntent.PickDate -> _ui.value = s.copy(deadline = intent.millis, showDate = false)
            is EditIntent.PickPresetColor -> {
                val hsv = FloatArray(3)
                colorToHSV(intent.color.toArgb(), hsv)
                _ui.value = s.copy(
                    hue = hsv[0],
                    sat = hsv[1],
                    lum = hsv[2],
                    custom = false,
                    selectedColor = intent.color
                )
            }
            is EditIntent.PickCustomColor -> _ui.value = s.copy(hue = intent.h, sat = intent.s, lum = intent.v)
            is EditIntent.OpenColorChooser -> _ui.value = s.copy(showColorChooser = true)
            is EditIntent.HideColorChooser -> _ui.value = s.copy(showColorChooser = false)
            is EditIntent.ConfirmCustom -> _ui.value = s.copy(custom = true, showColorChooser = false)
        }
    }

    override fun onCleared() {
        savedStateHandle[KEY] = _ui.value
        super.onCleared()
    }
}
