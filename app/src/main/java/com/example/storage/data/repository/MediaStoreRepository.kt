package com.example.storage.data.repository

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.example.storage.StorageApplication
import com.example.storage.domain.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaStoreRepository
@Inject
constructor(
    private val applicationContext: StorageApplication
) {
    private val TAG = this.javaClass.name

    private var _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos = _videos.asStateFlow()

    fun getVideos() {
        val videoList = mutableListOf<Video>()
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_MODIFIED,
        )

        // Show only videos that are at least 5 minutes in duration.
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES).toString())

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        val query = applicationContext.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )

        Log.d(TAG, "getVideos - query is null = ${query == null}")
        query?.use { cursor ->
            Log.d(TAG, "getVideos - cursor")
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id: Long = cursor.getLong(idColumn)
                val name: String = cursor.getString(nameColumn)
                val duration: Int = cursor.getInt(durationColumn)
                val size: Int = cursor.getInt(sizeColumn)
                val dateModified: Long = cursor.getLong(dateModifiedColumn)

                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList += Video(
                    id = id,
                    contentUri = contentUri,
                    name = name,
                    duration = duration,
                    size = size,
                    dateModified = dateModified
                )
                Log.d(TAG, "getVideos contentUri $contentUri")
            }
        }
        Log.d(TAG, "getVideos - videoList size = ${videoList.size} ")
        _videos.value = videoList
    }

    fun addVideo() {
        // Add a specific media item.
        val resolver = applicationContext.contentResolver


        // Find all videos files on the primary external storage device.
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI


        // Publish a new song.
        val videoDetail = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, "video_${System.currentTimeMillis()}.mp4")
        }

        // Keep a handle to the new song's URI in case you need to modify it
        // later.
        try {
            val videoUri: Uri? = resolver.insert(collection, videoDetail)

            if (videoUri == null) {
                Log.d(TAG, "addVideo - videoUri is null")
            } else {
                resolver.openOutputStream(videoUri)?.use { stream ->
                    // Perform operations on "stream".
                    applicationContext.assets.open("video_sample.mp4").use { inputStream ->
                        stream.write(inputStream.readBytes())
                    }
                }
            }
            Log.d(TAG, "addVideo - success with videoUri $videoUri")
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "addVideo - fail because ${ex.message}")
        }

        // refresh the video list to update UI
        getVideos()
    }

    fun removeVideo(video: Video) {
        // Remove a specific media item.
        val resolver = applicationContext.contentResolver

        try {
            // Perform the actual removal.
            val numImagesRemoved = resolver.delete(
                video.contentUri,
                "${MediaStore.Audio.Media._ID} = ?",
                arrayOf(video.id.toString())
            )
            Log.d(TAG, "removeVideo - numImagesRemoved success = $numImagesRemoved")
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "removeVideo fail ${ex.message}")
        }

        // refresh the video list to update UI
        getVideos()
    }

    fun renameVideo(
        video: Video,
        newName: String,
        onSuccess: ()->Unit = {},
        onFailure: ()->Unit = {},
    ) {
        // Updates an existing media item.
        val mediaId = video.id
        val resolver = applicationContext.contentResolver

        // When performing a single item update, prefer using the ID.
        val selection = "${MediaStore.Audio.Media._ID} = ?"

        // By using selection + args you protect against improper escaping of // values.
        val selectionArgs = arrayOf(mediaId.toString())

        // Update an existing song.
        val updatedSongDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, newName)
        }

        try {
            // Use the individual song's URI to represent the collection that's
            // updated.
            val numSongsUpdated = resolver.update(
                video.contentUri,
                updatedSongDetails,
                selection,
                selectionArgs
            )
            onSuccess()
            Log.d(TAG, "renameVideo success $numSongsUpdated")
        } catch (ex: Exception) {
            onFailure()
            ex.printStackTrace()
            Log.d(TAG, "renameVideo fail ${ex.message}")
        }

        getVideos()
    }
}