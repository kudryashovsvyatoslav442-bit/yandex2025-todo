package com.svyakus.todo.data

import android.util.Log
import com.svyakus.todo.data.network.TodoApi
import com.svyakus.todo.data.network.TodoItemResponse
import com.svyakus.todo.data.network.toDomain
import com.svyakus.todo.data.network.toDto
import com.svyakus.todo.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val storage: FileStorage,
    private val api: TodoApi
) {
    private var currentRevision: Int = 0
    private val tag = "TodoRepo"

    suspend fun getAll(): List<TodoItem> = withContext(Dispatchers.IO) {
        // 1. Всегда сначала возвращаем то, что есть локально
        val localItems = storage.items

        // 2. Пробуем обновить из сети
        try {
            val response = api.getList()
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                currentRevision = body.revision
                Log.d(tag, "Network synced. Revision: $currentRevision")

                val networkItems = body.list.map { it.toDomain() }
                // Обновляем локальную базу
                storage.replaceAll(networkItems)
                return@withContext networkItems
            } else {
                Log.e(tag, "GetList failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "GetList network error", e)
        }

        // Если сеть упала, возвращаем локальные данные
        return@withContext localItems
    }

    suspend fun getById(id: String): TodoItem? = withContext(Dispatchers.IO) {
        storage.items.firstOrNull { it.uid == id }
    }

    suspend fun add(item: TodoItem) = withContext(Dispatchers.IO) {
        // Оптимистичное обновление (сразу сохраняем локально)
        storage.addItem(item)

        try {
            val response = api.addItem(currentRevision, TodoItemResponse("ok", item.toDto(), currentRevision))
            if (response.isSuccessful && response.body() != null) {
                currentRevision = response.body()!!.revision
                Log.d(tag, "Item added to server. New revision: $currentRevision")
            } else {
                Log.e(tag, "Failed to add on server: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Network error during add", e)
        }
    }

    suspend fun update(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.updateItem(item)

        try {
            val response = api.updateItem(item.uid, currentRevision, TodoItemResponse("ok", item.toDto(), currentRevision))
            if (response.isSuccessful && response.body() != null) {
                currentRevision = response.body()!!.revision
                Log.d(tag, "Item updated on server. New revision: $currentRevision")
            } else {
                Log.e(tag, "Failed to update on server: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Network error during update", e)
        }
    }

    suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        storage.removeItem(id)

        try {
            val response = api.deleteItem(id, currentRevision)
            if (response.isSuccessful && response.body() != null) {
                currentRevision = response.body()!!.revision
                Log.d(tag, "Item deleted on server. New revision: $currentRevision")
            } else {
                Log.e(tag, "Failed to delete on server: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Network error during delete", e)
        }
    }
}