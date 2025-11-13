package com.svyakus.todo.data

import com.svyakus.todo.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(
    private val storage: FileStorage
) {
    suspend fun getAll(): List<TodoItem> = withContext(Dispatchers.IO) {
        storage.items
    }

    suspend fun getById(id: String): TodoItem? = withContext(Dispatchers.IO) {
        storage.items.firstOrNull { it.uid == id }
    }

    suspend fun add(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.addItem(item)
    }

    suspend fun update(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.updateItem(item)
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        storage.removeItem(id)
    }
}
