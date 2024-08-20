package com.example.storage.domain.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Video(
    val id: Long,
    val contentUri: Uri,
    val name: String,
    val duration: Int,
    val size: Int,
    val thumbnail: Bitmap? = null
){
}