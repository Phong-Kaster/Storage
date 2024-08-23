package com.example.storage.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.storage.R
import com.example.storage.domain.enums.VideoFormat
import com.example.storage.domain.model.Video
import kotlin.math.roundToInt

object FileUtil {
    /**
     * get name of an audio file.
     * For example: my_record.mp3 then my_record is name of this audio file.
     * this function will return MY_RECORD AS RESULT
     */
    fun getTrueName(name: String): String {
        var title = ""

        when {
            name.contains(VideoFormat.MP4.name.lowercase(), ignoreCase = true) -> title = name.split(".${VideoFormat.MP4.name.lowercase()}")[0]

            name.contains(VideoFormat.AVI.name.lowercase(), ignoreCase = true) -> title = name.split(".${VideoFormat.AVI.name.lowercase()}")[0]

            name.contains(VideoFormat.MKV.name.lowercase(), ignoreCase = true) -> title = name.split(".${VideoFormat.MKV.name.lowercase()}")[0]

            name.contains(VideoFormat.MOV.name.lowercase(), ignoreCase = true) -> title = name.split(".${VideoFormat.MOV.name.lowercase()}")[0]
        }

        return title
    }

    /**
     * get name of an audio file.
     * For example: my_record.mp3 then my_record is name of this audio file.
     * this function will return MP3 AS RESULT
     */
    fun getTrueExtension(name: String): String {
        var extension = ""

        when {
            name.contains(VideoFormat.MP4.name, ignoreCase = true) -> extension = VideoFormat.MP4.name.lowercase()

            name.contains(VideoFormat.AVI.name, ignoreCase = true) -> extension = VideoFormat.AVI.name.lowercase()

            name.contains(VideoFormat.MKV.name, ignoreCase = true) -> extension = VideoFormat.MKV.name.lowercase()

            name.contains(VideoFormat.MOV.name, ignoreCase = true) -> extension = VideoFormat.MOV.name.lowercase()
        }
        return ".${extension}"// .m4a
    }

    fun isNameUnacceptable(name: String, extension: String): Int {
        if (name.isEmpty()) {
            return R.string.filename_can_not_be_empty
        }

        if (name.isBlank()) {
            return R.string.filename_can_not_be_blank
        }

        if (name.length > 31) {
            return R.string.filename_can_not_have_more_than_31_characters
        }

        val invalidCharacter = listOf(
            "#", "%", "&", "{", "}", "/",
            "<", ">", "*", "?", "$",
            "!", "'", "\"", "\\", ":",
            "@", "+", "`", "|", "=", ".",
            ".${VideoFormat.MP4.name.lowercase()}",
            ".${VideoFormat.AVI.name.lowercase()}",
            ".${VideoFormat.MKV.name.lowercase()}",
            ".${VideoFormat.MOV.name.lowercase()}"
        )

        for (index in invalidCharacter.indices) {
            val character = invalidCharacter[index]
            if (name.contains(character)) {
                return R.string.filename_can_not_have_the_following_character
            }
        }

        return 0
    }

    /**
     * From byte to Kilobyte
     */
    fun Int.toKilobyte(): Float{
        return this * 0.001f
    }

    /**
     * From Byte to Megabyte
     */
    fun Int.toMegabyte(): Float{
        return this * 0.000001f
    }

    fun Float.roundTo(n : Int) : Float {
        return "%.${n}f".format(this).toFloat()
    }

    /**
     * From millisecond of video to total watch time
     * For example: 12381283 milliseconds is equal to 3 hours, 26 minutes, and 21 seconds.
     */
    fun Int.toTotalWatchTime(): String {
        val totalSeconds = (this * 0.001).roundToInt()

        val totalMinutes = totalSeconds / 60

        val hours = totalMinutes / 60
        val minutes = totalMinutes - hours * 60
        val seconds = totalSeconds - hours * 60 * 60 - minutes * 60

//        Log.d("TAG", "toSecond - totalSeconds = $totalSeconds")
//        Log.d("TAG", "toSecond - totalMinutes = $totalMinutes")
//        Log.d("TAG", "toSecond - hours = $hours")
//        Log.d("TAG", "toSecond - minutes = $minutes")
//        Log.d("TAG", "toSecond - seconds = $seconds")

        val readableHours = if (hours > 9) hours.toString() else "0$hours"
        val readableMinutes = if (minutes > 9) minutes.toString() else "0$minutes"
        val readableSeconds = if (seconds > 9) seconds.toString() else "0$seconds"

        return if (hours > 0)
            "${readableHours}:${readableMinutes}:${readableSeconds}"
        else
            "${readableMinutes}:${readableSeconds}"
    }

    fun shareVideo(context: Context, video: Video) {
        if(video.contentUri.path.isNullOrEmpty()) {
            Toast.makeText(context, "Video URI is null", Toast.LENGTH_SHORT).show()
            return
        }


        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, video.contentUri)
            type = "video/mp4"
        }

        val chooserIntent = Intent.createChooser(shareIntent, "")
        context.startActivity(chooserIntent)
    }
}