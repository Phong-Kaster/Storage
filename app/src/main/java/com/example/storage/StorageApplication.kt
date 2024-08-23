package com.example.storage

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import coil.util.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StorageApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.01)
                    .directory(cacheDir)
                    .build()
            }
            .respectCacheHeaders(false)
            .logger(DebugLogger())
            .build()
    }
}