package com.example.storage.ui.fragment

import androidx.lifecycle.viewModelScope
import com.example.storage.core.CoreViewModel
import com.example.storage.data.repository.MediaStoreRepository
import com.example.storage.domain.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mediaStoreRepository: MediaStoreRepository,
) : CoreViewModel() {

    private var _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos = _videos.asStateFlow()

    init {
        collectLatestVideos()
    }

    private fun collectLatestVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreRepository.videos.collectLatest {
                _videos.value = it
            }
        }
    }

    fun getVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreRepository.getVideos()
        }
    }

    fun addVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreRepository.addVideo()
        }
    }

    fun removeVideo(video: Video) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreRepository.removeVideo(video = video)
        }
    }

    fun renameVideo(
        video: Video,
        newName: String,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaStoreRepository.renameVideo(
                video = video, newName = newName,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}