package com.svyakus.todo.data

import android.content.Context
import com.svyakus.todo.model.TodoItem
import com.svyakus.todo.model.TodoItemJson
import com.svyakus.todo.model.TodoItemJson.json
import org.json.JSONArray
import java.io.File

class FileStorage(context: Context) {
    private val file = File(context.filesDir, "todo_items.json")
    private val _items: MutableList<TodoItem> = mutableListOf()

    val items: List<TodoItem>
        get() = _items.filter { !it.isExpired }.toList()

    init {
        loadFromFile()
    }

    fun addItem(item: TodoItem) {
        _items.add(item)
        saveToFile()
    }

    fun removeItem(uid: String) {
        _items.removeIf { it.uid == uid }
        saveToFile()
    }

    private fun saveToFile() {
        val jsonArray = JSONArray().apply {
            items.forEach { put(it.json) }
        }
        file.writeText(jsonArray.toString())
    }

    private fun loadFromFile() {
        _items.clear()

        if (file.exists()) {
            val jsonArray = JSONArray(file.readText())
            for (i in 0 until jsonArray.length()) {
                TodoItemJson.parse(jsonArray.getJSONObject(i))?.let { item ->
                    _items.add(item)
                }
            }
        }
    }
}