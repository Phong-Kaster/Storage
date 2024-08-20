package com.example.storage.util

import com.example.storage.R
import com.example.storage.domain.enums.VideoFormat

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
}