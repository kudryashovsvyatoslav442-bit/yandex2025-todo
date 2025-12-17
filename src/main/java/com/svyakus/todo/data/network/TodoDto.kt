package com.svyakus.todo.data.network

import com.google.gson.annotations.SerializedName
import com.svyakus.todo.model.Importance
import com.svyakus.todo.model.TodoItem
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

// --- DTO КЛАССЫ ---

data class TodoListResponse(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int
)

data class TodoItemResponse(
    val status: String,
    val element: TodoItemDto,
    val revision: Int
)

data class TodoItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String
)

// --- MAPPERS (ФУНКЦИИ КОНВЕРТАЦИИ) ---
// Они должны быть написаны здесь только ОДИН раз

fun LocalDateTime?.toEpochMillis(): Long? =
    this?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

fun Long?.toLocalDateTime(): LocalDateTime? =
    this?.let { LocalDateTime.ofEpochSecond(it / 1000, ((it % 1000) * 1_000_000).toInt(), ZoneOffset.UTC) }

fun TodoItemDto.toDomain(): TodoItem {
    return TodoItem(
        uid = id,
        text = text,
        importance = when (importance) {
            "low" -> Importance.LOW
            "important" -> Importance.HIGH
            else -> Importance.NORMAL
        },
        deadline = deadline.toLocalDateTime(),
        isDone = done,
        // Простая обработка цвета, чтобы не крашилось
        color = try { android.graphics.Color.parseColor(color) } catch (e: Exception) { android.graphics.Color.WHITE }
    )
}

fun TodoItem.toDto(): TodoItemDto {
    val now = System.currentTimeMillis()
    return TodoItemDto(
        id = uid,
        text = text,
        importance = when (importance) {
            Importance.LOW -> "low"
            Importance.HIGH -> "important"
            Importance.NORMAL -> "basic"
        },
        deadline = deadline.toEpochMillis(),
        done = isDone,
        color = "#FFFFFF",
        createdAt = now,
        changedAt = now,
        lastUpdatedBy = "android_device"
    )
}