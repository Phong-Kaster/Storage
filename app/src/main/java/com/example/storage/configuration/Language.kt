package com.example.jetpack.configuration

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.storage.R

import java.util.Locale

enum class Language
    (
    val code: String,
    @StringRes val text: Int,
    @DrawableRes val drawable: Int,
) {
    English(code = "en", text = R.string.english, drawable = R.drawable.ic_language_english),
//    German(code = "de", text = R.string.german, drawable = R.drawable.ic_language_german),
//    French(code = "fr", text = R.string.french, drawable = R.drawable.ic_language_french),
//    Hindi(code = "hi", text = R.string.hindi, drawable = R.drawable.ic_language_hindi),
//    Japanese(code = "ja", text = R.string.japanese, drawable = R.drawable.ic_language_japanese),
//    Korean(code = "ko", text = R.string.korean, drawable = R.drawable.ic_language_korean),
//    Vietnam(code = "vi", text = R.string.vietnamese, drawable = R.drawable.ic_language_vietnamese),
    ;

    companion object {
        fun default(): Language = English

        fun getByCode(code: String?): Language {
            if (code == null) return default()
            return entries.firstOrNull { it.code == code } ?: English
        }

        fun getSortedList(): List<Language> {
            val list = entries.toMutableList()
            val defaultCode = Locale.getDefault().language
            val indexOfDefault = list.indexOfFirst { defaultCode == it.code }
            if (indexOfDefault > 0) {
                val item = list.removeAt(indexOfDefault)
                list.add(0, item)
            }
            return list
        }
    }
}