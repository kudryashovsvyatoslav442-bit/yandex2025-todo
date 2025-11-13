package com.svyakus.todo.screen_edit

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import com.svyakus.todo.screen_edit.components.AnimatedColorPickerWrapper
import com.svyakus.todo.screen_edit.components.HueSVPicker

@Composable
fun ColorDialog(
    show: Boolean,
    h: Float, s_: Float, v: Float,
    onHSV: (Float, Float, Float) -> Unit,
    onDismiss: () -> Unit,
    onDone: () -> Unit
) {
    if (!show) return
    AnimatedColorPickerWrapper(
        show = show,
        content = {
            Dialog(onDismissRequest = onDismiss) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(initialScale = 0.85f),
                    exit = fadeOut() + scaleOut(targetScale = 0.9f)
                ) {
                    Surface(
                        tonalElevation = 8.dp
                    ) {
                        Column(
                            modifier = androidx.compose.ui.Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            HueSVPicker(h = h, s = s_, v = v, onPick = onHSV)
                            Row {
                                TextButton(onDismiss) { androidx.compose.material3.Text("Cancel") }
                                TextButton(onDone) { androidx.compose.material3.Text("Done") }
                            }
                        }
                    }
                }
            }
        }
    )
}