package com.svyakus.todo.screen_edit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedColorPickerWrapper(
    show: Boolean,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f)
    ) {
        content()
    }
}