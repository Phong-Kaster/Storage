package com.example.storage.domain.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.storage.R

enum class VideoAction(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
){
    Share(icon = R.drawable.ic_share, text = R.string.share),
    Edit(icon = R.drawable.ic_edit, text = R.string.edit),
    Rename(icon = R.drawable.ic_rename, text = R.string.rename),
    Delete(icon = R.drawable.ic_delete, text = R.string.delete)
}