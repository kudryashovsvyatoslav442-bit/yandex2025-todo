package com.svyakus.todo.model

import java.util.UUID
import android.graphics.Color
import org.threeten.bp.LocalDateTime

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val color: Int = Color.WHITE,
    val deadline: LocalDateTime? = null,
    val isDone: Boolean = false
) {
    val isExpired: Boolean
        get() = deadline?.isBefore(LocalDateTime.now().minusDays(1)) ?: false
}