package com.example.storage.ui.fragment

import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.viewModelScope
import com.example.storage.StorageApplication
import com.example.storage.core.CoreViewModel
import com.example.storage.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val applicationContext: StorageApplication
) : CoreViewModel() {

    private var _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    init {
        getAllImages()
        detectChanges()
    }

     fun getAllImages() {
         Log.d(TAG, "getAllSongs")
//         val collection =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
//            else
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

         val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )

        // Show only videos that are at least 5 minutes in duration.
        val selection = "${MediaStore.Images.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString()
        )

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"


         val songList = mutableListOf<Song>()
         viewModelScope.launch(Dispatchers.IO) {


             applicationContext.contentResolver.query(
                 collection,
                 projection,
                 null,
                 null,
                 sortOrder
             )?.use { cursor ->
                 Log.d(TAG, "getEntireVideos cursor")
                 Log.d(TAG, "getAllImages moveToNext = ${cursor.moveToNext()} ")
                 while (cursor.moveToNext()) {
                     // Use an ID column from the projection to get
                     // a URI representing the media item itself.

                     // Cache column indices.
                     val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                     val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                     val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                     val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                     while (cursor.moveToNext()) {
                         // Get values of columns for a given video.
                         val id = cursor.getLong(idColumn)
                         val name = cursor.getString(nameColumn)
                         val duration = cursor.getString(durationColumn)
                         val size = cursor.getInt(sizeColumn)

                         val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                         val thumbnail: Bitmap? = loadThumbnail(contentUri = contentUri)

                         // Stores column values and the contentUri in a local object
                         // that represents the media file.
                         //videoList += Video(contentUri, name, duration, size)

                         Log.d(TAG, "Images has contentUri $contentUri with name $name, duration $duration")
                         songList += Song(contentUri, name, duration, size, null)
                     }
                 }

                 Log.d(TAG, "getEntireVideos - size = ${songList.size}")
                 _songs.value = songList
             }
         }


    }

    private fun loadThumbnail(contentUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            applicationContext.contentResolver.loadThumbnail(contentUri, Size(640, 480), null)
        else
            null
    }

    fun addSong(){


        // Add a specific media item.
        val resolver = applicationContext.contentResolver

        // Find all Images files on the primary external storage device.
        val ImagesCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
             else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI



        // Publish a new song.
        val newSongDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "MySong1.mp3")
        }

        // Keep a handle to the new song's URI in case you need to modify it
        // later.
        try {
            val songUri: Uri? = resolver.insert(ImagesCollection, newSongDetails)
            Log.d(TAG, "addSong success with songUri $songUri")
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "addSong fail because ${ex.message}")
        }
    }

    private fun detectChanges(){
        val observer = object : ContentObserver(null) {override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            viewModelScope.launch {
                getAllImages()
            }
        }
        }
        applicationContext.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )
    }
}