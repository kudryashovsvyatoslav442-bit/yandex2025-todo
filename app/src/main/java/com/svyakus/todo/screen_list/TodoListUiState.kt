package com.svyakus.todo.screen_list

import com.svyakus.todo.model.TodoItem

data class TodoListUiState(
    val items: List<TodoItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
