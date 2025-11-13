package com.svyakus.todo.screen_edit

import android.graphics.Color.colorToHSV
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svyakus.todo.data.TodoRepository
import com.svyakus.todo.model.Importance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EditIntent {
    data class ChangeText(val v: TextFieldValue) : EditIntent
    data class LoadTodo(val uid: String) : EditIntent
    data class ChangeDone(val checked: Boolean) : EditIntent
    data class ChangeImportance(val imp: Importance) : EditIntent
    object ShowDate : EditIntent
    object AddTodo : EditIntent
    data class EditTodo(val uid: String) : EditIntent
    object HideDate : EditIntent
    data class PickDate(val millis: Long?) : EditIntent
    data class PickPresetColor(val color: Color) : EditIntent
    data class PickCustomColor(val h: Float, val s: Float, val v: Float) : EditIntent
    object OpenColorChooser : EditIntent
    object HideColorChooser : EditIntent
    object ConfirmCustom : EditIntent
}

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(EditUiState())
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
            is EditIntent.AddTodo -> {
                viewModelScope.launch {
                    val todo = state.value.toTodoItem()
                    repository.add(todo)
                }
            }
            is EditIntent.EditTodo -> {
                viewModelScope.launch {
                    val todo = state.value.toTodoItem(existingUid = intent.uid)
                    repository.update(todo)
                }
            }
            is EditIntent.LoadTodo -> {
                viewModelScope.launch {
                    val loaded = repository.getById(intent.uid)
                    if (loaded != null) {
                        _ui.value = loaded.toEditUiState()
                    } else {
                        _ui.value = EditUiState()
                    }
                }
            }
        }
    }
}
