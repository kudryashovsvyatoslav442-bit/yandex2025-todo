package com.svyakus.todo.model

import android.graphics.Color
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeParseException
import java.util.UUID

object TodoItemJson {
    fun parse(json: JSONObject): TodoItem? {
        return try {
            val uid = json.optString("uid", UUID.randomUUID().toString())
            val text = json.getString("text")
            val importance = when (json.optString("importance", "NORMAL")) {
                "LOW" -> Importance.LOW
                "HIGH" -> Importance.HIGH
                else -> Importance.NORMAL
            }
            val color = json.optInt("color", Color.WHITE)
            val deadline = parseLocalDateTimeSafely(json.optString("deadline", null))
            val isDone = json.optBoolean("isDone", false)

            TodoItem(uid, text, importance, color, deadline, isDone)
        } catch (e: JSONException) {
            Log.e("TodoItemJson", "Failed to parse JSON: $e")
            null
        }
    }

    val TodoItem.json: JSONObject
        get() = JSONObject().apply {
            put("uid", uid)
            put("text", text)
            putOpt("importance", if (importance != Importance.NORMAL) importance.name else null)
            putOpt("color", if (color != Color.WHITE) color else null)
            putOpt("deadline", deadline?.toString())
            put("isDone", isDone)
        }

    private fun parseLocalDateTimeSafely(dateString: String?): LocalDateTime? {
        return dateString?.let {
            try { LocalDateTime.parse(it) } catch (e: DateTimeParseException) { null }
        }
    }
}