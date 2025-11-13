package com.svyakus.todo.screen_edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorSelectorRow(
    colors: List<Color>,
    selectedColor: Color,
    isCustom: Boolean,
    onPreset: (Color) -> Unit,
    customColor: Color,
    onCustomClick: () -> Unit,
    onCustomLongClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        colors.forEach {
            ColorSwatch(
                color = it,
                selected = !isCustom && it == selectedColor,
                onClick = { onPreset(it) }
            )
        }
        CustomSwatch(
            color = customColor,
            selected = isCustom,
            onClick = onCustomClick,
            onLongClick = onCustomLongClick
        )
    }
}

@Composable
fun ColorSwatch(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(color, RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = { }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun CustomSwatch(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta)
                ),
                RoundedCornerShape(8.dp)
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
