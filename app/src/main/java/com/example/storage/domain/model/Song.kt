package com.example.storage.domain.model

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Song(
    val contentUri: Uri,
    val name: String,
    val duration: String,
    val size: Int,
    val thumbnail: Bitmap?
){
}