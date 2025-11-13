package com.svyakus.todo.screen_edit.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun HueSVPicker(
    h: Float, s: Float, v: Float,
    onPick: (Float, Float, Float) -> Unit
) {
    var w by remember { mutableStateOf(0f) }
    var hPx by remember { mutableStateOf(0f) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .drawWithContent {
                    val rainbow = listOf(
                        Color.hsv(0f, 1f, v),
                        Color.hsv(60f, 1f, v),
                        Color.hsv(120f, 1f, v),
                        Color.hsv(180f, 1f, v),
                        Color.hsv(240f, 1f, v),
                        Color.hsv(300f, 1f, v),
                        Color.hsv(360f, 1f, v)
                    )
                    drawRect(brush = Brush.horizontalGradient(rainbow))
                    drawRect(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.White)))
                    val cx = (h / 360f).coerceIn(0f, 1f) * this.size.width
                    val cy = (1f - s.coerceIn(0f, 1f)) * this.size.height
                    val cross = 10.dp.toPx()
                    val stroke = 2.dp.toPx()
                    drawLine(Color.Black, Offset(cx - cross, cy - cross), Offset(cx + cross, cy + cross), strokeWidth = stroke)
                    drawLine(Color.Black, Offset(cx - cross, cy + cross), Offset(cx + cross, cy - cross), strokeWidth = stroke)
                }
                .pointerInput(v) {
                    detectDragGestures { change, _ ->
                        if (w == 0f || hPx == 0f) return@detectDragGestures
                        val x = change.position.x.coerceIn(0f, w)
                        val y = change.position.y.coerceIn(0f, hPx)
                        val hNorm = (x / w).coerceIn(0f, 1f)
                        val sNorm = (1f - y / hPx).coerceIn(0f, 1f)
                        onPick(hNorm * 360f, sNorm, v)
                    }
                }
                .onGloballyPositioned {
                    w = it.size.width.toFloat()
                    hPx = it.size.height.toFloat()
                }
        )
        Slider(
            value = v,
            onValueChange = { vv -> onPick(h, s, vv) },
            valueRange = 0f..1f
        )
    }
}
