package com.svyakus.todo.screen_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.svyakus.todo.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TodoListIntent {
    object Load : TodoListIntent
    object Refresh : TodoListIntent
    data class Delete(val uid: String) : TodoListIntent
}

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repo: TodoRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(TodoListUiState(loading = true))
    val state: StateFlow<TodoListUiState> = _ui

    fun dispatch(intent: TodoListIntent) {
        when (intent) {
            is TodoListIntent.Load, is TodoListIntent.Refresh -> {
                _ui.value = _ui.value.copy(loading = true, error = null)
                viewModelScope.launch {
                    runCatching { repo.getAll() }
                        .onSuccess { _ui.value = _ui.value.copy(items = it, loading = false, error = null) }
                        .onFailure { _ui.value = _ui.value.copy(loading = false, error = it.localizedMessage) }
                }
            }
            is TodoListIntent.Delete -> {
                viewModelScope.launch {
                    runCatching {
                        repo.delete(intent.uid)
                        repo.getAll()
                    }.onSuccess { _ui.value = _ui.value.copy(items = it, loading = false) }
                }
            }
        }
    }
}
