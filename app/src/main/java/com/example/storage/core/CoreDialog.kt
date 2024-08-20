package com.example.storage.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun CoreDialog(
    enable: Boolean,
    onDismissRequest: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    if (enable) {
        Dialog(
            onDismissRequest = onDismissRequest,
            content = content
        )
    }
}