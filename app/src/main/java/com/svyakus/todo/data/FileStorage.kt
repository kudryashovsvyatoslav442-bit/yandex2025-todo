package com.svyakus.todo.data

import android.content.Context
import com.svyakus.todo.model.TodoItem
import com.svyakus.todo.model.TodoItemJson
import com.svyakus.todo.model.TodoItemJson.json
import org.json.JSONArray
import org.slf4j.LoggerFactory
import java.io.File

class FileStorage(context: Context) {
    private val file = File(context.filesDir, "todo_items.json")
    private val log = LoggerFactory.getLogger(FileStorage::class.java)
    private val _items: MutableList<TodoItem> = mutableListOf()

    val items: List<TodoItem>
        get() = _items.filter { !it.isExpired }

    init {
        log.info("Init FileStorage, file=${file.absolutePath}")
        loadFromFile()
    }

    fun addItem(item: TodoItem) {
        log.info("Add item: uid={} text={} imp={}", item.uid, item.text, item.importance)
        _items.add(item)
        saveToFile()
    }

    fun removeItem(uid: String) {
        val found = _items.firstOrNull { it.uid == uid }
        if (found != null) {
            log.info("Remove item: uid={} text={}", found.uid, found.text)
            _items.remove(found)
            saveToFile()
        } else {
            log.warn("Remove missed: uid={} not found", uid)
        }
    }

    fun updateItem(item: TodoItem) {
        val idx = _items.indexOfFirst { it.uid == item.uid }
        if (idx >= 0) {
            log.info("Update item: uid={} text={}", item.uid, item.text)
            _items[idx] = item
            saveToFile()
        } else {
            log.warn("Update missed: uid={} not found", item.uid)
        }
    }

    private fun saveToFile() {
        val jsonArray = JSONArray()
        _items.forEach { jsonArray.put(it.json) }
        try {
            file.writeText(jsonArray.toString())
            log.info("Saved {} items to file {}", _items.size, file.name)
        } catch (e: Exception) {
            log.error("Failed to save to file {}", file.absolutePath, e)
        }
    }

    private fun loadFromFile() {
        _items.clear()
        if (!file.exists()) {
            log.warn("File not found, starting with empty list: {}", file.absolutePath)
            return
        }
        try {
            val text = file.readText()
            val jsonArray = JSONArray(text)
            for (i in 0 until jsonArray.length()) {
                TodoItemJson.parse(jsonArray.getJSONObject(i))?.let { item ->
                    _items.add(item)
                }
            }
            log.info("Loaded {} items from file {}", _items.size, file.name)
        } catch (e: Exception) {
            log.error("Failed to load from file {}", file.absolutePath, e)
        }
    }
}
